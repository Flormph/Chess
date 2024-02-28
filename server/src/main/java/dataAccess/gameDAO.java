package dataAccess;

import model.Records;
import server.clearApplication.Response;
import server.database.Database;
import chess.*;

import java.util.ArrayList;
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

    static void deleteGame(int id) {
        server.database.Database.getInstance().deleteGame(id);
    }

    static void deleteAllGames() {
        server.database.Database.getInstance().deleteAllGames();
    }

    public static void setWhitePlayer(int ID, String userName) {
        server.database.Database.getInstance().setWhitePlayer(ID, userName);
    }
    public static void setBlackPlayer(int ID, String userName) {
        server.database.Database.getInstance().setBlackPlayer(ID, userName);
    }
    public static void addObserver(int ID, String userName) {
        server.database.Database.getInstance().addObserver(ID, userName);
    }

    public static HashSet<Records.GameData> getList(String auth) {
        HashSet<Records.GameData> list = new HashSet<>(server.database.Database.getInstance().getGames());
        return list;
    }

    public static Response clearApplication() {
        server.database.Database.getInstance().clearApplication();
        return new server.clearApplication.Response();
    }
}
