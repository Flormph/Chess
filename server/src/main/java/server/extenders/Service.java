package server.extenders;

import server.database.Database;

public class Service {
    protected static Database database = null;
    protected Service() {
        database = Database.getInstance();
    }
}
