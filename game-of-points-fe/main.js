import p5 from "p5";
import { cloneDeep } from "lodash";

document.querySelector('df-messenger').addEventListener('df-response-received', handleDfResponseEvent);
let weapons = await fetchWeapons();
let randomWeaponId = selectRandomWeapon(weapons);
let weapon = weapons[randomWeaponId];
updatePlayerStats({
  player: {
    points: 0,
    hitPoints: 3,
    damage: weapon.damage,
    ammo: weapon.ammoCapacity,
    speed: weapon.speedModifier,
    reach: weapon.range,
  }
})

async function displayError(errorMessage) {
  document.querySelector('df-messenger').classList.add('removed');
  document.querySelector('.game-container').classList.add('removed');
  document.querySelector('.pregame-buttons').classList.add('removed');
  document.getElementById('messenger-error').classList.remove('removed');
  document.getElementById('error-time-label').classList.add("removed");
  const messengerErrorText = document.getElementById('messenger-error-text');
  messengerErrorText.innerText = errorMessage;
  messengerErrorText.classList.remove('removed');
}

const displayTimedError = async (errorMessage, timeToShow) => {
  await displayError(errorMessage);
  const errorTimeText = document.getElementById('error-time-label');
  errorTimeText.classList.remove("removed");

  const countdown = setInterval(async () => {
    if (timeToShow <= 0) {
      clearInterval(countdown);

      document.querySelector('.game-container').classList.remove('removed');
      document.getElementById('messenger-error').classList.add('removed');
      document.getElementById('messenger-error-text').classList.add('removed');
      errorTimeText.classList.add("removed");

      const { offsetWidth: width, offsetHeight: height } =
          document.getElementById("game-canvas");

      let { sessionId, gameState } = await getInitialGameState(
          null,
          weapon.id,
          width,
          height,
      );
      updatePlayerStats(gameState);

      new p5(sketch(sessionId, gameState));
    } else {
      document.getElementById("error-time").textContent = timeToShow.toString();
      timeToShow--;
    }
  }, 1000);
}

window.addEventListener('df-messenger-error', async () => {
  await displayTimedError(
      '🪳🪳🪳🪳 🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
      '🪳🪳🪳 Google\'s conversational agent server is not responding: 🪳🪳🪳\n' +
      '🪳🪳🪳🪳 1. You forgot to publish agent🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
      '🪳🪳🪳🪳 2. Googles servers are down🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
      '🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n',
      5
  );
});

window.addEventListener('df-session-expired', async () => {
  await displayTimedError(
      '🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
      '🪳🪳🪳 Session with conversational agent expired🪳🪳🪳\n' +
      ' 🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n',
      5
  );
});

window.addEventListener('df-session-ended', async () => {
  await displayTimedError(
      '🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
      '🪳🪳🪳Session with conversational agent has ended🪳🪳\n' +
      ' 🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n',
      5
  );
});

document.getElementById('yes-button').addEventListener('click', () => {
  const pregameButtons = document.getElementsByClassName("pregame-buttons");
  for (let btn of pregameButtons) {
    btn.classList.add("removed");
  }

  const messenger = document.getElementById("messenger");
  if (messenger == null) {
    console.log("Failed to handle YesPreGameButton, CAUSE: no messenger found")
    return
  }
  messenger.classList.remove("removed");

  const gameContainers = document.getElementsByClassName("game-container");
  for (let container of gameContainers) {
    container.classList.remove("removed")
  }
});

document.getElementById('no-button').addEventListener('click', async () => {
  const pregameButtons = document.getElementsByClassName("pregame-buttons");
  for (let btn of pregameButtons) {
    btn.classList.add("removed");
  }

  const gameContainers = document.getElementsByClassName("game-container");
  for (let container of gameContainers) {
    container.classList.remove("removed")
  }

  const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");

  let { sessionId, gameState } = await getInitialGameState(
      null,
      weapon.id,
      width,
      height,
  );

  updatePlayerStats(gameState);

  new p5(sketch(sessionId, gameState));
})

