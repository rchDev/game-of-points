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

const isMoving = {
  up: false,
  down: false,
  left: false,
  right: false,
};

const sketch = (p) => {
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
    console.log("key pressed", p.keyCode);
    console.log("isMoving", isMoving);
    // sendMovementUpdate(keycode, true, timestamp);
  };

  p.keyReleased = () => {
    const timestamp = Date.now();
    updateMovement(p.keyCode, false);
    console.log("key released", p.keyCode);
    console.log("isMoving", isMoving);
    // sendMovementUpdate(keycode, false, timestamp);
  };

  // Rendering logic
  function render() {
    const { player, agent, resources } = p.gameState;

    p.clear();

    if (player) {
      p.ellipse(player.x, player.y, player.hitBox.width, player.hitBox.height);
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
};

new p5(sketch);
