package dataAccess;

import model.Records;
import server.database.Database;
import chess.*;

public class gameDAO {
    public static int createGame(Records.GameData game) {
        server.database.Database.getInstance().addGame(game.gameName(), game);
        return game.gameID();
    }

    static Records.GameData getGame(int id) {
        return server.database.Database.getInstance().gameFromID(id);
    }

    static void deleteGame(int id) {
        server.database.Database.getInstance().deleteGame(id);
    }

    static void deleteAllGames() {
        server.database.Database.getInstance().deleteAllGames();
    }
}
