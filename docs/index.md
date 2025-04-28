---
layout: default
title: Home
nav_order: 0
---

# Game Of Points
{: .no_toc }

A Simple game where human user competes against AI agent in a battle to collect the most points and survive.

{: .note }
*This project was created for educational purposes. The ain goal was to explore and play around with various technologies: including bayes nets, neural nets, rule engines, and conversational agent tools.*

## Table of contents
{: .no_toc .text-delta }

- TOC
{:toc}

---

## Game Rules:
1. Game starts with 60s timer. Once the time runs out, whoever (agent or player) collected most points - wins. 
2. Points appear at random locations and time intervals. 
3. Both the agent and user start with a random weapon that affects stats like damage, speed, recharge time, and usage count.
4. Whoever kills one another - wins.

## 🌀 Weird parts

This project was created for educational purposes.
It is in no way "production ready".
There are a bunch of unhandled edge cases, bugs and quirky behaviours as well as creative, but objectively bad problem solutions.

Some rendering bugs:
1. The front-end to back-end game state reconciliation sometimes breaks and the game becomes laggy...
2. Game timer updates too slow - timer is tied to game update cycle when the server fails to process game state updates fast enough, timer updates lag too.
3. Error messages get rendered incorrectly in Mozilla browsers.

Ohh and the game has no end state...

## 📋 Environment setup

### Everyone:
{: .no_toc }

**Step 1:** Have Git version control installed.

**Step 2:** Install Git LFS (Large File Storage).

