package server.database;

import model.Records;

import java.util.*;

/**
 * Database - a temporary class to function in place of a remote server for phase 3 of the chess project. It stores the games, users, and other information.
 */
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
            games = new ArrayList<Records.GameData>();
        }
        if(users == null) {
            users = new ArrayList<Records.UserData>();
        }
        if(tokens == null) {
            tokens = new HashSet<Records.AuthData>();
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
                //todo add logic for add game
                return;
            }
        }

        games.add(game);
    }

    public boolean deleteGame(String gameName) {
        for(Records.GameData g: games) {
            if(Objects.equals(g.gameName(), gameName)) {
                games.remove(g);
                return true;
            }
        }
        return false;
    }

    public boolean deleteGame(int gameID) {
        for(Records.GameData g: games) {
            if(Objects.equals(g.gameID(), gameID)) {
                games.remove(g);
                return true;
            }
        }
        return false;
    }
    public boolean deleteAllGames() {
        games.clear();
        return true;
    }

    public boolean addUser(Records.UserData user) {
        for(Records.UserData u: users) {
            if (Objects.equals(u.username(), user.username())) {
                return false;
            }
        }
        users.add(user);
        return true;
    }

    public boolean deleteUser(String userName) {
        for(Records.UserData u: users) {
            if (Objects.equals(u.username(), userName)) {
                users.remove(u);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAllUsers() {
        users.clear();
        return true;
    }

    public boolean addAuthToken(Records.AuthData token) {
        if(tokens.contains(token)) {
            return false;
        }
        else {
            tokens.add(token);
            return true;
        }
    }

    public String getToken(String auth) {
        for (Records.AuthData a : tokens) {
            if(Objects.equals(a.authToken(), auth)) {
                return a.username();
            }
        }
        return null;
    }

    public boolean deleteAuthToken(Records.AuthData token) {
        if(tokens.contains(token)) {
            tokens.remove(token);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteAllAuthTokens() {
        tokens.clear();
        return true;
    }

    public boolean containsGame(String gameName) {
        for(Records.GameData g: games) {
            if(Objects.equals(g.gameName(), gameName)) {
                return true;
            }
        }
        return false;
    }

    public int getGameID() {
        UUID id = UUID.randomUUID();
        String str = "" + id;
        int myID = str.hashCode();
        String filterStr = "" + myID;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    public Records.GameData gameFromID(int id) {
        for(Records.GameData g: games) {
            if(g.gameID() == id) {
                return g;
            }
        }
        return null;
    }

    public String getNameFromID(int id) {
        for(Records.GameData g: games) {
            if(g.gameID() == id) {
                return g.gameName();
            }
        }
        return null;
    }

    public boolean gamesIsEmpty() {
        return games.isEmpty();
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

    public String generateToken(String username) {
        Records.AuthData token = new Records.AuthData(UUID.randomUUID().toString(), username);
        tokens.add(token);
        return token.authToken();
    }

    public boolean tokensContains(Records.AuthData token) {
        for(Records.AuthData t: tokens) {
            if(Objects.equals(t.authToken(), token.authToken())) return true;
        }
        return false;
    }

    public void nullifyToken(String username) {
        if (this.usersContains(username)) {
            tokens.remove(username);
        }
    }

    public boolean isEmpty(){
        return games.isEmpty() && users.isEmpty() && tokens.isEmpty();
    }
    public String printApplication() {
        return this.toString();
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
//todo add a to string method for debugging purposes
}
