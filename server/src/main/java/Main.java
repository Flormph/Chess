import chess.*;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        server.Server server = new server.Server();
        int result = server.run(8080);

    }

    public static void establishRoutes() {
        Spark.delete("/db", (request, response) -> {
            server.clearApplication.Handler handler = new server.clearApplication.Handler();
            return handler.Handler(request, response);
        });
/*


        Spark.post("/user", (request, response) -> {
            RegisterHandler handler = new RegisterHandler();
            return handler.Handler(request, response);
        });

        Spark.post("/session", (request, response) -> {
            LoginHandler handler = new LoginHandler();
            return handler.Handler(request, response);
        });

        Spark.delete("/session", (request, response) -> {
            LogoutHandler handler = new LogoutHandler();
            return handler.Handler(request, response);
        });

        Spark.get("/game", (request, response) -> {
            ListGameHandler handler = new ListGameHandler();
            return handler.Handler(request, response);
        });

        Spark.post("/game", (request, response) -> {
            CreateGameHandler handler = new CreateGameHandler();
            return handler.Handler(request, response);
        });

        Spark.put("/game", (request, response) -> {
            JoinGameHandler handler = new JoinGameHandler();
            return handler.Handler(request, response);
        });
*/
    }
}