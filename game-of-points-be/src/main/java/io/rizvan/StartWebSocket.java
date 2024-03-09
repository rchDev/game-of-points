package io.rizvan;

import io.quarkus.vertx.ConsumeEvent;
import io.rizvan.beans.FactStorage;
import io.rizvan.beans.SessionStorage;
import io.rizvan.beans.facts.FactDeserializer;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/games/{sessionId}")
@ApplicationScoped
public class StartWebSocket {
    @Inject
    SessionStorage sessionStorage;
    @Inject
    FactStorage factStorage;
    @Inject
    FactDeserializer factDeserializer;
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
        sessionStorage.removeGameState(sessionId);
    }

    @OnError
    public void onError(Session session, @PathParam("sessionId") String sessionId, Throwable throwable) {
        System.out.println("onError> " + ": " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sessionId") String sessionId) {
        var fact = factDeserializer.deserialize(message);
        factStorage.add(sessionId, fact);
    }

    @ConsumeEvent("game.update")
    public void broadcast(String sessionId) {
        var session = sessionStorage.getSession(sessionId);
        if (session.isOpen()) {
            try {
                var gameState = sessionStorage.getGameState(sessionId);
                var gameStateJson = jsonb.toJson(gameState);
                session.getAsyncRemote().sendText(gameStateJson);
            } catch (Exception e) {
                System.err.println("Failed to send message: " + e.getMessage());
            }
        }
    }
}
