package server.register;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

/**
 * RegisterRequest - stores request information for RegisterService
 */
public class Handler extends server.extenders.Handler{
    public Handler() {super();}
    public String Handler(Request Srequest, Response Sresponse) {
        server.register.Request request = serializer.fromJson(Srequest.body(), server.register.Request.class);
        server.register.Response response = null;
        try {
            server.register.Service service = new server.register.Service();
            response = Service.register(request);
        } catch (DataAccessException e) {
            //TODO A FLIP OR NOT TODO A FLIP (MAKE RESPONSE OBJECT FOR FAILURE
            response = new server.register.Response(e.getMessage());
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }
}