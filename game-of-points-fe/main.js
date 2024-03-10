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
      p.gameState = JSON.parse(message.data);
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

  const sendMovementUpdate = (timestamp, deltaTime) => {
    const { player } = p.gameState;
    p.ws.send(
      JSON.stringify({
        type: "move",
        playerX: player.x,
        playerY: player.y,
        mouseX: p.mouseX,
        mouseY: p.mouseY,
        timestamp: timestamp,
        detaTime: deltaTime,
      }),
    );
  };

  let lastUpdateTime = Date.now();

  function update(timestamp, deltaTime) {
    let { player } = p.gameState;
    // Calculate the vector from player to mouse
    let targetX = p.mouseX;
    let targetY = p.mouseY;
    let dx = targetX - player.x;
    let dy = targetY - player.y;

    // Calculate distance from player to mouse
    let distance = Math.sqrt(dx * dx + dy * dy);

    // Player will move only if the mouse is outside the player's circle radius
    if (distance > player.hitBox.width / 2) {
      // Normalize the direction vector
      let normalizedDx = dx / distance;
      let normalizedDy = dy / distance;

      // Update player's position towards the mouse at constant speed
      let updatedX = player.x + normalizedDx * player.speed * 200 * deltaTime;

      let updatedY = player.y + normalizedDy * player.speed * 200 * deltaTime;

      const prevPlayerX = player.x;
      const prevPlayerY = player.y;

      player.x = p.constrain(
        updatedX,
        player.hitBox.width / 2,
        p.width - player.hitBox.width / 2,
      );
      player.y = p.constrain(
        updatedY,
        player.hitBox.height / 2,
        p.height - player.hitBox.height / 2,
      );

      if (prevPlayerX !== player.x || prevPlayerY !== player.y) {
        sendMovementUpdate(timestamp, deltaTime);
      }
    }
  }

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

  p.draw = async () => {
    const now = Date.now();
    const deltaTime = (now - lastUpdateTime) / 1000.0;

    if (!p.gameStateLoaded) {
      return;
    }

    update(now, deltaTime);
    render();

    lastUpdateTime = now;
  };

  p.windowResized = () => {
    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");
    p.resizeCanvas(width, height);
  };
};

new p5(sketch);
