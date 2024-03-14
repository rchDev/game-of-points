import p5 from "p5";
import { cloneDeep } from "lodash";

async function countdownTimer(duration) {
  for (let seconds = duration; seconds > 0; seconds--) {
    console.log("game starts in:", seconds);
    await new Promise((resolve) => setTimeout(resolve, 1000));
  }
}

async function fetchWeapons() {
  const response = await fetch("http://localhost:8080/weapons");
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  return response.json();
}

async function selectRandomWeapon(weapons) {
  const randomIndex = Math.floor(Math.random() * weapons.length);
  return weapons[randomIndex].id;
}

async function getInitialGameState(weaponId, windowWidth, windowHeight) {
  const response = await fetch("http://localhost:8080/games", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ weaponId, windowWidth, windowHeight }),
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }

  console.log("Weapon selected successfully");

  return response.json();
}

function connectToGameSession(sessionId) {
  const ws = new WebSocket(`ws://localhost:8080/games/${sessionId}`);

  ws.onopen = () => {
    console.log("WebSocket connection established");
  };

  ws.onerror = (error) => {
    console.error("WebSocket error:", error);
  };

  ws.onclose = () => {
    console.log("WebSocket connection closed");
  };

  return ws;
}

function updateStats(gameState) {
  if (!gameState) return;

  document.querySelector(".ai-score").innerText =
    `${gameState.agent.points}: AI`;
  document.querySelector(".player-score").innerText =
    `Player: ${gameState.player.points}`;
}

const sketch = (p) => {
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

  const sendPlayerActionToServer = (type, details) => {
    if (!p.ws) return;

    const clientTimestamp = Date.now();

    predictions.push({ type, details, clientTimestamp });
    p.ws.send(JSON.stringify({ type, ...details, clientTimestamp }));
  };

  const onServerUpdate = (message) => {
    reconcileWithServerState(JSON.parse(message.data));
    updateStats(p.gameState);
  };

  const reconcileWithServerState = (gameState) => {
    predictions = predictions.filter(
      (action) => action.timeStamp >= gameState.lastAppliedClientTimestamp,
    );

    const tempGameState = cloneDeep(gameState);
    predictions.forEach((action) => {
      applyPrediction(tempGameState, action);
    });

    p.gameState = tempGameState;
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
    const weapons = await fetchWeapons();
    const selectedWeaponId = await selectRandomWeapon(weapons);

    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");

    let { sessionId, gameState } = await getInitialGameState(
      selectedWeaponId,
      width,
      height,
    );

    updateStats(gameState);

    p.ws = connectToGameSession(sessionId);
    p.ws.onmessage = (message) => onServerUpdate(message);

    p.gameState = gameState;
    p.gameStateLoaded = true;
  };

  p.setup = () => {
    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");

    let canvas = p.createCanvas(width, height);
    canvas.parent("game-canvas");
    p.fill(255, 0, 0);

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
      sendPlayerActionToServer("aim", { mouseX: p.mouseX, mouseY: p.mouseY });
    }
  };

  p.mouseMoved = () => {
    if (
      p.dist(
        p.mouseX,
        p.mouseY,
        lastSentMousePosition.x,
        lastSentMousePosition.y,
      ) > mousePositionSendThreshold
    ) {
      sendMousePositionUpdate();
    }
  };

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

    player.x += dx * deltaTime;
    player.y += dy * deltaTime;

    if (dx !== 0 || dy !== 0) {
      sendPlayerActionToServer("move", {
        dx: dx * deltaTime,
        dy: dy * deltaTime,
      });
    }
  }

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

  p.keyPressed = () => updateMovement(p.keyCode, true);

  p.keyReleased = () => updateMovement(p.keyCode, false);

  // Rendering logic
  function render() {
    const { player, agent, resources } = p.gameState;

    p.clear();

    if (player) {
      p.ellipse(player.x, player.y, player.hitBox.width, player.hitBox.height);
      p.square(
        player.x - player.hitBox.width / 2,
        player.y - player.hitBox.height / 2,
        player.hitBox.width,
      );
      drawAimLine(player);
    }

    if (agent) {
      p.ellipse(agent.x, agent.y, agent.hitBox.width, agent.hitBox.height);
    }

    if (resources) {
      resources.forEach((resourse) => {
        p.stroke("purple");
        p.strokeWeight(resourse.hitBox.width);
        p.point(resourse.x, resourse.y);
        p.stroke("black");
        p.strokeWeight(1);
      });
    }
  }

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
        sendPlayerActionToServer("collect", resource);
        p.gameState.resources = p.gameState.resources.filter(
          (r) => r.id !== resource.id,
        );
      }
    }
  }

  function drawAimLine(entity) {
    // Calculate angle between player position and mouse position
    let angle = p.atan2(p.mouseY - entity.y, p.mouseX - entity.x);

    // Calculate the start point of the line based on the player's edge
    let startX = entity.x + p.cos(angle) * entity.hitBox.width;
    let startY = entity.y + p.sin(angle) * entity.hitBox.height;

    // Calculate the end point of the line based on the player's reach
    let endX = entity.x + p.cos(angle) * entity.reach;
    let endY = entity.y + p.sin(angle) * entity.reach;

    // Draw the line
    p.stroke("red");
    p.strokeWeight(4);
    p.line(startX, startY, endX, endY);
    p.strokeWeight(1);
    p.stroke("black");
  }

  // Draw loop
  p.draw = async () => {
    if (!p.gameStateLoaded) {
      return;
    }
    predictMovementAndSendUpdate(p.deltaTime);
    checkCollisionWithResources(p.gameState.player, p.gameState.resources);
    render();
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

new p5(sketch);
