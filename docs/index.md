---
layout: default
title: Home
---

# Game Of Points

A Simple game where human user competes against AI agent in a battle to collect the most points and survive.

#### *Disclaimer:*

*This project was created for educational purposes. Main goal was to explore and play with various technologies: including bayes net, neural net, rule engine libraries, and conversational agent tools.*

#### Rules:
1. Game starts with 60s timer, once the time runs out, whoever collected most points - wins. 
2. Points appear at random locations and time intervals. 
3. Both the agent and user start with a random weapon that affects stats like damage, speed, recharge time, and usage count.
4. Whoever kills one another - wins.

## Weird parts

This project was created for educational purposes.
It is in no way "production ready". 
There are a bunch of unhandled edge cases, bugs and quirky behaviours as well as creative, but objectively bad problem solutions.

## 📋 Environment setup

❗❗❗ Due to having many small services, the project is hard to launch.
To mitigate this issue, launch scripts have been created.
To use these scripts, you have to install optional dependencies. ❗❗❗

### Required:

#### 1. Working Google Conversational Agent (~~Dialogflow CX~~)

Inside project's root directory you'll find a file: **exported_agent_snitch.blob**

Take this file and import it into your own: <a href="https://conversational-agents.cloud.google.com/projects" target="_blank">Google conversational agent</a> project.

Overview of the process:
1. Create a new project.
2. Create new agent.
3. Restore (import) the agent to use the agent that's inside the repo file.
4. Set the webhook url to your own.
4. Publish the agent.
5. Use the provided: *project-id*, *agent-id* during npm install.

Detailed instructions can be found: <a href="" target="_blob">here</a>.

#### 2. *JDK* 17-22 installed.

❗Java 23 is not supported and will cause compatibility issues.

#### 3. Maven 3.2+ installed

Don't have to install maven. Run one of the commands:
1. ./mvnw shell command for Mac and Linux
2. mvnw.cmd for Windows

These commands will install the correct version of Maven package manager the first time they are run.

#### 4. Python >=3.12 installed
1. Install the version that is specified in <a href="https://github.com/rchDev/game-of-points/blob/main/.python-version" target="_blank">.python-version</a> file (use pyenv)
2. Install version that is complies with spec in <a href="https://github.com/rchDev/game-of-points/blob/main/pyproject.toml" target="_blank">pyproject.toml</a> file.

#### 5. Node.js >=v16.20.2 installed

### Optional:

#### 1. You have installed IntelliJ IDEA: <a href="https://www.jetbrains.com/idea/download/?section=mac" target="_blank">Mac</a>, <a href="https://www.jetbrains.com/idea/download/?section=linux" target="_blank">Linux</a>, <a href="https://www.jetbrains.com/idea/download/?section=windows" target="_blank">Windows</a>

Helpful for easily launching the project.

#### 2. You have installed <a href="https://python-poetry.org/docs/#installation" target="_blank">Poetry</a>

Although not required, but project uses poetry for easy dependency management.
You can use your own virtual environment and install dependencies from <a href="https://github.com/rchDev/game-of-points/blob/main/requirements.txt" target="_blank">requirements.txt</a> file.

#### 3. You have installed <a href="https://ngrok.com/docs/getting-started/" target="_blank">Ngrok</a>

Used for exposing your local game server to
Google's conversational agents' webhook,
without hosting the game server yourself.

## 🚀 How to launch this project

#### Project setup:

Inside game-of-points project's root directory run: 

```shell
poetry install
``` 

or activate virtual environment of your choice and run: 

```shell
pip install -r ./game-of-points-be/requirements.txt
```

Inside game-of-points/game-of-points-fe, run: 

```shell
npx cross-env PROJECT_ID=<conversational agents project id> AGENT_ID=<conversational agent id> npm install
```

**(Optional)** Inside game-of-points/game-of-points-be, run:

Mac or Linux:

```shell
./mvnw clean install
```

Windows:

```shell
mvnw.cmd clean install
```

**Full setup command to run inside project root:**

Mac or Linux:

```shell
poetry install && \
cd ./game-of-points-fe && \
npx cross-env PROJECT_ID=<conversational_agent_project_id> AGENT_ID=<conversational_agent_id> npm install && \
cd ../game-of-points-be && \
./mvnw clean install
```

Windows:

```shell
poetry install
cd game-of-points-fe
npx cross-env PROJECT_ID=<conversational_agent_project_id> AGENT_ID=<conversational_agent_id> npm install
cd ..\game-of-points-be
mvnw.cmd clean install
```

#### Launching the project:
Once you've set up the project, you can launch it in a few ways by following the steps described below.

#### Launching without IntelliJ:
All these steps should be performed from project's root directory.

**Mac or Linux:**

Run bayes-net:

```shell
cd ./game-of-points-be/src/main/java/io/rizvan/beans/actors/agent && \
poetry run python bayesian_network.py
```

Run sentiment classifier:

```shell
cd ./sentiment-analysis
poetry run python sentiment_classifier.py predict
```

Run game server:

```shell
cd ../../../../../../../../../ && \
./mvnw quarkus:dev
```

Run ngrok:

```shell
ngrok http --domain=<your_public_domain> 8080
```

Run game frontend:

```shell
cd ../game-of-points-fe && \
npm run dev
```

**Windows:**

#### Launching with IntelliJ:

#### General launch rules:
1. Game server depends on working python services (bayes-net and sentiment classifier).
2. Game frontend depends on a working game server
3. The Player data collection step in frontend chat depends on ngrok tunnel (if you are not hosting your backend on public ip address).
   If your conversational agent's webhook isn't pointing to your backend's public address, game server won't receive user questionnaire results.
## 🏗️ How system works (Top down view)
