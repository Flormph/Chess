package dataaccess;

import model.Records;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class authDAO {
    /**
     *
     * @param username name of the user to create the authToken around (should only be called during registration or login
     * @return Returns the token associated with the newly logged-in user
     */
    public static String createAuth(String username) throws DataAccessException{
        String token = null;
        do { //this loop grabs a new token if the generated one is already in the server
            token = UUID.randomUUID().toString();
        }
        while (server.database.Database.getInstance().hasToken(token));

        Records.AuthData auth = new Records.AuthData(UUID.randomUUID().toString(), username);
        try (var conn = DatabaseManager.getConnection()) {
            if(!hasAuth(conn, username)) {
                try (var preparedStatement = conn.prepareStatement("INSERT INTO tokens (auth, username) VALUES (?, ?)")) {
                    preparedStatement.setString(1, auth.authToken());
                    preparedStatement.setString(2, auth.username());
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted == 0) {
                        throw new DataAccessException("Failed to insert data into table", 500);
                    }
                }
            }
            else {
                throw new DataAccessException("already logged in", 403);
            }
        }
        catch (SQLException e) {
            // Print stack trace of the caught SQL exception
            e.printStackTrace();
            throw new DataAccessException("SQL error: " + e.getMessage(), 500);
        } catch (Exception e) {
            // Print stack trace of any other caught exception
            e.printStackTrace();
            throw new DataAccessException("Failed to connect to server", 500);
        }
        return auth.authToken();
    }

    public static void deleteAuth(String auth) {
        server.database.Database.getInstance().deleteToken(auth);
    }

    public static String getAuth(String auth) {
        return server.database.Database.getInstance().getToken(auth);
    }

    /**
     *
     * @param conn a sql Connection
     * @return whether or not the provided token is present in the database
     */
    private static boolean hasAuth(Connection conn, String username) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT 1 FROM tokens WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public static void clearApplication() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE tokens")) {
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