Install and setup instructions can be found here: ["Installing Git Large File Storage"](https://docs.github.com/en/repositories/working-with-files/managing-large-files/installing-git-large-file-storage).

{: .note}
Steps 3 and 4 are optional if you want a working conversational to question a player before the game starts.
The game will work without this step.

**Step 3 (Optional):** Have a working Google's Conversational Agent (~~Dialogflow CX~~)**
{: .no_toc }

{: .note }
[Here](/game-of-points/conv-agent-config/) you can find a full guide on how to set up a conversational agent for this project on Google's platform.

1. Inside project's root directory you'll find a file: **exported_agent_snitch.blob**
2. Take this file and import it into your own: [Google conversational agent](https://conversational-agents.cloud.google.com/projects) project.
3. Publish your agent and use provided: *project-id*, *agent-id* in:

```shell
npm run update-bot-ids -- --project-id=<project_id> --agent-id=<agent_id>
```

```shell
npm run update-bot-ids -- --project-id=<project_id> --agent-id=<agent_id>

# Should print success message similar to this one:
✔ Updated project‑id and agent‑id in index.html
```

**Step 4 (Optional):** Have ngrok installed and configured with your account

Used for exposing your local game server to
Google's conversational agent's webhook,
without hosting the game server yourself.

```shell
ngrok --version

# Should print version info similar to this: 
ngrok version 3.20.0
```

### ⭐ ⭐ ⭐ Docker specific (Recommended) ⭐ ⭐ ⭐
{: .no_toc }

**Step 1:** Have docker engine installed and accessible from your terminal

```shell
docker --version

# Should print version info similar to this: 
Docker version 28.0.4, build b8034c0
```

### ☠️ ☠️ ☠️ Non docker setup (Not recommended) ☠️ ☠️ ☠️
{: .no_toc }

This takes a lot of hassle to get up and working.

Tested on: macOS 15.4.1, Ubuntu 24.04, Windows WSL2 Ubuntu.

{: .warning}
Won't work on Windows 11 x64. **Reason:** 18 python packages used in a project didn't have wheels for Windows Python versions: 3.8-3.13. I didn't want to play around with versions, so I used Docker.

**Step 1:** Make sure *JDK* 17-22 is installed.

{: .warning}
Java 23 is not supported and will cause compatibility issues. Some maven packages won't build.

```shell
java --version 

# Should print version info similar to this one:
openjdk 17.0.14 2025-01-21 LTS
OpenJDK Runtime Environment Corretto-17.0.14.7.1 (build 17.0.14+7-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.14.7.1 (build 17.0.14+7-LTS, mixed mode, sharing)

# and

javac --version

#Should also print version info:
javac 17.0.14
```

**Step 2:** Have Python ==3.12 installed

Best bet is to have the version that is specified in [.python-version](https://github.com/rchDev/game-of-points/blob/main/bayes-net/.python-version) file (use pyenv)

```shell
python --version

# Should print a python version:
Python 3.12.9
```

**Step 3:** Install Node.js >=v16.20.2

```shell
node --version

# Should print node version that's >= 16.20.2:
v22.14.0
```

**Step 4 (Optional):** Install Maven 3.2+

Maven is not required, because Maven will be installed when running one of these scripts:
1. ./mvnw shell command on Mac or Linux
2. mvnw.cmd on Windows

**Step 5 (Optional):** Install IntelliJ IDEA: [macOS](https://www.jetbrains.com/idea/download/?section=mac), [Linux](https://www.jetbrains.com/idea/download/?section=linux), [Windows](https://www.jetbrains.com/idea/download/?section=windows).

Helpful for launching individual project modules and editing project files.

## ⚙️ Project setup

### ⭐ ⭐ ⭐ Docker specific (Recommended) ⭐ ⭐ ⭐
{: .no_toc }

**Step 1:** Inside game-of-points project's root run:

```shell
git lfs pull
```
This will download:
1. The player answers database file.
2. The sentiment analysis ML model.
3. Exported conversational agent file.

**Step 2 (Optional):** If you haven't already during the environment setup, connect the front-end application to the conversational agent.

```shell
cd ./game-of-points-fe && \
npm run update-bot-ids -- --project-id=<project_id> --agent-id=<agent_id>

# You should see a success message similar to this one:
✔ Updated project‑id and agent‑id in index.html
```

---

### ☠️☠️☠️ Non Docker (Not Recommended) ☠️☠️☠️
{: .no_toc }

**Step 1:** Inside game-of-points project's root run:
```shell
git lfs pull
```

This will download:
1. The player answers database file.
2. The sentiment analysis ML model.
3. Exported conversational agent file.


**Step 2:** If not already created, create a virtual environment of your choosing, using python version provided in this: [.python-version](https://github.com/rchDev/game-of-points/blob/main/sentiment-classifier/.python-version) file.

**Example with [venv](https://docs.python.org/3/library/venv.html) and [pyenv](https://github.com/pyenv/pyenv):**

Inside projects root run:

```shell
cd ./bayes-net && \
pyenv local && \
python -m venv venv/
```
**Step 3:** Activate virtual environment and install dependencies that are specified inside the requirements.txt file.

```shell
source .venv/bin/activate
pip install requirements.txt
```

**Step 4:** Change directories to /game-of-points/sentiment-classifier and create another virtual environment there.

**Example with [venv](https://docs.python.org/3/library/venv.html) and [pyenv](https://github.com/pyenv/pyenv):**

```shell
cd ../sentiment-classifier && \
pyenv local && \
python -m venv venv/
```

**Step 5:** Once again, activate the virtual environment and install dependencies that are specified inside the requirements.txt file, only this this it's inside /game-of-points/sentiment-classifier directory:

```shell
source .venv/bin/activate && \
pip install requirements.txt
```

**Step 6:** We need to set up the front-end. So we change into the /game-of-points/game-of-points-fe and run: ``npm install``.

Here, we change into front-end module's directory and run npm install to install all dependencies:
```shell
cd ../game-of-points-fe && \
npm install
```

**Step 7 (Optional):** If you haven't already during the environment setup, connect the front-end application to the conversational agent.

```shell
cd ./game-of-points-fe && \
npm run update-bot-ids -- --project-id=<project_id> --agent-id=<agent_id>

# You should see a success message similar to this one:
✔ Updated project‑id and agent‑id in index.html
```

## 🚀 Launching the project

Once you've set up the environment and the project, you can launch the whole application in a few ways, by following instructions bellow.

### ⭐ ⭐ ⭐️ Docker specific launch (Recommended) ⭐ ⭐ ⭐
{: .no_toc }

**Step 1 (Optional):** Run ngrok, if you want your local game back-end communicating with the Google's conversational agent.

In a terminal session run:
```shell
ngrok http --domain=<your_public_domain> 8080
```

**Step 2:** Run the [docker-compose.yml]() file.

Inside project's root, open a new terminal session (while keeping the ngrok session alive) and run:
```shell
docker compose up

# or for detached version

docker compose up -d

# or if you want to fully rebuild all containers from their images

docker compose up --build
```

After this you can open a front-end at: `` http://localhost:5173 ``

### ☠️ ☠️ ☠️ Non Docker Launch ☠️ ☠️ ☠️
{: .no_toc }

{: .note }
Works only on: macOS, Linux and Windows WSL.

---

### Easy mode:
{: .no_toc }

**Step 1 (Optional):** Run ngrok, if you want your local game back-end communicating with the Google's conversational agent.

In a terminal session run:
```shell
ngrok http --domain=<your_public_domain> 8080
```

**Step 2:** Run [run-all.sh](https://github.com/rchDev/game-of-points/blob/main/run-all.sh) script.

In another terminal session, run:

```shell
bash run-all.sh

# You should see a bunch of similar messages.

⏳ Launching bayes-net and sentiment classifier services...
Connection to localhost port 25334 [tcp/*] succeeded!
Connection to localhost port 25336 [tcp/*] succeeded!
✅ Bayes-net and sentiment classifier are ready!
⏳ Launching game backend...
Connection to localhost port 8080 [tcp/http-alt] succeeded!
✅ Game backend is ready.
⏳ Launching game frontend...

> game-of-points-fe@0.0.0 dev
> vite


  VITE v6.2.6  ready in 49 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help
```

---

### Hardcore mode:
{: .no_toc }

{: note }
Use these steps only if you want to run a specific part of the project, or if `` run-all.sh `` and Docker approaches are not working.

All these steps should be performed from project's root directory.

**Step 1:** Run bayes-net.

{: note }
Example shown with [venv](https://docs.python.org/3/library/venv.html), but you can use any virtual environment, or none (good luck with that).

```shell
cd ./bayes-net && \
source .venv/bin/activate && \
python bayesian_network.py
```

**Step 2:** Run sentiment classifier.

Open a new terminal session in project's root, don't kill the previous where bayes-net is running.

{: .note }
Example shown with [venv](https://docs.python.org/3/library/venv.html), but you can use any virtual environment, or none (good luck with that).

```shell
cd ./sentiment-classifier && \
source .venv/bin/activate && \
python sentiment_classifier.py predict
```

**Step 3:** Run game server.

In a new terminal session (keep others alive):

```shell
cd ./game-of-points-be && \
./mvnw quarkus:dev
```

**Step 4 (Optional):** Run ngrok, if you want your local game back-end communicating with the Google's conversational agent.

In a new terminal session (keep others alive):

```shell
ngrok http --domain=<your_public_domain> 8080
```

**Step 5:** Run game front-end.

In a new terminal session (keep others alive):

```shell
cd ../game-of-points-fe && \
npm run dev
```

**General launch rules:**
{: .no_toc }

1. Game server depends on working python services (bayes-net and sentiment classifier).
2. Game front-end depends on a working game server
3. The Player data collection step in front-end chat depends on ngrok tunnel (if you are not hosting your back-end on public ip address).
   If your conversational agent's webhook isn't pointing to your back-end's public address, game server won't receive user questionnaire results.

## 🏗️ System overview

### Main components:
{: .no_toc }

{: .info }
More info can be found in [deep lore](/game-of-points/deep-lore/) section of the docs.

1. Game [front-end](https://github.com/rchDev/game-of-points/tree/main/game-of-points-fe) application.
2. [Game server](https://github.com/rchDev/game-of-points/tree/main/game-of-points-be).
3. [Bayesian network](https://github.com/rchDev/game-of-points/tree/main/bayes-net) server.
4. [Sentiment classifier](https://github.com/rchDev/game-of-points/tree/main/sentiment-classifier) server.
5. Google's [conversational agent](https://conversational-agents.cloud.google.com/projects).
6. [Ngrok](https://ngrok.com/) as a tunnel service connecting Google's conversational agent to local game server.

```mermaid
graph TD
  A[Game Front-end Application]
  B[Game Server]
  C[Bayesian Network Server]
  D[Sentiment Classifier Server]
  E[Google's Conversational Agent]
  F[Ngrok Tunnel Service]

  A -->|"Player Actions"| B
  A -->|"Chat message"| E
  E -->|"Response message"| A
  B --> |"Authoritative Game State"| A
  B -->|"MAP query request"| C
  C -->|"Most probable stat combo"| B
  E -->|"Validation Requests"| F
  F -->|"Forwarded Validation Responses"| E
  F -->|"Forwarded Validation Requests"| B
  B -->|"Validation Responses"| F
  B -->|"Player Mood Description"| D
  D -->|"Mood Class"| B

  style A fill:#90EE90,stroke:#333,stroke-width:2px
  style B fill:#87CEFA,stroke:#333,stroke-width:2px
  style C fill:#FFFFE0,stroke:#333,stroke-width:2px
  style D fill:#FFFFE0,stroke:#333,stroke-width:2px
  style E fill:pink,stroke:#333,stroke-width:2px
  style F fill:#D3D3D3,stroke:#333,stroke-width:2px
```

### What is going on:
{: .no_toc }

{: .info }
<a href="https://www.gabrielgambetta.com/client-server-game-architecture.html" target="_blank">Article that really helped me to implement fast-paced multiplayer client-server communication</a>

{: .info }
More info can be found in [deep lore](/game-of-points/deep-lore/) section of the docs.

1. Game session initialization involving questioning by the conversational agent. 
2. Game front-end sends a bunch of game state updates to game server through a websocket connection (i know... tcp is bad for game dev.)
3. While the game server is processing these updates, front-end app simulates the application of these updates to create an illusion of smooth gameplay experience for a user. 
4. For each game session, game server stores game updates inside a <a href="https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/SessionStorage.java" target="_blank">session storage</a>. 
5. Once the time for processing comes, server runs the loop through all sessions and starts applying updates for each of game states. See this <a href="https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/GameStateUpdateScheduler.java" target="_blank">code</a>. 
6. Updating starts with cloning a game state, getting all player actions from the session storage and validating them. 
7. Once actions are deemed valid, they are applied to the game state clone and are registered as **facts** for AI agent. (<a href="https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/GameState.java">See this place</a>.)
8. Once all facts are registered, [agent.reason](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/DroolsBrain.java#L271-L307) method which then uses Drools rule engine and Bayesian network to reason about the current game state, and make action choice decisions based on the current state configuration. 
9. After agent takes these actions, they are applied to the game state clone. 
10. Clone is then placed into game state update history inside session storage.
11. For each session update event is published.
12. [Controller](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/StartWebSocket.java#L100-L118) that's listening for those update events, sends updated game states to each session (front-end).
13. Front-end reconciles it's predicted game state with authoritative game state that's provided by back-end. ([reconcileWithServerState](https://github.com/rchDev/game-of-points/blob/main/game-of-points-fe/main.js#L341-L354))