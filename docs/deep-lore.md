---
layout: default
title: Deep Lore
nav_order: 3
has_children: true
permalink: /deep-lore/
---

# Deep lore

Delve deeper into the inner workings of a project

## Overview of what's going on:

**Questioning before a game match action sequence:**
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
    Dialogflow ->> GameServer: Send intent validation request
    GameServer ->> GameServer: Process and store info (linked to session)
    GameServer -->> Dialogflow: Send validation response

    %% Chat continues until end page
    Dialogflow -->> Frontend: Indicate chat complete

    %% Game creation
    Frontend ->> GameServer: Create game (with session ID)
    GameServer ->> GameServer: Read stored session data
    alt Player provided mood
        GameServer ->> Sentiment: Analyze mood text
        Sentiment -->> GameServer: Return mood class
    end

    %% Agent + Bayes Net creation
    GameServer ->> Bayes: add_nodes
    GameServer ->> Bayes: add_edges
    GameServer ->> Bayes: add_cpd_tables
    GameServer ->> Bayes: finalize_model

    GameServer ->> GameServer: Create agent and game state
    GameServer -->> Frontend: Return session ID + game state
    Frontend ->> Frontend: Render game view

```

**Gameplay and reasoning action sequence:**
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
        Server ->> Server: Call agent.reason(gameState)
        Server ->> BayesNet: Request most probable player stat combo
        BayesNet -->> Server: Return the most probable stat combo
        Server ->> Server: Finish reasoning and apply agent actions to game state
        Server ->> Frontend: Send updated game state
        Frontend ->> Frontend: Reconcile authoritative server state with local simulation
    end
```

## Testing links
```mermaid
graph TD
    Frontend["[Frontend](https://yourdomain.com/docs/frontend)"]
    GameServer["[Game Server](https://yourdomain.com/docs/server)"]
    Sentiment["[Sentiment Analysis](https://yourdomain.com/docs/sentiment)"]
    
    Frontend --> GameServer
    GameServer --> Sentiment
```