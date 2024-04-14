package io.rizvan;

import io.quarkus.vertx.ConsumeEvent;
import io.rizvan.beans.SessionStorage;
import io.rizvan.beans.dtos.responses.GameEndedResponse;
import io.rizvan.beans.playerActions.PlayerActionDeserializer;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.time.Instant;

@ServerEndpoint("/games/{sessionId}")
@ApplicationScoped
public class StartWebSocket {
    @Inject
    SessionStorage sessionStorage;
    @Inject
    PlayerActionDeserializer playerActionDeserializer;
    @Inject
    EventBus eventBus;
    @Inject
    Jsonb jsonb;

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) {
        sessionStorage.addSession(sessionId, session);
        eventBus.publish("game.created", sessionId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("sessionId") String sessionId) {
        sessionStorage.removeSession(sessionId);
        sessionStorage.removeGameStates(sessionId);
    }

    @OnError
    public void onError(Session session, @PathParam("sessionId") String sessionId, Throwable throwable) {
        System.out.println("onError> " + ": " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sessionId") String sessionId) {
        var timestamp = Instant.now().toEpochMilli();
        var playerAction = playerActionDeserializer.deserialize(message);
        playerAction.setServerTimestamp(timestamp);
        sessionStorage.addPlayerAction(sessionId, playerAction);
    }

    @ConsumeEvent("game.closed")
    public void closeGame(String sessionId) {
        var session = sessionStorage.getSession(sessionId);
        if (session.isOpen()) {
            try {
                System.out.println("Game Closed got called");
                var latestGameState = sessionStorage.getLatestGameState(sessionId);
                if (latestGameState == null || !latestGameState.hasGameEnded()) {
                    return;
                }
                var agent = latestGameState.getAgent();
                var player = latestGameState.getPlayer();
                var gameEndedResponse = jsonb.toJson(
                        new GameEndedResponse(
                                agent,
                                player,
                                latestGameState.getTime(),
                                true
                        )
                );

                sessionStorage.removeSession(sessionId);
                sessionStorage.removeGameStates(sessionId);
                sessionStorage.removeLatestGameState(sessionId);

                session.getAsyncRemote().sendText(gameEndedResponse);
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game has ended"));
            } catch (Exception e) {
                System.err.println("Failed to close session: " + e.getMessage());
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Error occurred"));
                } catch (Exception closeException) {
                    System.err.println("Failed to close session: " + closeException.getMessage());
                }
            }
        }
    }

    @ConsumeEvent("game.update")
    public void broadcast(String sessionId) {
        var session = sessionStorage.getSession(sessionId);
        if (session.isOpen()) {
            try {
                var gameState = sessionStorage.getLatestGameState(sessionId);
                if (gameState == null) return;

                var gameStateJson = jsonb.toJson(gameState);
                session.getAsyncRemote().sendText(gameStateJson);
            } catch (Exception e) {
                System.err.println("Failed to send message: " + e.getMessage());
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Error occurred"));
                } catch (Exception closeException) {
                    System.err.println("Failed to close session: " + closeException.getMessage());
                }
            }
        }
    }
}
