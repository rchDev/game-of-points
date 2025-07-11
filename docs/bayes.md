---
layout: default
title: Bayesian Network
nav_order: 3
parent: Lore
permalink: /bayes/
---


# **Bayesian network**
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

- TOC
{:toc}

---

Drools rule engine gains information about player's stats from facts, but not all info can be known right away, at the start of a game.
Most of the time, rules engine has only partial information about the player stats,
while the agent's capability assessment process heavily depends on having complete information.
The Solution to this partial information problem is **Bayesian network**.

Known stats are presented as **evidence** and unknown ones are given as **query** variables.

## Usage

The main use for a Bayes net in my application is to: **get the most probable combination of player stats.**

I'm using the Bayes net inside one of the inference layer Drools rules. What this rule does, is query the Bayes net to get the most probable stat combination and uses received values to update [KnowledgeItems](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/knowledge/KnowledgeItem.java) inside the [AgentKnowledge](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/knowledge/AgentKnowledge.java) class.
Each AgentKnowledgeItem inside AgentKnowledge has a value field and and a boolean field, indicating if that value is known or approximated. Items become known, only when they are experienced by the agent during the gameplay.
Items that have been set by the Bayes net, have their values set, but are not considered unknown. 
This means that values derived from experience take precedence over values that were derived from the Bayes net.

When querying the Bayes net, known values from the AgentKnowledge get set as a list of **evidence variables** and unknown values are converted into a list of **query variables**.
These lists, containing query and evidence values, are then passed into a `` bayesNetwork.map_query(query, evidence); `` call.

**What's really going on:**

Inside a rule, updateKnowledge callable gets called with a query and an evidence list.
```java
rule "player-stat-inference"
agenda-group "inference-group"
salience -100
    when
        $knowledge : AgentKnowledge()
        $getQueryList : GetQueriesCallable()
        $getEvidenceList : GetEvidenceCallable()
        $updateKnowledge: UpdateKnowledgeCallable()
    then
        String[] queries = (String[]) $getQueryList.call();
        String[][] evidence = (String[][]) $getEvidenceList.call();

        $updateKnowledge.setParameters(queries, evidence);
        $updateKnowledge.call();
        System.out.println("inference-group: player-stat-inference-ran");
end
```

UpdateKnowledgeCallable then calls updateKnowledge() function with the two lists.
```java
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
```

Inside updateKnowledge() function, a Bayesian network on the Python side is called. This call returns a hash map of key value pairs, where the key is player stat name e.g. damage, reload speed... and the value is a weapon stat value index.
Then the function loops over every entry inside a map and uses the received name and the value index to derive the actual stat value. Then it uses that value to set the value field of a corresponding knowledge item, inside the AgentKnowledge object.
```java
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
            // WAY MORE CASES BELLOW
        }
        // WAY MORE LOGIC BELLOW
    }
}
```

On the python side, query variable, which is of JavaList type, is converted to a python list. Evidence variable - into python dictionary. Once the conversion is done, self.infer.map_query(variables=query, evicence=evidence_dict) is called. Then the map_query result is converted to a JavaMap python class and returned.

{: .note }
`` self.infer = VariableElimination(self.model) `` - a Bayesian network querying class provided by pgmpy library. It uses variable elimination technique to query the Bayes net. I could have used some of the sampling techniques, that allow exchanging compunational efficiency for accuracy, but my network is small enough, that I can still get the benefits of using the exact querying technique such as a variable elimination.

```python
def map_query(self, query, evidence):
    try:
        queryList = list(query)
        evidence_dict = {entry[0]: int(entry[1]) for entry in list(evidence)}
        result = self.infer.map_query(variables=queryList, evidence=evidence_dict)
    java_map = MapConverter().convert(result, self.gateway._gateway_client)
    return java_map
except Exception as e:
    logger.error(f"Error performing MAP query: {e}")
    raise

```

### MAP (Maximum A Posteriori) query
{: .no_toc }

$$
\operatorname{MAP}(Q \mid E = e) = \arg\max_{q} \sum_{z} P(Q = q, Z = z, E = e)
$$

