package io.rizvan.beans.actors.agent;

import io.rizvan.beans.GameState;
import io.rizvan.beans.Weapon;
import io.rizvan.beans.actors.player.PlayerAnswers;
import io.rizvan.beans.actors.player.PlayerMood;
import io.rizvan.beans.knowledge.AgentKnowledge;
import io.rizvan.beans.knowledge.AgentPossibilities;
import io.rizvan.beans.knowledge.KnowledgeItem;
import io.rizvan.entities.WeaponEntity;
import io.rizvan.utils.BayesPythonManager;
import io.rizvan.utils.Pair;
import io.rizvan.utils.PythonGateway;
import jakarta.annotation.PreDestroy;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class DroolsBrain implements AgentsBrain {
    private final AgentKnowledge knowledge;
    private final AgentPossibilities possibilities;
    private final KieContainer kieContainer;
    private final BayesPythonManager bayesNetwork;
    private final MarginalResult marginals;
    private final ConditionalResult conditionals;
    private final List<PlayerMood> foundMoods;

    public DroolsBrain(PythonGateway pythonGateway, PlayerAnswers playerAnswers, List<WeaponEntity> weaponMoodOccurrences) {
        knowledge = new AgentKnowledge();
        knowledge.setPlayerAnswers(playerAnswers);
        possibilities = new AgentPossibilities();
        KieServices kieService = KieServices.Factory.get();
        kieContainer = kieService.getKieClasspathContainer();


        marginals = getMarginalProbabilities(knowledge.getPossibleWeapons());
        conditionals = getConditionalProbabilities(
                knowledge.getPossibleWeapons(),
                marginals,
                knowledge.getStatRelations()
        );

        var speedValues = marginals.getValues().get(Weapon.Stat.SPEED_MOD);
        var damageValues = marginals.getValues().get(Weapon.Stat.DAMAGE);

        HashMap<PlayerMood, HashMap<Number, HashMap<Number, Double>>> speedDamageMoodProbabilities = new HashMap<>();

        Set<PlayerMood> moodsFound = new HashSet<>();
        Set<Number> speedsFound = new HashSet<>();
        Set<Number> damagesFound = new HashSet<>();

        for (var speed : speedValues) {
            for (var damage : damageValues) {
                var speedDamageOccurrenceCount = weaponMoodOccurrences.stream()
                        .filter(o -> o.getDamage() == (int) damage && o.getSpeedModifier() == (double) speed)
                        .count();

                if (speedDamageOccurrenceCount > 0) {
                    for (var mood : PlayerMood.values()) {
                        var speedDamageMoodOccurrenceCount = weaponMoodOccurrences.stream()
                                .filter(wp -> wp.getDamage() == (int) damage &&
                                        wp.getSpeedModifier() == (double) speed &&
                                        wp.getMoods().stream().toList().get(0).getMood() == mood
                                ).count();
                        if (speedDamageMoodOccurrenceCount > 0) {
                            speedsFound.add(speed);
                            damagesFound.add(damage);
                            moodsFound.add(mood);
                            var speedPart = speedDamageMoodProbabilities.getOrDefault(mood, new HashMap<>());
                            var probabilities = speedPart.getOrDefault(speed, new HashMap<>());
                            probabilities.put(damage, (double) speedDamageMoodOccurrenceCount / speedDamageOccurrenceCount);
                            speedPart.putIfAbsent(speed, probabilities);
                            speedDamageMoodProbabilities.putIfAbsent(mood, speedPart);
                        }
                    }
                }
            }
        }

        foundMoods = moodsFound.stream().toList();
        var speedsFoundList = speedsFound.stream().toList();
        var damagesFoundList = damagesFound.stream().toList();

        double[][] moodSpeedDamageConditionals = new double[moodsFound.size()][speedsFoundList.size() * foundMoods.size()];
        var matchingComboIdx = 0;
        for (var speed : speedsFoundList) {
            var found = false;
            for (var damage : damagesFoundList) {
                for (var moodIdx = 0; moodIdx < foundMoods.size(); moodIdx++) {
                    var mood = foundMoods.get(moodIdx);

                    var moodPart = speedDamageMoodProbabilities.get(mood);

                    var speedPart = moodPart.get(speed);
                    if (speedPart == null) continue;

                    var probability = speedPart.get(damage);
                    if (probability == null) continue;

                    moodSpeedDamageConditionals[moodIdx][matchingComboIdx] = probability;
                    found = true;
                }
                if (found) matchingComboIdx++;
            }
        }

        List<String> nodes = new ArrayList<>();
        nodes.add(Weapon.Stat.SPEED_MOD.getName());
        nodes.add(Weapon.Stat.DAMAGE.getName());
        nodes.add(Weapon.Stat.RECHARGE_TIME.getName());
        nodes.add(Weapon.Stat.USES.getName());
        nodes.add(Weapon.Stat.RANGE.getName());
        nodes.add("mood");

        List<String[]> edges = new ArrayList<>();
        edges.add(new String[]{Weapon.Stat.SPEED_MOD.getName(), Weapon.Stat.DAMAGE.getName()});
        edges.add(new String[]{Weapon.Stat.DAMAGE.getName(), Weapon.Stat.RECHARGE_TIME.getName()});
        edges.add(new String[]{Weapon.Stat.DAMAGE.getName(), Weapon.Stat.RANGE.getName()});
        edges.add(new String[]{Weapon.Stat.RECHARGE_TIME.getName(), Weapon.Stat.USES.getName()});
        edges.add(new String[]{Weapon.Stat.SPEED_MOD.getName(), "mood"});
        edges.add(new String[]{Weapon.Stat.DAMAGE.getName(), "mood"});

        bayesNetwork = pythonGateway.getBayesNetwork();
        bayesNetwork.add_nodes(nodes);
        bayesNetwork.add_edges(edges);

        var speedModEntries = marginals.probabilities.get(Weapon.Stat.SPEED_MOD);
        double[][] speedModProbs = new double[speedModEntries.size()][1];
        for (int i = 0; i < speedModProbs.length; i++) {
            speedModProbs[i][0] = speedModEntries.get(i);
        }
        bayesNetwork.add_cpd(Weapon.Stat.SPEED_MOD.getName(), speedModEntries.size(), speedModProbs, null, null);

        var damageGivenSpeedProbs = conditionals.cpds.get(Weapon.Stat.DAMAGE).get(Weapon.Stat.SPEED_MOD);
        bayesNetwork.add_cpd(Weapon.Stat.DAMAGE.getName(), damageGivenSpeedProbs.length, damageGivenSpeedProbs, new String[]{Weapon.Stat.SPEED_MOD.getName()}, new int[]{damageGivenSpeedProbs[0].length});

        var rechargeTimeGivenDamageProbs = conditionals.cpds.get(Weapon.Stat.RECHARGE_TIME).get(Weapon.Stat.DAMAGE);
        bayesNetwork.add_cpd(Weapon.Stat.RECHARGE_TIME.getName(), rechargeTimeGivenDamageProbs.length, rechargeTimeGivenDamageProbs, new String[]{Weapon.Stat.DAMAGE.getName()}, new int[]{rechargeTimeGivenDamageProbs[0].length});

        var rangeGivenDamageProbs = conditionals.cpds.get(Weapon.Stat.RANGE).get(Weapon.Stat.DAMAGE);
        bayesNetwork.add_cpd(Weapon.Stat.RANGE.getName(), rangeGivenDamageProbs.length, rangeGivenDamageProbs, new String[]{Weapon.Stat.DAMAGE.getName()}, new int[]{rangeGivenDamageProbs[0].length});

        var usesGivenRechargeTimeProbs = conditionals.cpds.get(Weapon.Stat.USES).get(Weapon.Stat.RECHARGE_TIME);
        bayesNetwork.add_cpd(Weapon.Stat.USES.getName(), usesGivenRechargeTimeProbs.length, usesGivenRechargeTimeProbs, new String[]{Weapon.Stat.RECHARGE_TIME.getName()}, new int[]{usesGivenRechargeTimeProbs[0].length});

        bayesNetwork.add_cpd(
                "mood",
                foundMoods.size(),
                moodSpeedDamageConditionals,
                new String[]{Weapon.Stat.SPEED_MOD.getName(), Weapon.Stat.DAMAGE.getName()},
                new int[]{speedsFoundList.size(), damagesFoundList.size()}
        );

        bayesNetwork.finalize_model();
    }

    private String[][] getEvidenceList() {
        var evidenceList = new ArrayList<>(knowledge.getPlayerStats()
                .stream()
                .filter(KnowledgeItem::isKnown)
                .map(stat -> new String[]{stat.getName(), stat.getValue().toString()})
                .toList());
        var moodEntity = knowledge.getPlayerAnswers().getValue();
        if (moodEntity != null) {
            var mood = moodEntity.getMood();
            mood.ifPresent(playerMood -> evidenceList.add(new String[]{"mood", "" + foundMoods.indexOf(playerMood)}));
        }

        String[][] evidence = new String[evidenceList.size()][2];
        for (int i = 0; i < evidenceList.size(); i++) {
            evidence[i] = evidenceList.get(i);
        }

        return evidence;
    }

    private String[] getQueryList() {

        var queryList = knowledge.getPlayerStats()
                .stream()
                .filter(stat -> !stat.isKnown())
                .map(KnowledgeItem::getName)
                .toList();

        String[] queries = new String[queryList.size()];
        for (int i = 0; i < queryList.size(); i++) {
            queries[i] = queryList.get(i);
        }

        return queries;
    }

    private void updateKnowledge(String[] query, String[][] evidence) {
        Map<String, Integer> mapResult = bayesNetwork.map_query(query, evidence);
        for (var entry : mapResult.entrySet()) {
            var index = entry.getValue();
            var statName = entry.getKey();
            var weaponStat = Weapon.Stat.fromName(statName);
            switch (statName) {
                case "damage":
                    var damageValue = (Integer) conditionals.queryValues.get(weaponStat).get(index);
                    knowledge.setPlayerDamage(damageValue, false);
                    break;
                case "recharge_time":
                    var rechargeTimeValue = (Long) conditionals.queryValues.get(weaponStat).get(index);
                    knowledge.setPlayerRechargeTime(rechargeTimeValue, false);
                    break;
                case "uses":
                    var usesValue = (Integer) conditionals.queryValues.get(weaponStat).get(index);
                    knowledge.setPlayerAmmoCapacity(usesValue, false);
                    break;
                case "speed_mod":
                    var speedModValue = (Double) marginals.values.get(weaponStat).get(index);
                    knowledge.setPlayerSpeed(speedModValue, false);
                    break;
                case "range":
                    var rangeValue = (Double) conditionals.queryValues.get(weaponStat).get(index);
                    knowledge.setPlayerReach(rangeValue, false);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void reason(GameState gameState) {
        knowledge.setPlayerHitBoxKnowledge(gameState.getPlayer().getHitBox(), true);

        KieSession kieSession = kieContainer.newKieSession("myKsession");
        kieSession.insert(gameState);
        kieSession.insert(gameState.getAgent());
        kieSession.insert(knowledge);
        kieSession.insert(bayesNetwork);
        kieSession.insert(marginals);
        kieSession.insert(conditionals);
        kieSession.insert(foundMoods);
        kieSession.insert(possibilities);
        kieSession.insert(new GetQueriesCallable());
        kieSession.insert(new GetEvidenceCallable());
        kieSession.insert(new UpdateKnowledgeCallable());

        try {
            gameState.getFacts().forEach(kieSession::insert);
            gameState.clearFacts();

            kieSession.getAgenda().getAgendaGroup("inference-group").setFocus();
            kieSession.fireAllRules();

            kieSession.getAgenda().getAgendaGroup("possibilities-group").setFocus();
            kieSession.fireAllRules();

            kieSession.getAgenda().getAgendaGroup("agent-choices-group").setFocus();
            kieSession.fireAllRules();

            kieSession.getAgenda().getAgendaGroup("agent-actions-group").setFocus();
            kieSession.fireAllRules();

            System.out.println("--------------------------");
        } finally {
            kieSession.dispose();
        }
    }

    @Override
    public AgentKnowledge getKnowledge() {
        return knowledge;
    }

    @PreDestroy
    public void cleanup() {
        if (kieContainer != null) {
            kieContainer.dispose();
        }
    }

    private MarginalResult getMarginalProbabilities(List<Weapon> weapons) {

        HashMap<Weapon.Stat, List<Double>> marginalProbabilities = new HashMap<>();
        HashMap<Weapon.Stat, List<Number>> values = new HashMap<>();

        for (var stat : Weapon.Stat.values()) {
            var statCounts = weapons.stream().map(wep -> wep.getStat(stat))
                    .collect(Collectors.groupingBy(val -> val, Collectors.counting()));

            values.put(stat, statCounts.keySet().stream().toList());
            var probabilities = statCounts.values().stream().map(val -> (double) val / weapons.size()).toList();
            marginalProbabilities.put(stat, probabilities);
        }

        return new MarginalResult(values, marginalProbabilities);
    }

    private ConditionalResult getConditionalProbabilities(
            List<Weapon> weapons,
            MarginalResult marginalResult,
            List<Pair<Weapon.Stat, Weapon.Stat>> conditionalRelations
    ) {

        HashMap<Weapon.Stat, HashMap<Weapon.Stat, double[][]>> statCPDs = new HashMap<>();
        HashMap<Weapon.Stat, List<Number>> statQueryValues = new HashMap<>();
        HashMap<Weapon.Stat, List<Number>> statEvidenceValues = new HashMap<>();

        for (var relation : conditionalRelations) {
            var queryStat = relation.getFirst();
            var evidenceStat = relation.getSecond();

            var queryValues = marginalResult.values.get(queryStat);
            var evidenceValues = marginalResult.values.get(evidenceStat);

            double[][] cpd = new double[queryValues.size()][evidenceValues.size()];
            for (int i = 0; i < queryValues.size(); i++) {
                for (int j = 0; j < evidenceValues.size(); j++) {
                    cpd[i][j] = 0.0;
                }
            }

            for (var weapon : weapons) {
                int queryIndex = queryValues.indexOf(weapon.getStat(queryStat));
                int evidenceIndex = evidenceValues.indexOf(weapon.getStat(evidenceStat));

                cpd[queryIndex][evidenceIndex]++;
            }

            // Normalize the counts to probabilities
            for (int i = 0; i < queryValues.size(); i++) {
                for (int j = 0; j < evidenceValues.size(); j++) {
                    var evidenceProb = marginalResult.probabilities.get(evidenceStat).get(j);
                    cpd[i][j] /= weapons.size();
                    cpd[i][j] /= evidenceProb;
                }
            }

            var statCPDValues = new HashMap<Weapon.Stat, double[][]>();
            statCPDValues.put(evidenceStat, cpd);
            statCPDs.put(queryStat, statCPDValues);
            statQueryValues.put(queryStat, queryValues);
            statEvidenceValues.put(evidenceStat, evidenceValues);
        }

        // Use streams to group and count the values
        return new ConditionalResult(statQueryValues, statEvidenceValues, statCPDs);
    }


    public class ConditionalResult {
        private HashMap<Weapon.Stat, List<Number>> queryValues;
        private HashMap<Weapon.Stat, List<Number>> evidenceValues;
        private HashMap<Weapon.Stat, HashMap<Weapon.Stat, double[][]>> cpds;

        public ConditionalResult(HashMap<Weapon.Stat, List<Number>> queryValues, HashMap<Weapon.Stat, List<Number>> evidenceValues, HashMap<Weapon.Stat, HashMap<Weapon.Stat, double[][]>> cpds) {
            this.queryValues = queryValues;
            this.evidenceValues = evidenceValues;
            this.cpds = cpds;
        }

        public HashMap<Weapon.Stat, List<Number>> getQueryValues() {
            return queryValues;
        }

        public HashMap<Weapon.Stat, List<Number>> getEvidenceValues() {
            return evidenceValues;
        }

        public HashMap<Weapon.Stat, HashMap<Weapon.Stat, double[][]>> getCpds() {
            return cpds;
        }
    }

    public class MarginalResult {
        private HashMap<Weapon.Stat, List<Number>> values;
        private HashMap<Weapon.Stat, List<Double>> probabilities;

        public MarginalResult(HashMap<Weapon.Stat, List<Number>> values, HashMap<Weapon.Stat, List<Double>> probabilities) {
            this.values = values;
            this.probabilities = probabilities;
        }

        public HashMap<Weapon.Stat, List<Number>> getValues() {
            return values;
        }

        public HashMap<Weapon.Stat, List<Double>> getProbabilities() {
            return probabilities;
        }
    }

    public class UpdateKnowledgeCallable implements Callable<Void> {
        private String[] query;
        private String[][] evidence;

        public void setParameters(String[] query, String[][] evidence) {
            this.query = query;
            this.evidence = evidence;
        }

        @Override
        public Void call() {
            updateKnowledge(query, evidence);
            return null;
        }
    }

    public class GetQueriesCallable implements Callable<String[]> {
        @Override
        public String[] call() throws Exception {
            return getQueryList();
        }
    }

    public class GetEvidenceCallable implements Callable<String[][]> {
        @Override
        public String[][] call() throws Exception {
            return getEvidenceList();
        }
    }
}
