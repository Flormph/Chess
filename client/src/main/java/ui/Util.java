package ui;


import java.util.Objects;

public class Util {
    public static Util util;
    private String token;
    private int port;
    public static Util getInstance() {
        return Objects.requireNonNullElseGet(util, Util::new);
    }

    private static String loginToken = null;

    public Util() {
        port = 8080;
    }

    public Util(int port) {
        this.port = port;
    }

    public static String getToken() {
        return loginToken;
    }

    public static void setToken(String token) {
        loginToken = token;
    }

    public static String[] convertWords(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        return line.trim().split("\\s+");
    }

    public static void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

}
