package server.createGame;

public class Request {
    public String gameName;
    public String auth;
    public Request(String gameName) {
        this.gameName = gameName;
    }
    public Request(String gameName, String auth) {
        this.gameName = gameName;
        this.auth = auth;
    }
}
