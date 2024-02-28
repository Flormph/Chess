package server.join;

public class Request {
    public String playerColor;
    public String gameID;
    public String auth;
    Request(String playerColor, String gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
}
