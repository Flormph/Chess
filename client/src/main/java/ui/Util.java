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

    public static String[] convertWords(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        return line.trim().split("\\s+");
    }

    public static int getPort() {
        return port;
    }

}