To get the the most probable stat combination, I'm using MAP query, which is, essentially, an argmax query over every possible variable combination, given the evidence variables. I don't use hidden variables, because I care about each value.

## Used libraries

[pgmpy (Probabilistic Graphical Models using Python)](https://pgmpy.org/) - Python library designed for working with probabilistic graphical models (PGMs) such as Bayesian Networks and Markov Networks.

[py4j (A Bridge between Python and Java)](https://www.py4j.org/) - Py4J enables Python programs running in a Python interpreter to dynamically access Java objects in a Java Virtual Machine.

**pgmpy** was used for creating the actual bayes network in python [seen here](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/bayesian_network.py),
but the game server, where this Bayes net will be used, is written in Java. So i used **py4j** library to translate and the exchange JVM objects with Python interpreter.

For **py4j** to work, it needs a Gateway server on the Python side, that runs on some port and listens on another,
then on Java side you instantiate a Gateway server which, at the point of calling:

```java
bayesGatewayServer.getPythonServerEntryPoint(
    new Class[]{ BayesPythonManager.class }
);
```

creates a connection between two servers.

{: .note }
The Python-side server must be running before the Java-side one, because the Java-side initiates the connection. Since the Java Game server starts connecting with two Python servers during its startup, Python servers must be up and running before the Java game server starts.

```java
@Singleton
@Startup
public class PythonGateway {
    private GatewayServer bayesGatewayServer;
    private GatewayServer sentimentGatewayServer;
    private BayesPythonManager bayesManager;
    private SentimentPythonManager sentimentPythonManager;

    @PostConstruct
    public void init() {

        bayesGatewayServer = new GatewayServer(
                null,                    // entryPoint
                25333,                   // port
                25334,                   // pythonPort
                GatewayServer.defaultAddress(),  // address
                GatewayServer.defaultAddress(),  // pythonAddress
                GatewayServer.DEFAULT_CONNECT_TIMEOUT,  // connectTimeout
                GatewayServer.DEFAULT_READ_TIMEOUT,     // readTimeout
                null                      // customCommands
        );
        bayesGatewayServer.start();

        // Initialize the Sentiment Gateway Server with its own Python and callback ports
        sentimentGatewayServer = new GatewayServer(
                null,
                25335,
                25336,
                GatewayServer.defaultAddress(),  // address
                GatewayServer.defaultAddress(),  // pythonAddress
                GatewayServer.DEFAULT_CONNECT_TIMEOUT,  // connectTimeout
                GatewayServer.DEFAULT_READ_TIMEOUT,
                null
        );
        sentimentGatewayServer.start();

        bayesManager = (BayesPythonManager) bayesGatewayServer.getPythonServerEntryPoint(new Class[]{BayesPythonManager.class});
        sentimentPythonManager = (SentimentPythonManager) sentimentGatewayServer.getPythonServerEntryPoint(new Class[]{SentimentPythonManager.class});
    }

    public BayesPythonManager getBayesNetwork() {
        return bayesManager;
    }

    public SentimentPythonManager getSentimentAnalyser() {
        return sentimentPythonManager;
    }
}
```

**What is BayesPythonManager?**
It's an interface that defines a contract which is implemented on the Python side. Concretely, it defines Bayesian network methods that are implemented inside [bayesian_network.py](https://github.com/rchDev/game-of-points/blob/main/bayes-net/bayesian_network.py) script and will be used inside the Java game server. 
Under the hood, this interface is probably used to inform **py4j's** serializers and mappers about implemented: function signatures, data types, variable names an so on.
```java
...
public interface BayesPythonManager {
    void add_nodes(List<String> nodes);

    void add_edges(List<String[]> edges);

    void add_cpd(String variable, int variable_card, double[][] values, String[] evidence, int[] evidence_card);

    void finalize_model();

    Map<String, Integer> map_query(String[] query, String[][] evidence);
}
```

You pass it to a gateway server on the Java side and get an object, which contains all those methods that you can then use to call the actual Bayes-net that lives on the Python side.
```java
public class PythonGateway {
    ...
    @PostConstruct
    public void init() {
        ...
        bayesManager = (BayesPythonManager) bayesGatewayServer.getPythonServerEntryPoint(new Class[]{BayesPythonManager.class});
        ...
    }
    ...
}
```

An example usage of a BayesPythonManager:
```java
public class DroolsBrain implements AgentsBrain {
    // ...
    private final BayesPythonManager bayesNetwork;
    // ...
    public DroolsBrain(
        PythonGateway pythonGateway,
        Optional<PlayerAnswers> playerAnswers,
        List<WeaponEntity> weaponMoodOccurrences
    ) {
        // ...
        bayesNetwork = pythonGateway.getBayesNetwork();
        // ...
        bayesNetwork.add-nodes(nodes);
        // ...
    }
}
```

A Python method that was called from the Java side:
```python
class BayesianNetworkManager:
    ...
    def add_nodes(self, nodes):
        """
        Funkcija prideda nauja atsitiktini kintamaji i Bajeso tinkla
        :param nodes:
        :return:
        """
        try:
            nodes = list(nodes.toArray())
            logger.info(f"Adding nodes: {nodes}")
            self.model.add_nodes_from(nodes)
            logger.info("Nodes added successfully.")
        except Exception as e:
            logger.error(f"Error adding nodes: {e}")
            raise
    ...
```
## Construction

Now that we have Python objects available to us on the Java, we can use methods:
1. add_nodes
2. add_edges
3. add_cpd
4. finalize_model
to construct the Bayes-net.

Bayesian network gets created inside DroolsBrain class,
during agent's creation, when a game session gets initialized.

Network is created by using weapon cache data and player answers database, which consists of weapon-mood combinations.

There are two version of a network: the one with a mood node and the one without it.
Which version get created depends on multiple factors, such as:
1. Did the system receive the player mood answer from the conversational agent's session during the game session initialization phase:
    - yes - check the other condition,
    - no - create bayes net without mood variable.
2. Does the player answers database contain all the possible damage and speed values.
   It is important that it does, because mood variable depends on speed and damage variables,
   so you cannot connect mood with speed and damage if some of the value combinations are missing.
    - yes - create a network with a mood variable.
    - no - create a network without a mood variable.

### Version 1 (with mood)
{: .no_toc }

```mermaid
flowchart TD
    SpeedMod["speed_mod"] --> Damage["damage"]
    Damage --> Ammo["ammo"]
    Ammo --> RechargeTime["recharge_time"]
    Damage --> Range["range"]
    SpeedMod --> Mood["mood"]
    Damage --> Mood
```

### Version 2 (without mood)
{: .no_toc }

```mermaid
flowchart TD
    SpeedMod["speed_mod"] --> Damage["damage"]
    Damage --> Ammo["ammo"]
    Ammo --> RechargeTime["recharge_time"]
    Damage --> Range["range"]
```

### Construction process:
{: .no_toc }
1. Go through the whole weapon list, **count** how many times each value of every weapon stat showed up.
2. Divide stat counts by the total weapon count, to get **marginal stat probabilities**.
3. If the stat is fully independent, we already have all the info we need.
4. Otherwise, when the weapon stat is only conditionally independent while knowing some other stat, we count how many times did the query-evidence combo occur and divide the occurence count by the total number of weapons.
This way we get joint probabilities: P(query, evidence). To get the P(query | evidence), we divide joint probabilities by the marginal probability of evidence: P(evidence).
   At the end of this we get CPD table in a form of matrix, that we, then feed into the **add_cpd** function. Function with the calculations can be found here: [getConditionalProbabilities()](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/DroolsBrain.java#L358-L411).
5. Once the probability calculations are done, all nodes are added to the network by calling bayesNetwork.add_nodes() and passing it the nodes list.
6. After adding the nodes, we connect all the nodes by calling bayesNetwork.add_edges() and passing it a list of string arrays, each containing related nodes.
7. Once all the nodes are added, a function [addMoodNode](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/DroolsBrain.java#L107-L199) is called, which conditionally adds mood node only if the condition: all the speed and damage values have showed up at least once, is fulfilled.
8. 

