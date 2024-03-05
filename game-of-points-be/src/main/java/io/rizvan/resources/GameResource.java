package io.rizvan.resources;

import io.rizvan.beans.*;
import io.rizvan.beans.requests.PlayerCreationRequest;
import io.rizvan.beans.responses.GameResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/games")
public class GameResource {
    @Inject
    WeaponCache weaponCache;

    @Inject
    SessionStorage storage;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public GameResponse createGame(PlayerCreationRequest request) {
        int hp = 3;
        int x = 350;
        int y = 50;
        double speed = 1.0;
        int startingPoints = 0;

        var weapons = weaponCache.getWeapons();
        var weapon = weapons.stream()
                .filter(w -> w.getId() == request.getWeaponId())
                .findFirst()
                .orElse(weapons.get(0));

        var player = new Player(hp, x, y, speed, startingPoints, weapon);
        var agent = Agent.Type.SOLDIER.get();

        var gameState = new GameState(player, agent);
        var sessionId = generateSessionId();

        storage.addGame(generateSessionId(), gameState);

        return new GameResponse(gameState, sessionId);
    }

    private String generateSessionId() {
        // Implement session ID generation logic
        return java.util.UUID.randomUUID().toString();
    }
}
