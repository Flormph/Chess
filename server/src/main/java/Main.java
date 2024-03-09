import dataAccess.DatabaseManager;

public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseManager.createDatabase();
        server.Server server = new server.Server();
        server.run(8080);
    }
}