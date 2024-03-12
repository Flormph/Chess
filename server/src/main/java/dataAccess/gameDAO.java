package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Records;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class gameDAO {
    public static int createGame(Records.GameData game) throws DataAccessException{
        Gson serializer = new Gson();
        String gameJson = serializer.toJson(game.game());

        try (var conn = DatabaseManager.getConnection()) {
            if(!hasGame(game.gameID())) {
                try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)")) {
                    preparedStatement.setInt(1, game.gameID());
                    preparedStatement.setString(2, game.whiteUsername());
                    preparedStatement.setString(3, game.blackUsername());
                    preparedStatement.setString(4, game.gameName());
                    preparedStatement.setString(5, gameJson);


                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted == 0) {
                        throw new DataAccessException("Failed to insert data into table", 500);
                    }
                }
            }
            else {
                throw new DataAccessException("already taken", 403);
            }
        }
        catch (SQLException e) {
            // Print stack trace of the caught SQL exception
            e.printStackTrace();
            throw new DataAccessException("SQL error: " + e.getMessage(), 500);
        } catch (DataAccessException e) {
            // Print stack trace of any other caught exception
            e.printStackTrace();
            throw new DataAccessException(e.getMessage(), e.getCode());
        }

        return game.gameID();
    }

    public static boolean hasGame(int id) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT 1 FROM games WHERE gameID = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    public static Records.GameData getGame(int id) throws DataAccessException{
        Gson serializer = new Gson();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        int gameID = resultSet.getInt("gameID");
                        String whiteUsername = resultSet.getString("whiteUsername");
                        String blackUsername = resultSet.getString("blackUsername");
                        String gameName = resultSet.getString("gameName");
                        ChessGame game = serializer.fromJson(resultSet.getString("game"), ChessGame.class);
                        System.out.print(resultSet.getString("game").length());
                        return new Records.GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                    else {
                        throw new DataAccessException("game does not exist", 403);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    public static void setWhitePlayer(int id, String userName) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET whiteUsername = ? WHERE gameID = ?")) {
                preparedStatement.setString(1, userName);
                preparedStatement.setInt(2, id);
                int rowsEdited = preparedStatement.executeUpdate();
                if(rowsEdited != 1) {
                    throw new DataAccessException("Data not updated", 500);
                }
            }
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }
    public static void setBlackPlayer(int id, String userName) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET blackUsername = ? WHERE gameID = ?")) {
                preparedStatement.setString(1, userName);
                preparedStatement.setInt(2, id);
                int rowsEdited = preparedStatement.executeUpdate();
                if(rowsEdited != 1) {
                    throw new DataAccessException("Data not updated", 500);
                }
            }
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    public static HashSet<Records.GameData> getList() throws DataAccessException{
        Gson serializer = new Gson();
        HashSet<Records.GameData> gameList = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while(resultSet.next()) {
                        int gameID = resultSet.getInt("gameID");
                        String whiteUsername = resultSet.getString("whiteUsername");
                        String blackUsername = resultSet.getString("blackUsername");
                        String gameName = resultSet.getString("gameName");
                        ChessGame game = serializer.fromJson(resultSet.getString("game"), ChessGame.class);
                        gameList.add(new Records.GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
        return gameList;
    }

    public static void clearApplication() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE games")) {
                preparedStatement.executeUpdate();
            }
            catch(Exception e) {
                throw new DataAccessException("SQL error", 500);
            }
        }
        catch(Exception e) {
            throw new DataAccessException("Failed to connect to server", 500);
        }
    }
}
