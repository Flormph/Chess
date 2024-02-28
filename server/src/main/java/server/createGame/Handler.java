package server.createGame;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class Handler extends server.extenders.Handler{
    public Handler() {super();}
    public String Handle(Request Srequest, Response Sresponse) {
        server.createGame.Request request = serializer.fromJson(Srequest.body(), server.createGame.Request.class);
        request.auth = Srequest.headers("authorization");
        server.createGame.Response response = null;
        try {
            Service service = new Service();
            response = service.createGame(request);
        } catch (DataAccessException e) {
            //TODO A FLIP OR NOT TODO A FLIP (MAKE RESPONSE OBJECT FOR FAILURE
            response = new server.createGame.Response(e.getMessage());
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }
}