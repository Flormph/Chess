package ui;

import static ui.JoinGame.joinGame;

public class JoinObserver {
    static void joinObserver(String line, int port) throws Exception {
        joinGame(line, port);
    }
}
