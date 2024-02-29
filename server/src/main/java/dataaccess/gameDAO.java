package dataaccess;

import model.Records;
import server.clearapplication.Response;

import java.util.HashSet;

public class gameDAO {
    public static int createGame(Records.GameData game) {
        server.database.Database.getInstance().addGame(game.gameName(), game);
        return game.gameID();
    }

    public static boolean findGame(int id) {return server.database.Database.getInstance().containsGame(id);}

    public static Records.GameData getGame(int id) {
        return server.database.Database.getInstance().gameFromID(id);
    }

    public static void setWhitePlayer(int ID, String userName) {
        server.database.Database.getInstance().setWhitePlayer(ID, userName);
    }
    public static void setBlackPlayer(int ID, String userName) {
        server.database.Database.getInstance().setBlackPlayer(ID, userName);
    }

    public static HashSet<Records.GameData> getList() {
        return new HashSet<>(server.database.Database.getInstance().getGames());
    }

    public static Response clearApplication() {
        server.database.Database.getInstance().clearApplication();
        return new server.clearapplication.Response();
    }
}
