package ui;

import static ui.JoinGame.joinGame;

public class JoinObserver {
    public static int joinObserver(String line, int port) throws Exception {
        return joinGame(line, port);
    }
}
