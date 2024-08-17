package io.rizvan.resources;

import io.rizvan.beans.*;
import io.rizvan.beans.actors.agent.Agent;
import io.rizvan.beans.actors.Player;
import io.rizvan.beans.actors.player.PlayerAnswersCache;
import io.rizvan.beans.dtos.requests.GameCreationRequest;
import io.rizvan.beans.dtos.responses.GameResponse;
import io.rizvan.utils.RandomNumberGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Collections;

@Path("/games")
public class GameResource {
    @Inject
    WeaponCache weaponCache;
    @Inject
    SessionStorage storage;
    @Inject
    RandomNumberGenerator rng;
    @Inject
    PlayerAnswersCache playerAnswersCache;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public GameResponse createGame(GameCreationRequest request) {
        var weapons = new ArrayList<>(weaponCache.getWeapons());

        if (weapons.isEmpty()) {
            System.err.println("No weapons found");
        }

        var playerWeapon = weapons.stream()
                .filter(w -> w.getId() == request.getWeaponId())
                .findFirst()
                .orElse(weapons.get(0));

        var playerAnswers = playerAnswersCache.getPlayerAnswers(request.getDialogFlowSessionId());
        var player = Player.fromWeapon(playerWeapon, playerAnswers);

        Collections.shuffle(weapons);
        var agentsWeapon = weapons.stream().findFirst().get();
        var agent = Agent.fromWeapon(agentsWeapon);

        var gameState = new GameState(
                player,
                agent,
                request.getWindowWidth(),
                request.getWindowHeight(),
                GameState.GAME_TIME,
                rng
        );
        var sessionId = generateSessionId();

        storage.addGameState(sessionId, gameState);

        return new GameResponse(gameState, sessionId);
    }

    private String generateSessionId() {
        // Implement session ID generation logic
        return java.util.UUID.randomUUID().toString();
    }
}
