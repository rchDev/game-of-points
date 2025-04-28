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
1. The user sends chat messages to a conversational agent. 
2. The agent extracts intents and validates them through the game server's /info-validation endpoint. 
3. It determines which flow page is next and makes a transition. 
4. Upon transition, it validates the transition and checks if it has the session-end page. If it did, it sends:
   - Sends a page entry fulfillment response.
   - Sends a session-end signal.
5. Otherwise, only page/flow entry fulfillment response is sent.
6. If session hasn't ended, steps 1-5 are repeated.
7. When session ends, the front-end sends game creation request containing Dialogflow ID.
8. The game backend parses the request and looks up stored player answers that were collected during the chat.
9. The player's answers about weapon stats or its mood will be used during reasoning, when agent makes action decisions.
10. Another important calculation the server performs is **mood extraction**. The server sends user's mood description to a sentiment classifier service - a trained up neural net - to extract its mood class.
11. The mood and speed-damage combo are then stored in the player answers database.
12. Entries from this database in combination with weapon stache, are then used by the game server to construct a bayes net, which will also be used during reasoning.
13. Once the bayes net is constructed, a reasoning game agent is created.
14. Server then uses newly created agent for creating a session game state.
15. The server generates a unique session id and sends the game state and the generated ID to the front-end.
16. The front-end parses the response, renders the game state, and, using provided session ID, establishes a websocket connection with the game server.
17. Front-end is now ready to send player actions to the game server.

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
