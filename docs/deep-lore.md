---
layout: default
title: Deep Lore
nav_order: 3
has_children: true
permalink: /deep-lore/
---

# Agent Reasoning
{: .no_toc }

An overview of how the agent makes decisions.

## Table of contents
{: .no_toc .text-delta }

- TOC
{:toc}

---

## Overview

### Reasoning participants
{: .no-toc}

- **GameState** - a class containing variables which hold information about game environment. Also has methods for validating and applying **PlayerActions** and **AgentActions**, also stores information about **Player** and **Agent** (positions, hp, speed, weapon uses count...)
- **AgentPossibilities** - (add later.)
- **AgentChoice** - (add later.)
- **AgentAction** - interface that defines **apply(GameState) -> void** and **getType() -> ActionType** methods. Concrete implementations of this class have various GameState altering effects that spring into action when **apply** method is called.
- **PlayerAction** - (add later.)
- **Facts** - validated, applied and registered player actions, that happened since the last **agent.reason()** method call.
- **AgentKnowledge** - Agent doesn't have direct access to **GameState**. So this class acts as a collection of gathered truths about the game environment during the course of game and is used in agents decision making.
- **AgentsBrain** - interface which defines a contract for creating various types of agents. Its centerpoint is a **reason** method, which takes in a **GameState** as an argument and applies changes to it.
- **DroolsBrain** - concrete implementation of **AgentsBrain** which uses Drools rule engine and a Bayes net to make gameplay decisions.

### A Journey from player action to agent action
{: .no_toc }

```mermaid
block-beta
   columns 5
   A["PlayerAction"] space B["Fact"] space C["AgentKnowledge"]
   space space space space space
   F["AgentAction"] space E["AgentChoice"] space D["AgentPossibilities"]

   A --> B
   B --> C
   C --> D
   D --> E
   E --> F
```

## Reasoning process

The reasoning starts once the **GameUpdateScheduler** updates the game state, 
inserts facts into a facts storage. After that the agent's **reason()** method gets called.

Agents reason method calls Drools brain's reason method. Which looks pretty simple:
We essentially insert a bunch of items that will be used by [Drools rules](https://github.com/rchDev/game-of-points/tree/main/game-of-points-be/src/main/resources/drools) into a stateless Drools session which is called **KieSession**.
```java
    @Override
    public void reason(GameState gameState) {
        knowledge.setPlayerHitBoxKnowledge(gameState.getPlayer().getHitBox(), true);
        
        KieSession kieSession = kieContainer.newKieSession("myKsession");
        kieSession.insert(gameState);                     // GameState object
        kieSession.insert(gameState.getAgent());
        kieSession.insert(knowledge);                     // Current AgentKnowledge
        kieSession.insert(bayesNetwork);                  // BayesNet
        kieSession.insert(marginals);                     // Marginal player stat value probabilities
        kieSession.insert(conditionals);                  // Conditional player stat value probabilities
        kieSession.insert(foundMoods);                    // Moods that were seen in PlayerAnswer database
        kieSession.insert(possibilities);                 // Agent's current possibilities 
        
        // Function callback for getting a list of query variables for a Bayes net query.
        kieSession.insert(new GetQueriesCallable());
        // Function callback for getting a list of evidence variables for a Bayes net query.
        kieSession.insert(new GetEvidenceCallable());
        // Function callback for updating agent's knowledge from a drools rule.
        kieSession.insert(new UpdateKnowledgeCallable());

        try {
            // insert all the player action facts
            gameState.getFacts().forEach(kieSession::insert);
            gameState.clearFacts();
            
            // run inference group rules to update AgentKnowledge
            kieSession.getAgenda().getAgendaGroup("inference-group").setFocus();
            kieSession.fireAllRules();

            // run possibilities group rules to update AgentPossibilities
            kieSession.getAgenda().getAgendaGroup("possibilities-group").setFocus();
            kieSession.fireAllRules();

            // run agent choices group to make an agents strategy choice
            kieSession.getAgenda().getAgendaGroup("agent-choices-group").setFocus();
            kieSession.fireAllRules();
            
            // run agent actions to select best action for currently selected strategy.
            kieSession.getAgenda().getAgendaGroup("agent-actions-group").setFocus();
            kieSession.fireAllRules();

            System.out.println("--------------------------");
        } finally {
            kieSession.dispose();
        }
    }
```
## Drools Rules

## Bayesian network