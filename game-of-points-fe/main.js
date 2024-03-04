import "./style.css";

document.addEventListener("DOMContentLoaded", () => {
  // Start the countdown timer
  let countdown = 10; // 10 seconds countdown
  const timerId = setInterval(() => {
    console.log(countdown);
    if (countdown === 0) {
      clearInterval(timerId);
      fetchWeapons();
    } else {
      countdown--;
    }
  }, 1000);

  // Function to fetch weapons and handle the weapon selection and POST request
  function fetchWeapons() {
    fetch("http://localhost:8080/weapons")
      .then((response) => response.json())
      .then((weapons) => {
        const randomIndex = Math.floor(Math.random() * weapons.length);
        const selectedWeapon = weapons[randomIndex];
        sendWeaponId(selectedWeapon.id);
      })
      .catch((error) => console.error("Error fetching weapons:", error));
  }

  // Function to send the selected weapon ID
  function sendWeaponId(weaponId) {
    fetch("http://localhost:8080/weapons", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ weapon_id: weaponId }),
    })
      .then((response) => {
        if (response.ok) {
          console.log("Weapon selected successfully");
          establishWebSocketConnection();
        }
      })
      .catch((error) => console.error("Error sending weapon ID:", error));
  }

  // Function to establish a WebSocket connection
  function establishWebSocketConnection() {
    const ws = new WebSocket("ws://localhost:5001/game");

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
});
