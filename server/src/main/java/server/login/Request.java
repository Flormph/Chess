package server.login;

public class Request {
    public String username;
    public String password;
    public Request(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
