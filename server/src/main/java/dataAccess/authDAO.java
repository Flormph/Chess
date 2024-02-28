package dataAccess;

import model.Records;

import java.util.UUID;

public class authDAO {
    /**
     *
     * @param username name of the user to create the authToken around (should only be called during registration or login
     * @return Returns the token associated with the newly logged-in user
     */
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
