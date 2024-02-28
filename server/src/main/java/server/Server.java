package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", (request, response) -> {
            server.clearApplication.Handler handler = new server.clearApplication.Handler();
            return handler.Handle(request, response);
        });

        Spark.post("/user", (request, response) -> {
            server.register.Handler handler = new server.register.Handler();
            return handler.Handle(request, response);
        });

        Spark.post("/session", (request, response) -> {
            server.login.Handler handler = new server.login.Handler();
            return handler.Handle(request, response);
        });

        Spark.delete("/session", (request, response) -> {
            server.logout.Handler handler = new server.logout.Handler();
            return handler.Handle(request, response);
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

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
