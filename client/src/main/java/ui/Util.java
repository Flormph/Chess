package ui;

import chess.*;
import model.Records;

public class Util {
    public static Util util;
    public static int port = 0;

    private static String loginToken = null;
    private static Records.GameData game;

    public Util() {
        port = 8080;
    }

    public Util(int port) {
        Util.port = port;
    }

    public static String getToken() {
        return loginToken;
    }

    public static void setToken(String token) {
        loginToken = token;
    }

    public static void setGame(Records.GameData game) {
        Util.game = game;
    }

    public static Records.GameData getGame() {
        return game;
    }

    public static void setBoard() {
        if(game != null) {
            game.game().resetBoard();
        }
        else {
            System.out.println("No current game");
        }
    }

    public static String[] convertWords(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        return line.trim().split("\\s+");
    }

    public static void highlight(Records.GameData game) {
        return;
    }

    public static int getPort() {
        return port;
    }

}