async function handleDfResponseEvent(event) {
  console.log('HANDLE_DF_RESPONSE_EVENT_CALLED:', event.detail);

  const pageDisplayName = event.detail.raw.queryResult.currentPage.displayName;

  if (pageDisplayName === 'End Session') {
    document.querySelector('df-messenger').removeEventListener('df-response-received', handleDfResponseEvent);

    const dfSessionId = event.detail.raw.queryResult.diagnosticInfo["Session Id"];

    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");

    let { sessionId, gameState } = await getInitialGameState(
      dfSessionId,
      weapon.id,
      width,
      height,
    );

    updatePlayerStats(gameState);

    const messenger = document.getElementById("messenger");
    messenger.classList.add("removed");

    new p5(sketch(sessionId, gameState));
  }
}

async function countdownTimer(duration) {
  for (let seconds = duration; seconds > 0; seconds--) {
    console.log("game starts in:", seconds);
    await new Promise((resolve) => setTimeout(resolve, 1000));
  }
}

async function fetchWeapons() {
  try {
    const response = await fetch("http://localhost:8080/weapons");
    return response.json();
  } catch (e) {
    await displayError(
        '🪳🪳🪳🪳 🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
        '🪳🪳🪳Game server is not responding: 🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
        '🪳🪳🪳🪳 1. You forgot to launch a server🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n' +
        '🪳🪳🪳🪳 2. Some weird bug occurred. Try restarting the server.🪳🪳🪳🪳\n' +
        '🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳🪳\n',
    );
    throw new Error("Network response was not ok");
  }
}

function selectRandomWeapon(weapons) {
  const randomIndex = Math.floor(Math.random() * weapons.length);
  return weapons[randomIndex].id;
}

async function getInitialGameState(dfSessionId, weaponId,  windowWidth, windowHeight) {
  const response = await fetch("http://localhost:8080/games", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      weaponId,
      windowWidth,
      windowHeight,
      dialogFlowSessionId: dfSessionId,
    }),
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }

  console.log("Weapon selected successfully");

  return response.json();
}

function connectToGameSession(sessionId, mousePositionSendTimer) {
  const ws = new WebSocket(`ws://localhost:8080/games/${sessionId}`);

  ws.onopen = () => {
    console.log("WebSocket connection established");
  };

  ws.onerror = (error) => {
    console.error("WebSocket error:", error);
  };

  ws.onclose = () => {
    console.log("WebSocket connection closed");
    mousePositionSendTimer && clearInterval(mousePositionSendTimer);
  };

  return ws;
}

function updatePlayerStats(gameState) {
  if (!gameState) return;
  const {
    player: {
      points: playerPoints,
      hitPoints: playerHp,
      damage: playerDmg,
      ammo: playerAmmo,
      speed: playerSpd,
      reach: playerReach,
    },
  } = gameState;

  document.querySelector(".player-score").innerText = `Player: ${playerPoints}`;
  document.querySelector("#player-hp-real").innerText = playerHp;
  document.querySelector("#player-dmg-real").innerText = playerDmg;
  document.querySelector("#player-ammo-real").innerText = playerAmmo;
  document.querySelector("#player-spd-real").innerText = playerSpd.toFixed(2);
  document.querySelector("#player-reach-real").innerText = playerReach;
}

function updateAgentPoints(points) {
  document.querySelector(".ai-score").innerText = `${points}: AI`;
}

