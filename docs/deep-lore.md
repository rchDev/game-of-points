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
    participant Front-end
    participant Dialogflow as Dialogflow Server
    participant GameServer as Game Server
    participant Sentiment as Sentiment Analysis Server
    participant Bayes as Bayes Network Server

    %% Chat interaction
    loop until session end
        Player ->> Front-end: Send chat message
        Front-end ->> Dialogflow: Forward message
        Dialogflow ->> Dialogflow: Figure out the intent
        alt Intent needs validation
            Dialogflow ->> GameServer: Send intent validation request
            GameServer ->> GameServer: Process and store info (linked to session)
            GameServer -->> Dialogflow: Send validation response
        end
        Dialogflow ->> Dialogflow: Transition to other page
        
        %% Chat continues until end page
        alt End Session page reached
            Dialogflow -->> Front-end: Send the end message
            Dialogflow -->> Front-end: Indicate session ending
        else otherwise
            Dialogflow -->> Front-end: Send page entry fulfillment response
        end
    end
    %% Game creation
    Front-end ->> GameServer: send game creation request (with Dialogflow ID)
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
    GameServer -->> Front-end: Return session ID + game state
    Front-end -->> Player: Render game view
    Front-end ->> GameServer: upgrade to web socket
    GameServer -->> Front-end: upgraded connection
    Front-end ->> GameServer: Ready to send player actions
```

### What's going on here:
1. User sends chat messages to a conversational agent.
2. Agent extracts intents, validates them through the game servers /info-validation endpoint.
3. Determines which flow page is the next and makes a transition.
4. On transition validated the transition and checks if its the session-end page. In the case that it is:
   - sends page entry fulfillment response.
   - session end signal.
5. Otherwise, only page/flow entry fulfillment response is sent.
6. If session didn't end steps 1-5 are repeated.
7. When session ends, front-end sends game creation request containing Dialogflow ID.
8. The game backend parses the request and looks up stored player answers from the chat.
9. Player's answers about weapon stats or its mood will be used during reasoning, when agent makes action decisions.
10. Another important calculation the server performs is **mood extraction**. The server sends users mood description to a sentiment analyzer to extract its mood class.
11. The mood and speed-damage combo is stored into player answers database.
12. Entries from this database with combination of weapon stache, are then used by the game server to construct a bayes net, which will also be used during reasoning.
13. Once the bayes net is constructed, reasoning game agent gets created.
14. Server then uses newly created agent for creating a session game state.
15. Then the server generates unique session id and sends the game state and the generated id to the front-end.
16. Front-end parses the response, renders game state data and using provided session id establishes a websocket connection with game server.
17. Front-end is ready to send player actions.

## Overview of gameplay and reasoning sequence
{: .no_toc }

```mermaid
sequenceDiagram
    participant User
    participant Front-end
    participant Server
    participant BayesNet as Bayes Network Server
    
    %% === Continuous Action Flow ===
    User ->> Front-end: Interact with game (e.g., move, click)
    Front-end ->> Server: Send player actions
    note right of Front-end: Simulates action locally
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
        Server -->> Front-end: Send updated game state
        Front-end ->> Front-end: Reconcile authoritative server state with local simulation
    end
```

### What's going on here:
