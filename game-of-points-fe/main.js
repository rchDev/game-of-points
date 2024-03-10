import p5 from "p5";

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

async function getGameState(weaponId, windowWidth, windowHeight) {
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

function setStats(gameState) {
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

  p.preload = async () => {
    p.gameStateLoaded = false;

    await countdownTimer(0);
    const weapons = await fetchWeapons();
    const selectedWeaponId = await selectRandomWeapon(weapons);

    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");

    let { sessionId, gameState } = await getGameState(
      selectedWeaponId,
      width,
      height,
    );

    setStats(gameState);

    p.ws = connectToGameSession(sessionId);
    p.ws.onmessage = (message) => {
      // p.gameState = JSON.parse(message.data);
    };

    p.gameState = gameState;
    p.gameStateLoaded = true;
  };

  p.setup = () => {
    // p.noCursor();
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

      // Update last sent position
      lastSentMousePosition = mousePosition;

      if (!p.ws) return;

      p.ws.send(
        JSON.stringify({ type: "aim", mouseX: p.mouseX, mouseY: p.mouseY }),
      );
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

  const sendMovementUpdate = (keycode, isPressed, timeAtButtonPress) => {
    const { player } = p.gameState;
    p.ws.send(
      JSON.stringify({
        type: "move",
        keycode,
        isPressed,
        playerX: player.x,
        playerY: player.y,
        clientTimestamp: timeAtButtonPress,
      }),
    );
  };

  function getMovementVector(isMoving, player) {
    const movementVector = { x: 0, y: 0 };

    if (isMoving.up) movementVector.y -= player.speed;
    if (isMoving.down) movementVector.y += player.speed;
    if (isMoving.left) movementVector.x -= player.speed;
    if (isMoving.right) movementVector.x += player.speed;

    return movementVector;
  }

  function handleMovement(deltaTime) {
    const { player } = p.gameState;
    let movementVector = getMovementVector(isMoving, player);

    if (movementVector.x !== 0 && movementVector.y !== 0) {
      movementVector.x /= Math.sqrt(2);
      movementVector.y /= Math.sqrt(2);
    }

    player.x += (movementVector.x * deltaTime) / 5;
    player.y += (movementVector.y * deltaTime) / 5;
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

  p.keyPressed = () => {
    const timestamp = Date.now();
    updateMovement(p.keyCode, true);
    // sendMovementUpdate(keycode, true, timestamp);
  };

  p.keyReleased = () => {
    const timestamp = Date.now();
    updateMovement(p.keyCode, false);
    // sendMovementUpdate(keycode, false, timestamp);
  };

  // Rendering logic
  function render() {
    const { player, agent, resources } = p.gameState;

    p.clear();

    if (player) {
      p.ellipse(player.x, player.y, player.hitBox.width, player.hitBox.height);
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

    handleMovement(p.deltaTime);
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
