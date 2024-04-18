package ui;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class WebSocketClient extends Endpoint{

    public Session session;


    public WebSocketClient(int port) throws Exception {
        URI uri = new URI("ws://localhost:"+ port + "/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
