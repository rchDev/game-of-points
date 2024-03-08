package io.rizvan;

import io.quarkus.vertx.ConsumeEvent;
import io.rizvan.beans.FactStorage;
import io.rizvan.beans.GameState;
import io.rizvan.beans.SessionStorage;
import io.rizvan.beans.facts.FactDeserializer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) {
        sessionStorage.addSession(sessionId, session);
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
    public void broadcast(String gameStateJson) {
        for (Session session : sessionStorage.getSessions()) {
            if (session.isOpen()) {
                try {
                    session.getAsyncRemote().sendText(gameStateJson);
                } catch (Exception e) {
                    System.err.println("Failed to send message: " + e.getMessage());
                }
            }
        }
    }
}
