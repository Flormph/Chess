package server.logout;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class Handler extends server.extenders.Handler{
    public Handler() {super();}
    public String Handle(Request Srequest, Response Sresponse) {
        server.logout.Request request = new server.logout.Request(Srequest.headers("authorization"));
        server.logout.Response response = null;
        try {
            Service service = new Service();
            response = service.logout(request);
        } catch (DataAccessException e) {
            response = new server.logout.Response(e.getMessage());
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }
}