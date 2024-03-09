/*package server.database;

import model.Records;

import java.util.*;
*/
/**
 * Database - a temporary class to function in place of a remote server for phase 3 of the chess project. It stores the games, users, and other information.
 */
/*
public class Database {
    public static ArrayList<Records.GameData> games;
    public static ArrayList<Records.UserData> users;
    public static HashSet <Records.AuthData> tokens;
    public static Database database;

    public static Database getInstance() {
        if(database == null) {
            database = new Database();
        }
        return database;
    }

    public Database() {
        if(games == null) {
            games = new ArrayList<>();
        }
        if(users == null) {
            users = new ArrayList<>();
        }
        if(tokens == null) {
            tokens = new HashSet<>();
        }
    }

    public void clearApplication() {
        deleteAllGames();
        deleteAllAuthTokens();
        deleteAllUsers();
    }

    public void addGame(String gameName, Records.GameData game) {
        for(Records.GameData g: games) {
            if(Objects.equals(g.gameName(), gameName)) {
                return;
            }
        }
        games.add(game);
    }

    public void deleteAllGames() {
        games.clear();
    }

    public String addUser(Records.UserData user) {
        for(Records.UserData u: users) {
            if (Objects.equals(u.username(), user.username())) {
                return null;
            }
        }
        users.add(user);
        return user.username();
    }

    public void deleteAllUsers() {
        users.clear();
    }

    public void addAuthToken(Records.AuthData token) {
        tokens.add(token);
    }
*/
    /**
     *
     * @param auth token of the current user
     * @return username of the current user
     */
    /*
    public String getToken(String auth) {
        for (Records.AuthData a : tokens) {
            if(Objects.equals(a.authToken(), auth)) {
                return a.username();
            }
        }
        return null;
    }

    public boolean hasToken(String auth) {
        for (Records.AuthData a : tokens) {
            if(Objects.equals(a.authToken(), auth)) {
                return true;
            }
        }
        return false;
    }

    public void deleteToken(String auth) {
        for (Records.AuthData a : tokens) {
            if(Objects.equals(a.authToken(), auth)) {
                tokens.remove(a);
                break;
            }
        }
    }

    public void deleteAllAuthTokens() {
        tokens.clear();
    }

    public boolean containsGame(int gameID) {
        for(Records.GameData g: games) {
            if(Objects.equals(g.gameID(), gameID)) {
                return true;
            }
        }
        return false;
    }

    public void setWhitePlayer(int ID, String userName) {
        for (int i = 0; i < games.size(); i++) {
            Records.GameData g = games.get(i);
            if (g.gameID() == ID) {
                Records.GameData updatedGame = new Records.GameData(g.gameID(), userName, g.blackUsername(), g.gameName(), g.game());
                games.set(i, updatedGame);
            }
        }
    }


    public void setBlackPlayer(int ID, String userName) {
        for (int i = 0; i < games.size(); i++) {
            Records.GameData g = games.get(i);
            if (g.gameID() == ID) {
                Records.GameData updatedGame = new Records.GameData(g.gameID(), g.whiteUsername(), userName, g.gameName(), g.game());
                games.set(i, updatedGame);
            }
        }
    }

    public Records.GameData gameFromID(int id) {
        for(Records.GameData g: games) {
            if(g.gameID() == id) {
                return g;
            }
        }
        return null;
    }

    public ArrayList<Records.GameData> getGames() {
        return games;
    }

    public boolean usersContains(String username) {
        for(Records.UserData u: users) {
            if(Objects.equals(u.username(), username)) {
                return true;
            }
        }
        return false;
    }

    public Records.UserData getUser(String username) {
        for(Records.UserData u: users) {
            if(Objects.equals(u.username(), username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Users:");
        sb.append('\n');
        for(Records.UserData u: users) {
            sb.append(u.toString());
            sb.append('\n');
        }

        sb.append("Games:");
        sb.append('\n');
        for(Records.GameData g: games) {
            sb.append(g.toString());
            sb.append('\n');
        }

        sb.append("Tokens:");
        sb.append('\n');
        for(Records.AuthData t: tokens) {
            sb.append('\t');
            sb.append("Username:");
            sb.append(t.username());
            sb.append('\n');
            sb.append('\t');
            sb.append("Token:");
            sb.append(t.authToken());
            sb.append('\n');
        }

        return sb.toString();
    }
}
*/