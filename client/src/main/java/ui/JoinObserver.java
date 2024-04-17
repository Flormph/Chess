package ui;

import model.Records;

import static ui.JoinGame.joinGame;

public class JoinObserver {
    public static Records.GameData joinObserver(String line, int port) throws Exception {
        return joinGame(line, port);
    }
}
