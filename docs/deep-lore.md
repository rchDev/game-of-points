---
layout: default
title: Deep Lore
nav_order: 3
has_children: true
permalink: /deep-lore/
---

# Deep lore

Delve deeper into the inner workings of a project

## Overview of game initialization sequence:
{: .no_toc }

```mermaid
sequenceDiagram
    participant Player
    participant Frontend
    participant Dialogflow as Dialogflow Server
    participant GameServer as Game Server
    participant Sentiment as Sentiment Analysis Server
    participant Bayes as Bayes Network Server

    %% Chat interaction
    Player ->> Frontend: Send chat message
    Frontend ->> Dialogflow: Forward message
    Dialogflow ->> Dialogflow: Figure out the intent
    alt Intent needs validation
        Dialogflow ->> GameServer: Send intent validation request
        GameServer ->> GameServer: Process and store info (linked to session)
        GameServer -->> Dialogflow: Send validation response
    
    Dialogflow ->> Dialogflow: Transition to other page
    
    %% Chat continues until end page
    alt End Session page reached
        Dialogflow -->> Frontend: Send the end message
        Dialogflow -->> Indicate session ending
    else
        Dialogflow -->> Send page entry fulfillment response
    end
    
    %% Game creation
    Frontend ->> GameServer: send game creation request (with Dialogflow ID)
    GameServer ->> GameServer: Read stored player answers
    alt Player provided mood description
        GameServer ->> Sentiment: Analyze mood text
        Sentiment -->> GameServer: Return mood class
    end
    
    GameServer ->> GameServer: Calculate probabilities and create CPDs

    %% Agent + Bayes Net creation
    GameServer ->> Bayes: call add_nodes
    GameServer ->> Bayes: call add_edges
    GameServer ->> Bayes: call add_cpds
    GameServer ->> Bayes: call finalize_model

    GameServer ->> GameServer: Create agent and game state
    GameServer -->> Frontend: Return session ID + game state
    Frontend ->> Frontend: Render game view

```
### What's going on here:
User types a greeting message into a chat

## Overview of gameplay and reasoning sequence
{: .no_toc }

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant Server
    participant BayesNet as Bayes Network Server
    
    %% === Continuous Action Flow ===
    User ->> Frontend: Interact with game (e.g., move, click)
    Frontend ->> Server: Send player actions
    note right of Frontend: Simulates action locally
    Server ->> Server: Store player actions for processing
    note right of Server: Action queuing runs concurrently
    
    %% === Periodic Server Tick Loop ===
    loop Every 20ms (server tick)
        Server ->> Server: Validate and apply player actions
        Server ->> Server: Store player actions as facts
        Server ->> Server: Call agent.reason(gameState)
        Server ->> BayesNet: Request most probable player stat combo
        BayesNet -->> Server: Return the most probable stat combo
        Server ->> Server: Finish reasoning and apply agent actions to game state
        Server -->> Frontend: Send updated game state
        Frontend ->> Frontend: Reconcile authoritative server state with local simulation
    end
```

### What's going on here:
