---
layout: default
title: Home
---

# Game Of Points
Welcome to the docs! Here’s what this project is about.
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

#### 2. *JDK* 17-22 installed.

❗Java 23 is not supported and will cause compatibility issues.

#### 3. Maven 3.2+ installed

Don't have to install maven. Run one of the commands:
1. mvnw shell command for Mac and Linux
2. mvnw.cmd for Windows

These will install the correct Maven version the first time they are run.

#### 4. Python >=3.12 installed
1. Install the version that is specified in <a href="https://github.com/rchDev/game-of-points/blob/main/.python-version" target="_blank">.python-version</a> file (use pyenv)
2. Install version that is complies with spec in <a href="https://github.com/rchDev/game-of-points/blob/main/pyproject.toml" target="_blank">pyproject.toml</a> file.

#### 5. Node.js >=v16.20.2 installed

### Optional:

#### 1. You have installed IntelliJ IDEA: <a href="https://www.jetbrains.com/idea/download/?section=mac" target="_blank">Mac</a>, <a href="https://www.jetbrains.com/idea/download/?section=linux" target="_blank">Linux</a>, <a href="https://www.jetbrains.com/idea/download/?section=windows" target="_blank">Windows</a>

Helpful for easily launching the project.

#### 2. You have installed <a href="https://python-poetry.org/docs/#installation" target="_blank">Poetry</a>

Although not required, but project uses poetry for easy dependency management.
You can use your own virtual environment and install from [requirements.txt](https://github.com/rchDev/game-of-points/blob/main/requirements.txt) file.

#### 3. You have installed <a href="https://ngrok.com/docs/getting-started/" target="_blank">Ngrok</a>

Used for exposing your local game server to
Google's conversational agents' webhook,
without hosting the game server yourself.

## 🚀 How to launch this project

#### Project setup:
1. Inside game-of-points root run: 
```
poetry install
``` 
>or activate your desired virtual environment and run: 
```
pip install -r ./requirements.txt
```
2. Inside game-of-points/game-of-points-fe run: 
```
npm install
```

#### Launch steps:
1. step 1

#### General launch rules:
1. Game server depends on working python services (bayes-net and sentiment classifier).
2. Game frontend depends on working game server
3. Player data collection step in frontend chat depends on ngrok tunnel (if you are not hosting your backend on public ip address).

## 🏗️ How system works (Top down view)
