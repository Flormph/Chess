package server.list;

import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public class Handler extends server.extenders.Handler{
    public Handler() {super();}
    public String Handle(Request Srequest, Response Sresponse) {
        server.list.Request request = new server.list.Request(Srequest.headers("authorization"));
        server.list.Response response = null;
        try {
            Service service = new Service();
            response = service.listGames(request);
        } catch (DataAccessException e) {
            //TODO A FLIP OR NOT TODO A FLIP (MAKE RESPONSE OBJECT FOR FAILURE
            response = new server.list.Response(e.getMessage());
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }
}