const sketch = (sessionId, gameState) => (p) => {
  const isMoving = {
    up: false,
    down: false,
    left: false,
    right: false,
  };

  let lastSentMousePosition = { x: -1, y: -1 };
  const mousePositionSendThreshold = 10; // px
  const mousePositionSendInterval = 100; // ms
  let mousePositionSendTimer;
  let predictions = [];
  let unappliedStateChanges = [];
  const emotions = {
    AVOID: "😱",
    SAFE_COLLECT: "🤗",
    AGGRESSIVE_COLLECT: "🤑",
    KILL: "🤬",
  };

  const sendPlayerActionToServer = (type, details) => {
    if (!p.ws) return;

    const clientTimestamp = Date.now();

    predictions.push({ type, details, clientTimestamp });
    p.ws.send(JSON.stringify({ type, ...details, clientTimestamp }));
  };

  const addToUnappliedStateChanges = (gameState, gameStateUpdate) => {
    const diff = gameState.resources.filter(
      ({ id }) => !gameStateUpdate.resources.some((item) => item.id === id),
    );
    unappliedStateChanges.push({
      agent: gameStateUpdate.agent,
      deltaBetweenUpdates: gameStateUpdate.deltaBetweenUpdates,
      resources: diff,
    });
  };

  const renderHealthBar = (entity) => {
    const entityHP = entity.hitPoints;
    const barWidth = 20;
    const spaceBetween = 8;
    const healthBarHeight = 10;
    const healthBarWidth = 3 * (barWidth + spaceBetween) - spaceBetween;
    p.stroke("red");
    p.strokeWeight(4);
    p.fill(255, 0, 0);
    for (let i = 0; i < 3; i++) {
      if (i < entityHP) {
        p.fill(255, 0, 0);
      } else {
        p.noFill();
      }
      p.rect(
        entity.x - healthBarWidth / 2 + i * (barWidth + spaceBetween),
        entity.y - entity.hitBox.height / 2 - 10,
        barWidth,
        healthBarHeight,
      );
    }
    p.stroke("black");
    p.strokeWeight(1);
  };
  const onServerUpdate = (ws, message) => {
    var updatedGameState = JSON.parse(message.data);
    if (updatedGameState.gameHasEnded) {
      ws.close();
      return;
    }

    addToUnappliedStateChanges(cloneDeep(p.gameState), updatedGameState);
    reconcileWithServerState(updatedGameState);
    updatePlayerStats(p.gameState);
    document.getElementById("game-time").innerText =
      `Time: ${p.gameState.time}s`;
  };

  const reconcileWithServerState = (gameState) => {
    predictions = predictions.filter(
      (action) => action.timeStamp >= gameState.lastAppliedClientTimestamp,
    );

    const tempGameState = cloneDeep(gameState);
    predictions.forEach((action) => {
      applyPrediction(tempGameState, action);
    });

    const agent = p.gameState.agent;
    p.gameState = tempGameState;
    p.gameState.agent = agent;
  };

  const applyPrediction = (gameState, action) => {
    switch (action.type) {
      case "move":
        gameState.player.x += action.details.dx;
        gameState.player.y += action.details.dy;
        break;
      case "aim":
        gameState.player.mouseX = action.details.mouseX;
        gameState.player.mouseY = action.details.mouseY;
        break;
      case "collect":
        gameState.player.resources = gameState.player.resources.filter(
          (r) => r.id !== action.details.id,
        );
        break;
      case "shoot":
        break;
      default:
        console.error("Unknown action type:", action.type);
    }
  };

  p.preload = async () => {
    p.gameStateLoaded = false;

    await countdownTimer(0);

    p.ws = connectToGameSession(sessionId, mousePositionSendTimer);
    p.ws.onmessage = (message) => onServerUpdate(p.ws, message);

    p.gameState = gameState;
    p.gameStateLoaded = true;
  };

  p.setup = () => {
    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");

    let canvas = p.createCanvas(width, height);
    canvas.parent("game-canvas");
    p.fill(255, 0, 0);
    p.shotTime = -1;
    p.agentShotTime = -1;

    mousePositionSendTimer = setInterval(
      sendMousePositionUpdate,
      mousePositionSendInterval,
    );
  };

  const sendMousePositionUpdate = () => {
    if (
      p.mouseX !== lastSentMousePosition.x ||
      p.mouseY !== lastSentMousePosition.y
    ) {
      const mousePosition = { x: p.mouseX, y: p.mouseY };

      lastSentMousePosition = mousePosition;
      sendPlayerActionToServer("aim", {
        mouseX: p.mouseX,
        mouseY: p.mouseY,
        gameStateTimeStap: p.gameState.lastUpdateTime,
      });
    }
  };

  p.mouseMoved = () =>
    needsServer(() => {
      if (
        p.dist(
          p.mouseX,
          p.mouseY,
          lastSentMousePosition.x,
          lastSentMousePosition.y,
        ) > mousePositionSendThreshold
      ) {
        console.log("needs server");
        sendMousePositionUpdate();
      }
    });

  function getMovementVector(isMoving, entity) {
    const movementVector = { x: 0, y: 0 };

    if (isMoving.up) movementVector.y -= entity.speed;
    if (isMoving.down) movementVector.y += entity.speed;
    if (isMoving.left) movementVector.x -= entity.speed;
    if (isMoving.right) movementVector.x += entity.speed;

    return movementVector;
  }

  function predictMovementAndSendUpdate(deltaTime) {
    deltaTime = Math.floor(deltaTime);

    const { player } = p.gameState;
    let { x: dx, y: dy } = getMovementVector(isMoving, player);

    if (dx !== 0 && dy !== 0) {
      dx /= Math.sqrt(2);
      dy /= Math.sqrt(2);
    }

    dx *= deltaTime;
    dy *= deltaTime;

    player.x = p.constrain(
      player.x + dx,
      0 + player.hitBox.width / 2,
      p.gameState.zone.width - player.hitBox.width / 2,
    );
    player.y = p.constrain(
      player.y + dy,
      0 + player.hitBox.height / 2,
      p.gameState.zone.height - player.hitBox.height / 2,
    );

    if (dx !== 0 || dy !== 0) {
      sendPlayerActionToServer("move", {
        dx,
        dy,
        gameStateTimeStap: p.gameState.lastUpdateTime,
      });
    }
  }

  const needsServer = (func) => {
    if (
      !p.ws ||
      p.ws.readyState === WebSocket.CLOSING ||
      p.ws.readyState === WebSocket.CLOSED
    ) {
      return;
    }
    return func();
  };

  p.mouseClicked = () =>
    needsServer(() => {
      const player = p.gameState.player;

      if (player.weapon.ammo <= 0 || player.weapon.recharging) return;

      p.shotTime = Date.now();
      sendPlayerActionToServer("shoot", {
        damage: p.gameState.player.damage,
        gameStateTimeStamp: p.gameState.lastUpdateTime,
      });
    });

  function updateMovement(keyCode, isPressed) {
    switch (keyCode) {
      case 87: // W
        isMoving.up = isPressed;
        break;
      case 83: // S
        isMoving.down = isPressed;
        break;
      case 65: // A
        isMoving.left = isPressed;
        break;
      case 68: // D
        isMoving.right = isPressed;
        break;
      default: // Do nothing
        break;
    }
  }

  p.keyPressed = () => needsServer(() => updateMovement(p.keyCode, true));

  p.keyReleased = () => needsServer(() => updateMovement(p.keyCode, false));

  // Rendering logic
  function render() {
    const { player, agent, resources } = p.gameState;

    p.clear();

    if (player) {
      p.fill("red");
      // p.ellipse(player.x, player.y, player.hitBox.width, player.hitBox.height);

      renderHealthBar(player);
      renderReloadTimer(player);
      p.textSize(player.hitBox.width);
      p.text(
        emotions["AGGRESSIVE_COLLECT"],
        player.x - player.hitBox.width / 2,
        player.y + player.hitBox.height / 2,
      );
      renderReach({ x: player.x, y: player.y }, player.reach, [255, 0, 0, 191]);
      renderReach(
        { x: player.x, y: player.y },
        agent.knowledge.playerReach.value,
        [255, 0, 0, 69],
      );
      renderShot(
        p.shotTime,
        { x: player.x, y: player.y },
        player.reach,
        [255, 0, 0, 191],
      );
    }

    if (agent) {
      renderHealthBar(agent);
      renderReloadTimer(agent);
      p.fill("blue");
      p.textSize(agent.hitBox.width);
      const agentChoice = agent.knowledge.agentChoice ?? {
        type: "SAFE_COLLECT",
      };
      p.text(
        emotions[agentChoice.type],
        agent.x - agent.hitBox.width / 2,
        agent.y + agent.hitBox.height / 2,
      );
      renderReach({ x: agent.x, y: agent.y }, agent.reach, [0, 0, 255, 69]);
      renderShot(
        p.agentShotTime,
        { x: agent.x, y: agent.y },
        agent.reach,
        [255, 0, 0, 191],
      );
    }

    if (resources) {
      const unappliedResources = unappliedStateChanges.flatMap(
        (state) => state.resources,
      );
      resources.concat(unappliedResources).forEach((resourse) => {
        p.stroke("purple");
        p.strokeWeight(resourse.hitBox.width);
        p.point(resourse.x, resourse.y);
        p.stroke("black");
        p.strokeWeight(1);
      });
    }
  }

  const renderShot = (shotTime, position, reach, color = [255, 0, 0, 69]) => {
    const timeSinceShot = Date.now() - shotTime;
    const timeTreshhold = 100;

    if (timeSinceShot > timeTreshhold) return;

    const maxCircleDiameter = reach * 2;
    const circleDiameter =
      maxCircleDiameter - maxCircleDiameter * (timeSinceShot / timeTreshhold);

    p.strokeWeight(6);
    p.stroke(...color);
    p.fill(...color);
    p.ellipse(position.x, position.y, circleDiameter);
    p.fill(255, 0, 0);
  };

  const renderReloadTimer = (entity) => {
    const reloadTime = entity.weapon.rechargeTimeMilli;
    const reloadTimeLeft = entity.weapon.rechargeTimeLeft;
    const timerBoxWidth = 76;
    const timerBarWidth = timerBoxWidth * (reloadTimeLeft / reloadTime);
    const timerBarHeight = 10;

    if (reloadTimeLeft > 0) {
      p.strokeWeight(4);
      p.stroke("red");
      p.fill("white");
      p.rect(
        entity.x - timerBoxWidth / 2,
        entity.y - entity.hitBox.height / 2 - 25,
        timerBoxWidth,
        timerBarHeight,
      );
      p.fill("red");
      p.rect(
        entity.x - timerBoxWidth / 2,
        entity.y - entity.hitBox.height / 2 - 25,
        timerBarWidth,
        timerBarHeight,
      );
    }
  };

  function renderReach(position, reach, color = [255, 0, 0, 69]) {
    p.stroke(color);
    p.strokeWeight(6);
    p.noFill();
    p.ellipse(position.x, position.y, reach * 2);
    p.fill(255, 0, 0);
    p.stroke("black");
    p.strokeWeight(1);
  }

  function interpolateAgent(deltaTime) {
    // Convert deltaTime to seconds if needed
    const dtSeconds = Math.floor(deltaTime);
    // Check if there are any updates to interpolate towards
    if (unappliedStateChanges.length > 0) {
      // Get the next update
      const update = unappliedStateChanges[unappliedStateChanges.length - 1];
      const agent = p.gameState.agent;
      const targetPos = { x: update.agent.x, y: update.agent.y };
      // Calculate the step based on agent's speed and deltaTime
      const directionX = targetPos.x - agent.x;
      const directionY = targetPos.y - agent.y;

      // Calculate the distance to the target position
      const distance = Math.sqrt(
        directionX * directionX + directionY * directionY,
      );

      // Normalize the direction vector
      const dirX = directionX / distance || 0;
      const dirY = directionY / distance || 0;

      // Calculate the step size based on the agent's speed and deltaTime
      const stepSize = agent.speed * dtSeconds;

      // Update the agent's position towards the target
      let newAgentX = agent.x + dirX * stepSize;
      let newAgentY = agent.y + dirY * stepSize;

      p.gameState.agent.x = newAgentX;
      p.gameState.agent.y = newAgentY;
      p.gameState.agent.mouseX = update.agent.mouseX;
      p.gameState.agent.mouseY = update.agent.mouseY;
      p.gameState.agent.hitPoints = update.agent.hitPoints;
      p.gameState.agent.isReloading = update.agent.isReloading;
      p.agentShotTime =
        p.gameState.agent.weapon.ammo < update.agent.weapon.ammo
          ? Date.now()
          : -1;
      p.gameState.agent.weapon.ammo = update.agent.weapon.ammo;
      p.gameState.agent.weapon.ammoCapacity = update.agent.weapon.ammoCapacity;

      // Check if the agent has reached the target position
      if (distance <= stepSize) {
        p.gameState.agent.x = targetPos.x;
        p.gameState.agent.y = targetPos.y;
        var stateChange =
          unappliedStateChanges[unappliedStateChanges.length - 1];
        unappliedStateChanges = [];
        updateAgentInfo(stateChange);
      }
    }
  }

  const updateAgentInfo = (update) => {
    let { agent } = update;
    updateAgentPoints(agent.points);
    updatePerceivedInfo(agent.knowledge);
  };

  const updatePerceivedInfo = (knowledge) => {
    const {
      playerHitPoints: { value: perceivedPlayerHp },
      playerDamage: { value: perceivedPlayerDmg },
      playerAmmoCapacity: { value: perceivedPlayerAmmo },
      playerSpeed: { value: perceivedPlayerSpd },
      playerReach: { value: perceivedPlayerReach },
    } = knowledge;

    document.querySelector("#player-hp-perceived").innerText =
      perceivedPlayerHp;
    document.querySelector("#player-dmg-perceived").innerText =
      perceivedPlayerDmg;
    document.querySelector("#player-ammo-perceived").innerText =
      perceivedPlayerAmmo;
    document.querySelector("#player-spd-perceived").innerText =
      perceivedPlayerSpd.toFixed(2);
    document.querySelector("#player-reach-perceived").innerText =
      perceivedPlayerReach;
  };

  // TODO: fix this
  function checkCollisionWithResources(player, resources) {
    for (let resource of resources) {
      if (
        player.x - player.hitBox.width / 2 <=
          resource.x + resource.hitBox.width / 2 &&
        player.x + player.hitBox.width / 2 >=
          resource.x - resource.hitBox.width / 2 &&
        player.y - player.hitBox.height / 2 <=
          resource.y + resource.hitBox.height / 2 &&
        player.y + player.hitBox.height / 2 >=
          resource.y - resource.hitBox.height / 2
      ) {
        sendPlayerActionToServer("collect", {
          ...resource,
          gameStateTimeStamp: p.gameState.lastUpdateTime,
        });
        p.gameState.resources = p.gameState.resources.filter(
          (r) => r.id !== resource.id,
        );
      }
    }
  }

  // Draw loop
  p.draw = async () => {
    if (!p.gameStateLoaded) {
      return;
    }

    needsServer(() => interpolateAgent(p.deltaTime));
    needsServer(() => predictMovementAndSendUpdate(p.deltaTime));
    needsServer(() =>
      checkCollisionWithResources(p.gameState.player, p.gameState.resources),
    );
    needsServer(() => render());
  };

  // Resize canvas
  p.windowResized = () => {
    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");
    p.resizeCanvas(width, height);
  };

  p.sketchEnded = () => {
    clearInterval(mousePositionSendTimer);
  };
};