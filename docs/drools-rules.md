---
layout: default
title: Drools Rules
nav_order: 2
parent: Deep Lore
permalink: /drools/
---

# Drools rules


Based on their functional responsibility, rules are divided into four distinct groups:

```mermaid
---
title: Diagram showing incomplete rule sets in each layer
---
flowchart TD
subgraph inference-group
    player-moved
    player-aimed
    player-hp-changed
    player-stat-inference
    resource-point-changed
end
subgraph possibilities-group
    can-agent-kill-player
    is-agent-slower
    agent-can-reach-player
    player-can-reach-agent
end
subgraph agent-choices-group
    agent-kill-player
    agent-avoid-player
    agent-safe-collect
    agent-aggresive-collect
end
subgraph agent-actions-group
    agent-chose-kill-can-reach-player
    agent-collect-cannot-reach-aggresive
    agent-collect-cannot-reach-safe
    agent-chose-avoid
end

inference-group --> possibilities-group
possibilities-group --> agent-choices-group
agent-choices-group --> agent-actions-group

```
### [Inference rules](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/resources/drools/fact_rules.drl)

```mermaid
flowchart TD
subgraph inference-group
    player-moved
    player-aimed
    player-shot
    player-collected
    player-hp-changed
    player-stat-inference
    resource-point-changed
    game-time-changed
end
```

These rules fire on inserted facts and update agent's knowledge base.

**Example:** a fact about player using their weapon was inserted into kieSession
and the damage was felt by an agent, a rule: ["Player shot"](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/resources/drools/fact_rules.drl)
will fire and update [agent's knowledge base](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/knowledge/AgentKnowledge.java).

### [Possibilities rules](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/resources/drools/possibilities_rules.drl)

Once inference rules have updated the knowledge base, possibilities group is ran.
These rules fire, based on variable relationships inside knowledge base and agent classes.

These rules are really simple, they just set variables inside AgentPossibilities class which you can se bellow:
```java
public class AgentPossibilities {
    private boolean canOneShootPlayer;
    private boolean oneShotByPlayer;
    private boolean fasterThanPlayer;
    private boolean slowerThanPlayer;
    private boolean canReachPlayer;
    private boolean reachedByPlayer;

    private boolean canKillPlayer;
    private boolean killedByPlayer;

    private boolean canWinByPointCollection;
    ...
}
```

This class represents agent's relationship with player.

### [Agent choices rules](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/resources/drools/behavioural_rules.drl)

Now that we have agent's possibilities, that were inserted by the previous layer rules.
We know what agent can and cannot do. We have to make a strategy choice. That is what this layer is responsible for.
This group basically implements the decision tree structure seen below and
produces strategy choice for an agent. Possible strategies include:
1. Avoid player.
2. Collect points - safely (while avoiding player).
3. Collect points - aggressively (while ignoring player).
4. Kill player.


```mermaid
flowchart TD
    A["Can agent kill player?"] -->|yes| B["Can player kill agent?"]
    A -->|no| C["Can player kill agent?"]

    B -->|yes| D["Game time > 50%"]
    B -->|no| E[kill]

    D -->|yes| F["Is player close?"]
    D -->|no| G["Game time <= 50% and > 15%"]

    F -->|yes| H[avoid]
    F -->|no| I[safe-collect]

    G -->|yes| J["Is it worth collecting points?"]
    G -->|no| K["Game time <= 15%"]

    J -->|yes| L["Is player close?"]
    J -->|no| M["Can player one shoot agent?"]

    L -->|yes| N[avoid]
    L -->|no| O[safe-collect]

    M -->|yes| P["Is player close?"]
    M -->|no| Q["Can agent one shoot player?"]

    P -->|yes| R[avoid]
    P -->|no| S[safe-collect]

    Q -->|yes| T[kill]
    Q -->|no| U["Did player shoot at you?"]

    U -->|yes| V[kill]
    U -->|no| W[aggressive-collect]

    K -->|yes| X["Is it worth collecting points?"]
    K -->|no| Y[kill]

    X -->|yes| Z["Is player close?"]
    X -->|no| AA[kill]

    Z -->|yes| AB[avoid]
    Z -->|no| AC[safe-collect]

    C -->|yes| AD["Is player close?"]
    C -->|no| AE[aggressive-collect]

    AD -->|yes| AF[avoid]
    AD -->|no| AG[safe-collect]
```
### [Agent Action Rules](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/resources/drools/agent_action_rules.drl)
{: .no_toc }

Once the previous layer rules have successfully run and inserted agent's strategy choice, this layer runs.

Rules in this layer are responsible for evaluating the current environment and picking the most appropriate action that contributes to current strategy.

For example: **agent chose to kill player**,
because the player is really effective at collecting points and
there is no way the agent will outpace him.
In this case a combination of rules will fire that determine if a player is within agents reach,
in that case - **attack**, otherwise - **move** in player's direction.
