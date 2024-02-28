package dataAccess;

import model.Records;

public class userDAO {

    public static String createUser(Records.UserData user) {
        return server.database.Database.getInstance().addUser(user);
    }

    static void removeUser(String username) {
        server.database.Database.getInstance().deleteUser(username);
    }

    public static Records.UserData getUser(String username) {
        return server.database.Database.getInstance().getUser(username);
    }

    static void removeAllUsers() {
        server.database.Database.getInstance().deleteAllUsers();
    }
}
