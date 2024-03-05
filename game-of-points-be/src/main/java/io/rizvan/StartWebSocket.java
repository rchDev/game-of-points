package io.rizvan;

import io.rizvan.beans.SessionStorage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import static java.util.Objects.requireNonNull;

@ServerEndpoint("/games/{sessionId}")
@ApplicationScoped
public class StartWebSocket {
    @Inject
    SessionStorage storage;

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) {
        System.out.println("onOpen> ");
    }

    @OnClose
    public void onClose(Session session, @PathParam("sessionId") String sessionId) {
        System.out.println("onClose> ");
    }

    @OnError
    public void onError(Session session, @PathParam("sessionId") String sessionId, Throwable throwable) {
        System.out.println("onError> " + ": " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sessionId") String sessionId) {
        System.out.println("onMessage> " + ": " + message);
    }
}
