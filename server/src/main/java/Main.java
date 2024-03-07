import chess.*;
import dataaccess.DatabaseManager;

public class Main {
    public static void main(String[] args) throws Exception {
        server.Server server = new server.Server();
        server.run(8080);
    }
}