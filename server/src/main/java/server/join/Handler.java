package server.join;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class Handler extends server.extenders.Handler{
    public Handler() {super();}
    public String Handle(Request Srequest, Response Sresponse) {
        server.join.Request request = serializer.fromJson(Srequest.body(), server.join.Request.class);
        request.auth = Srequest.headers("authorization");
        server.join.Response response = null;
        try {
            Service service = new Service();
            response = Service.joinGame(request);
        } catch (DataAccessException e) {
            //TODO A FLIP OR NOT TODO A FLIP (MAKE RESPONSE OBJECT FOR FAILURE
            response = new server.join.Response(e.getMessage());
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }
}