package viergewinnt;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {
    public static WebSocketClient client;

    private Session s = null;
    private NachrichtenBearbeitung nachrichtenBearbeitung;
    public String raumId = null;

    public WebSocketClient (URI uri) throws Exception {

        // Verbinde mit WebSocket
        WebSocketContainer container =  ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, uri);

        // Referenz auf Klasse wird statisch freigegeben
        WebSocketClient.client = this;

    }

    /**
     * Methode wird aufgerufen, wenn sich der WebSocket mit dem Server verbindet
     * @param s Session die geöffent wurde.
     */
    @OnOpen
    public void onOpen(Session s) {
        System.out.println("Verbunden");
        this.s = s;
    }


    /**
     * Methode wird aufgerufen, wenn die Verbindung geschlossen wird.
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("Verbindung geschlossen");
        this.s = null;
    }


    /**
     * Methode wird aufgerufen, wenn eine Nachricht empfangen wurde.
     * @param message Nachricht, die Empafangen wurde
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.nachrichtenBearbeitung != null)
            this.nachrichtenBearbeitung.bearbeiteNachricht(message);
    }


    public void addMessageHandler(NachrichtenBearbeitung nachrichtenBearbeitung) {
        this.nachrichtenBearbeitung = nachrichtenBearbeitung;
    }


    /**
     * Sendet Nachrichten
     * @param message Nachricht, die gesendet werden soll.
     */
    public void sendMessage(String message) {
        this.s.getAsyncRemote().sendText(message);
    }


    public static interface NachrichtenBearbeitung {
        public void bearbeiteNachricht(String message);
    }
}
