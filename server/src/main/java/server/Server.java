package server;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        try {
            DatabaseManager.createDatabase();
        }
        catch(DataAccessException e) {
            e.printStackTrace();
        }

        Spark.staticFiles.location("web");

        Spark.delete("/db", (request, response) -> {
            server.clearapplication.Handler handler = new server.clearapplication.Handler();
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

        Spark.post("/game", (request, response) -> {
            server.creategame.Handler handler = new server.creategame.Handler();
            return handler.Handle(request, response);
        });

        Spark.put("/game", (request, response) -> {
            server.join.Handler handler = new server.join.Handler();
            return handler.Handle(request, response);
        });

        Spark.get("/game", (request, response) -> {
            server.list.Handler handler = new server.list.Handler();
            return handler.Handle(request, response);
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
