package dataAccess;

import model.Records;

import java.util.UUID;

public class authDAO {
    public static String createAuth(String username) {
        Records.AuthData auth = new Records.AuthData(UUID.randomUUID().toString(), username);
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
