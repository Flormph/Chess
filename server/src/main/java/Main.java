import chess.*;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        server.Server server = new server.Server();
        int result = server.run(8080);

    }
}