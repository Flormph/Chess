package dataAccess;

import model.Records;

public class userDAO {

    static void createUser(Records.UserData user) {
        server.database.Database.getInstance().addUser(user);
    }

    static void removeUser(String username) {
        server.database.Database.getInstance().deleteUser(username);
    }

    static Records.UserData getUser(String username) {
        return server.database.Database.getInstance().getUser(username);
    }

    static void removeAllUsers() {
        server.database.Database.getInstance().deleteAllUsers();
    }
}
