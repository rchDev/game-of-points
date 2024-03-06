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

const scetch = (p) => {
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
    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");

    let canvas = p.createCanvas(width, height);
    canvas.parent("game-canvas");
    p.fill(255, 0, 0);
  };

  p.draw = async () => {
    if (!p.gameStateLoaded) {
      return;
    }

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
  };

  p.windowResized = () => {
    const { offsetWidth: width, offsetHeight: height } =
      document.getElementById("game-canvas");
    p.resizeCanvas(width, height);
  };
};

new p5(scetch);
