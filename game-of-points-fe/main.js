import p5 from "p5";

async function countdownTimer(duration) {
  for (let seconds = duration; seconds > 0; seconds--) {
    console.log(seconds);
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

async function getGameState(weaponId) {
  const response = await fetch("http://localhost:8080/games", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ weaponId: weaponId }),
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

  ws.onmessage = (message) => {
    console.log("Received message:", message.data);
  };

  ws.onerror = (error) => {
    console.error("WebSocket error:", error);
  };

  ws.onclose = () => {
    console.log("WebSocket connection closed");
  };

  return ws;
}

const sketch = (p) => {
  p.preload = async () => {
    p.gameStateLoaded = false;
    await countdownTimer(0);
    const weapons = await fetchWeapons();
    const selectedWeaponId = await selectRandomWeapon(weapons);
    const { sessionId, gameState } = await getGameState(selectedWeaponId);
    p.ws = connectToGameSession(sessionId);
    p.player = gameState.player;
    p.agent = gameState.agent;
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
    p.ws.send(
      JSON.stringify({
        type: "move",
        playerX: p.player.x,
        playerY: p.player.y,
        mouseX: p.mouseX,
        mouseY: p.mouseY,
        timestamp: timestamp,
        detaTime: deltaTime,
      }),
    );
  };

  let lastUpdateTime = Date.now();

  function update(timestamp, deltaTime) {
    // Calculate the vector from player to mouse
    let targetX = p.mouseX;
    let targetY = p.mouseY;
    let dx = targetX - p.player.x;
    let dy = targetY - p.player.y;

    // Calculate distance from player to mouse
    let distance = Math.sqrt(dx * dx + dy * dy);

    // Player will move only if the mouse is outside the player's circle radius
    if (distance > p.player.hitBox.width / 2) {
      // Normalize the direction vector
      let normalizedDx = dx / distance;
      let normalizedDy = dy / distance;

      // Update player's position towards the mouse at constant speed
      let updatedX =
        p.player.x + normalizedDx * p.player.speed * 200 * deltaTime;

      let updatedY =
        p.player.y + normalizedDy * p.player.speed * 200 * deltaTime;

      const prevPlayerX = p.player.x;
      const prevPlayerY = p.player.y;

      p.player.x = p.constrain(
        updatedX,
        p.player.hitBox.width / 2,
        p.width - p.player.hitBox.width / 2,
      );
      p.player.y = p.constrain(
        updatedY,
        p.player.hitBox.height / 2,
        p.height - p.player.hitBox.height / 2,
      );

      console.log("time diff ms:", deltaTime);

      if (prevPlayerX !== p.player.x || prevPlayerY !== p.player.y) {
        sendMovementUpdate(timestamp, deltaTime);
      }
    }
  }

  function render() {
    p.clear();
    p.ellipse(
      p.player.x,
      p.player.y,
      p.player.hitBox.width,
      p.player.hitBox.height,
    );

    p.ellipse(
      p.agent.x + 50,
      p.agent.y + 50,
      p.agent.hitBox.width,
      p.agent.hitBox.height,
    );
  }

  p.draw = async () => {
    const now = Date.now();
    const deltaTime = (now - lastUpdateTime) / 1000.0;

    if (!p.gameStateLoaded) {
      return;
    }

    update(now, deltaTime);
    render();

    // Consider recalculating Date.now()
    lastUpdateTime = now;
  };

  p.windowResized = () => {
    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");
    p.resizeCanvas(width, height);
  };
};

new p5(sketch);
