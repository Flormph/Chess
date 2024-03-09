package server.creategame;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class Handler extends server.extenders.Handler{
    public Handler() {super();}
    public String Handle(Request Srequest, Response Sresponse) {
        server.creategame.Request request = serializer.fromJson(Srequest.body(), server.creategame.Request.class);
        request.auth = Srequest.headers("authorization");
        server.creategame.Response response;
        try {
            response = Service.createGame(request);
        } catch (DataAccessException e) {
            //TODO A FLIP OR NOT TODO A FLIP (MAKE RESPONSE OBJECT FOR FAILURE
            response = new server.creategame.Response(e.getMessage());
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }
}