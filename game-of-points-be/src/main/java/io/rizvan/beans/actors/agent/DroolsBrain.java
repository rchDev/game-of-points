package io.rizvan.beans.actors.agent;

import io.rizvan.beans.GameState;
import io.rizvan.beans.Weapon;
import io.rizvan.beans.knowledge.AgentKnowledge;
import io.rizvan.beans.knowledge.AgentPossibilities;
import io.rizvan.utils.Pair;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import py4j.GatewayServer;
import com.google.gson.Gson;


import java.util.*;
import java.util.stream.Collectors;


public class DroolsBrain implements AgentsBrain {
    @Inject
    Jsonb jsonb;

    private final AgentKnowledge knowledge;
    private final AgentPossibilities possibilities;
    // I want KieSession to reside here
    private final KieContainer kieContainer;
    private List<Weapon> weapons;


    public DroolsBrain() {

        GatewayServer gatewayServer = new GatewayServer();
        gatewayServer.start();

        PythonManager manager = (PythonManager) gatewayServer.getPythonServerEntryPoint(new Class[]{PythonManager.class});

        knowledge = new AgentKnowledge();
        possibilities = new AgentPossibilities();
        KieServices kieService = KieServices.Factory.get();
        kieContainer = kieService.getKieClasspathContainer();
        var marginalProbabilities = getMarginalProbabilities(knowledge.getPossibleWeapons());
        var conditionalResults = getConditionalProbabilities(
                knowledge.getPossibleWeapons(),
                marginalProbabilities,
                knowledge.getStatRelations()
        );

        List<String> nodes = new ArrayList<>();
        nodes.add(Weapon.Stat.SPEED_MOD.getName());
        nodes.add(Weapon.Stat.DAMAGE.getName());
        nodes.add(Weapon.Stat.RECHARGE_TIME.getName());
        nodes.add(Weapon.Stat.USES.getName());
        nodes.add(Weapon.Stat.RANGE.getName());

        List<String[]> edges = new ArrayList<>();
        edges.add(new String[]{Weapon.Stat.SPEED_MOD.getName(), Weapon.Stat.DAMAGE.getName()});
        edges.add(new String[]{Weapon.Stat.DAMAGE.getName(), Weapon.Stat.RECHARGE_TIME.getName()});
        edges.add(new String[]{Weapon.Stat.DAMAGE.getName(), Weapon.Stat.RANGE.getName()});
        edges.add(new String[]{Weapon.Stat.RECHARGE_TIME.getName(), Weapon.Stat.USES.getName()});

        manager.add_nodes(nodes);
        manager.add_edges(edges);


        var speedModEntries = marginalProbabilities.probabilities.get(Weapon.Stat.SPEED_MOD);
        double[][] speedModProbs = new double[speedModEntries.size()][1];
        for (int i = 0; i < speedModProbs.length; i++) {
            speedModProbs[i][0] = speedModEntries.get(i);
        }
        marginalProbabilities.probabilities.get(Weapon.Stat.SPEED_MOD);
        manager.add_cpd(Weapon.Stat.SPEED_MOD.getName(), speedModEntries.size(), speedModProbs, null, null);

        var damageGivenSpeedProbs = conditionalResults.cpds.get(Weapon.Stat.DAMAGE).get(Weapon.Stat.SPEED_MOD);
        manager.add_cpd(Weapon.Stat.DAMAGE.getName(), damageGivenSpeedProbs.length, damageGivenSpeedProbs, new String[]{Weapon.Stat.SPEED_MOD.getName()}, new int[]{damageGivenSpeedProbs[0].length});

        var rechargeTimeGivenDamage = conditionalResults.cpds.get(Weapon.Stat.RECHARGE_TIME).get(Weapon.Stat.DAMAGE);
        manager.add_cpd(Weapon.Stat.RECHARGE_TIME.getName(), rechargeTimeGivenDamage.length, rechargeTimeGivenDamage, new String[]{Weapon.Stat.DAMAGE.getName()}, new int[]{rechargeTimeGivenDamage[0].length});

        var rangeGivenDamage = conditionalResults.cpds.get(Weapon.Stat.RANGE).get(Weapon.Stat.DAMAGE);
        manager.add_cpd(Weapon.Stat.RANGE.getName(), rangeGivenDamage.length, rangeGivenDamage, new String[]{Weapon.Stat.DAMAGE.getName()}, new int[]{rangeGivenDamage[0].length});

        var usesGivenRechargeTime = conditionalResults.cpds.get(Weapon.Stat.USES).get(Weapon.Stat.RECHARGE_TIME);
        manager.add_cpd(Weapon.Stat.USES.getName(), usesGivenRechargeTime.length, usesGivenRechargeTime, new String[]{Weapon.Stat.RECHARGE_TIME.getName()}, new int[]{usesGivenRechargeTime[0].length});

        manager.finalize_model();

        // Set evidence and perform MAP query
        List<String[]> evidenceList = new ArrayList<>();
        evidenceList.add(new String[]{Weapon.Stat.USES.getName(), "3"});
        evidenceList.add(new String[]{Weapon.Stat.DAMAGE.getName(), "1"});

        Map<String, Integer> mapResult = manager.map_query(evidenceList);

        for (var entry : mapResult.entrySet()) {
            var statName = entry.getKey();
            var idx = entry.getValue();
            if (entry.getKey().equals("speed_mod")) {
                var value = marginalProbabilities.values.get(Weapon.Stat.fromName(statName)).get(idx);
                System.out.println("idx: " + idx + ", " + statName + ": " + value);
            } else {
                var value = conditionalResults.queryValues.get(Weapon.Stat.fromName(statName)).get(idx);
                System.out.println("idx: " + idx + ", " + statName + ": " + value);
            }
        }
        System.out.println();
    }

    @Override
    public void reason(GameState gameState) {
        knowledge.setPlayerHitBoxKnowledge(gameState.getPlayer().getHitBox(), true);
        KieSession kieSession = kieContainer.newKieSession("myKsession");
        kieSession.insert(gameState);
        kieSession.insert(gameState.getAgent());
        kieSession.insert(knowledge);
        kieSession.insert(possibilities);

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

    public interface PythonManager {
        void add_nodes(List<String> nodes);

        void add_edges(List<String[]> edges);

        void add_cpd(String variable, int variable_card, double[][] values, String[] evidence, int[] evidence_card);

        void finalize_model();

        Map<String, Integer> map_query(List<String[]> evidence);
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
}
