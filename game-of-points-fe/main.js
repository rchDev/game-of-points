document.addEventListener("DOMContentLoaded", async () => {
  // Start the countdown timer
  await countdownTimer(10); // 10 seconds countdown
  try {
    const weapons = await fetchWeapons();
    const selectedWeaponId = await selectRandomWeapon(weapons);
    await sendWeaponId(selectedWeaponId);
    establishWebSocketConnection();
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

async function sendWeaponId(weaponId) {
  const response = await fetch("http://localhost:8080/weapons", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ weapon_id: weaponId }),
  });
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  console.log("Weapon selected successfully");
}

function establishWebSocketConnection() {
  const ws = new WebSocket("ws://localhost:5005/game");

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
