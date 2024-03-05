document.addEventListener("DOMContentLoaded", async () => {
  // Start the countdown timer
  await countdownTimer(10); // 10 seconds countdown
  try {
    const weapons = await fetchWeapons();
    const selectedWeaponId = await selectRandomWeapon(weapons);
    const {
      sessionId,
      gameState: { player, agent },
    } = await getGameState(selectedWeaponId);
    console.log("Session ID:", sessionId);
    console.log("player:", player);
    console.log("agent:", agent);
    establishWebSocketConnection(sessionId);
  } catch (error) {
    console.error("An error occurred:", error);
  }
});

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

function establishWebSocketConnection(sessionId) {
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
}
