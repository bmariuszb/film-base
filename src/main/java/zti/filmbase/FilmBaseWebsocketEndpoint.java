package zti.filmbase;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Represents a WebSocket endpoint for the FilmBase application.
 */
@ServerEndpoint("/websocket")
public class FilmBaseWebsocketEndpoint {
    /**
     * Called when a new WebSocket connection is opened.
     *
     * @param session The WebSocket session associated with the connection.
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened: " + session.getId());
    }

    /**
     * Called when a new message is received from a WebSocket client.
     *
     * @param message The message received from the client.
     * @param session The WebSocket session associated with the client.
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
        try {
            session.getBasicRemote().sendText(new JPAResource().getLoginPage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a WebSocket connection is closed.
     *
     * @param session The WebSocket session associated with the connection.
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    /**
     * Called when an error occurs in a WebSocket connection.
     *
     * @param session The WebSocket session associated with the connection.
     * @param error   The error that occurred.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("WebSocket connection error: " + session.getId());
        error.printStackTrace();
    }
}