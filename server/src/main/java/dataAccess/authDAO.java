package dataAccess;

import model.Records;

public class authDAO {
    static String createAuth(Records.AuthData auth) {
        server.database.Database.getInstance().addAuthToken(auth);
        return auth.authToken();
    }

    static void deleteAuth(Records.AuthData auth) {
        server.database.Database.getInstance().deleteAuthToken(auth);
    }

    static String getAuth(String auth) {
        return server.database.Database.getInstance().getToken(auth);
    }
}
