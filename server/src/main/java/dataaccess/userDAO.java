package dataaccess;

import model.Records;

public class userDAO {

    public static String createUser(Records.UserData user) {
        return server.database.Database.getInstance().addUser(user);
    }

    public static Records.UserData getUser(String username) {
        return server.database.Database.getInstance().getUser(username);
    }
}
