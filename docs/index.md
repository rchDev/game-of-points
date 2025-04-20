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

## 📋 Before using this project

### Required:
#### 1. You have working Google Conversational Agent (~~Dialogflow CX~~)
#### 2. You have working *JDK*.
#### 3. You have working *python* setup
#### 4. You have working *Node.js* setup

### Optional:
> ❗❗❗ Due to having many small services, the project is hard to launch.
To mitigate this issue, launch scripts have been created.
To use these scripts, you have to install optional dependencies. ❗❗❗

#### 1. You have installed *IntelliJ IDEA*.
Helpful for easily launching the project.

> **IntelliJ IDEA Downloads: 
[Mac](https://www.jetbrains.com/idea/download/?section=mac), 
[Windows](https://www.jetbrains.com/idea/download/?section=windows),
[Linux](https://www.jetbrains.com/idea/download/?section=linux)**

#### 2. You have installed <a href="https://python-poetry.org/docs/#installation" target="_blank">Poetry</a>

Although not required, but project uses poetry for easy dependency management.

> You can use your own virtual environment and install from [requirements.txt](https://github.com/rchDev/game-of-points/blob/main/requirements.txt) file.

#### 3. You have installed *[ngrok](https://ngrok.com/docs/getting-started/)*.

Used for exposing your local game server to
Google's conversational agents' webhook,
without hosting the game server yourself.
<a href="https://example.com" target="_blank">Visit Example</a>

## 🚀 How to launch this project
#### Launch rules:
1. Game server depends on working python services (bayes-net and sentiment classifier).
2. Game frontend depends on working game server
3. Player data collection step in frontend chat depends on ngrok tunnel (if you are not hosting your backend on public ip address).
#### Recommended launch order:
1. ''''''
## 🏗️ How system works (Top down view)
