package server.clearApplication;

import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

/**
 * ClearApplicationHandler - handles the json translation for the clear application service
 */
public class Handler extends server.extenders.Handler{

    public Handler() {
    }

    public String Handle(Request Srequest, Response Sresponse) {
        server.clearApplication.Request request = serializer.fromJson(Srequest.body(), server.clearApplication.Request.class);
        try {
            server.clearApplication.Service service = new server.clearApplication.Service();
            service.clearApplication(request);
        } catch(DataAccessException e) {
            //TODO A FLIP OR NOT TODO A FLIP (MAKE RESPONSE OBJECT FOR FAILURE
            server.clearApplication.Response response = new server.clearApplication.Response();
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        server.clearApplication.Response response = new server.clearApplication.Response();
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }

}
