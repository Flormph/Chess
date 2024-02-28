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

    public static void deleteAuth(String auth) {
        server.database.Database.getInstance().deleteToken(auth);
    }

    public static String getAuth(String auth) {
        return server.database.Database.getInstance().getToken(auth);
    }

    /**
     *
     * @param auth an authToken
     * @return whether or not the provided token is present in the database
     */
    public static boolean hasAuth(String auth) {
        return server.database.Database.getInstance().hasToken(auth);
    }
}
