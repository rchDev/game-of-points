---
layout: default
title: Configuring the conversational agent
nav_order: 1
parent: Configuration
permalink: /conv-agent-config/
---

# Configuring the conversational agent

{: .note }
*This guide was written 2025-04-22*

## Steps

1. Go to <a href="https://conversational-agents.cloud.google.com/projects" target="_blank">Conversational agents console</a> and select: **create a project**.
![Step 1](./assets/images/conv-agent-conf-step-1.png)
2. Choose project's name and press **create**.
![Step 2](./assets/images/conv-agent-conf-step-2.png)
3. You will be redirected to newly created project's dashboard. Select: **go to APIs overview**.
![Step 3](./assets/images/conv-agent-conf-step-3.png)
4. In **APIs & services** select: **enable APIs and services**.
![Step 4](./assets/images/conv-agent-conf-step-4.png)
5. Find and choose: **Dialogflow API**.
![Step 5](./assets/images/conv-agent-conf-step-5.png)
6. Choose: **enable**.
![Step 6](./assets/images/conv-agent-conf-step-6.png)
7. You will be redirected to Dialogflow APIs' page.
![Step 7](./assets/images/conv-agent-conf-step-7.png)
8. Go back to Conversational agents console under your project and press: **Create agent**.
![Step 8](./assets/images/conv-agent-conf-step-8.png)
9. Choose name, timezone and language, select **Flow** as conversation start and press **Create**.
![Step 9](./assets/images/conv-agent-conf-step-9.png)
10. Once the new agent gets created, you might be redirected to its dashboard, go back to the console, press: **three dots** and select: the **restore** option.
![Step 10](./assets/images/conv-agent-conf-step-10.png)
11. Upload a file: **exported_agent_snitch.blob** that can be found inside project's root directory and press **Restore**.
![Step 11](./assets/images/conv-agent-conf-step-11.png)
12. You will be redirected to agents dashboard, where you can see newly imported agent.
![Step 12](./assets/images/conv-agent-conf-step-12.png)
13. In side menu, select **Flows** and then select **Manage** tab. 
![Step 13](./assets/images/conv-agent-conf-step-13.png)
14. Inside a **Manage** tab, select **Webhooks** section and enter your own public server address with a path to /info-validation resource on a server.
![Step 14](./assets/images/conv-agent-conf-step-14.png)
15. Select: **Publish agent**, unauthenticated access (for dev purposes only), choose: **Side panel** ui style (doesn't matter really) and press **Enable Conversational Messenger**.
![Step 15](./assets/images/conv-agent-conf-step-15.png)
16. Now your agent will be available for outside use and you will get access to project-id and agent-id variables that will be used during npm install.
![Step 16](./assets/images/conv-agent-conf-step-16.png)

Now you have a working conversational agent, but game frontend doesn't know which agent it should be communicating with.
For this purpose i've made a simple script that can be ran on its own or during npm install and it will modify index.html file to insert ids into their places.

{: .info}
>During front-end setup, run:
>```shell
>npx cross-env PROJECT_ID=<conversational_agent_project_id> AGENT_ID=<conversational_agent_id> npm install && \
>```












