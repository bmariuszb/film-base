package zti.filmbase;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class FilmBaseWebsocketEndpoint {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
        try {
            session.getBasicRemote().sendText(new JPAResource().getLoginPage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("WebSocket connection error: " + session.getId());
        error.printStackTrace();
    }
}
