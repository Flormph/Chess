package server.clearapplication;

import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

/**
 * ClearApplicationHandler - handles the json translation for the clear application service
 */
public class Handler extends server.extenders.Handler{

    public Handler() {
    }

    public String Handle(Request Srequest, Response Sresponse) {
        server.clearapplication.Request request = serializer.fromJson(Srequest.body(), server.clearapplication.Request.class);
        server.clearapplication.Response response;
        try {
            server.clearapplication.Service service = new server.clearapplication.Service();
            response = Service.clearApplication(request);
        } catch(DataAccessException e) {
            //TODO A FLIP OR NOT TODO A FLIP (MAKE RESPONSE OBJECT FOR FAILURE
            response = new server.clearapplication.Response();
            Sresponse.body(serializer.toJson(response));
            Sresponse.status(e.getCode());
            return Sresponse.body();
        }
        Sresponse.body(serializer.toJson(response));
        Sresponse.status(200);
        return Sresponse.body();
    }

}
