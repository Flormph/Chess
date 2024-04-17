package dataAccess;

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
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
        Records.AuthData auth = new Records.AuthData(UUID.randomUUID().toString(), username);
        while(hasAuth(auth.authToken())) {
            auth = new Records.AuthData(UUID.randomUUID().toString(), username);
        }
        //if (!username.isEmpty()) {
            try (var conn = DatabaseManager.getConnection()) {
                    try (var preparedStatement = conn.prepareStatement("INSERT INTO tokens (auth, username) VALUES (?, ?)")) {
                        preparedStatement.setString(1, auth.authToken());
                        preparedStatement.setString(2, auth.username());
                        int rowsInserted = preparedStatement.executeUpdate();
                        if (rowsInserted == 0) {
                            throw new DataAccessException("ERROR: Failed to insert data into table", 500);
                        }
                    }
            } catch (SQLException e) {
                // Print stack trace of the caught SQL exception
                e.printStackTrace();
                throw new DataAccessException("SQL error: " + e.getMessage(), 500);
            } catch (Exception e) {
                // Print stack trace of any other caught exception
                e.printStackTrace();
                throw new DataAccessException("Failed to connect to server", 500);
            }
        //}
        //else {
        //    throw new DataAccessException("Error: user doesn't exist", 401);
        //}
        return auth.authToken();
    }

    public static void deleteAuth(String auth) throws DataAccessException{
        Records.AuthData Auth;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM tokens WHERE auth = ?;")) {
                preparedStatement.setString(1, auth);
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

    public static String getAuth(String auth) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM tokens WHERE auth = ?")) {
                preparedStatement.setString(1, auth);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        return resultSet.getString("username");
                    }
                    else {
                        return "NOT-LOGGED-USER";
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), 401);
        }
    }

    /**
     *
     * @param username a username to look for in the tokens
     * @return whether or not the provided token is present in the database
     */
    public static boolean hasUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT 1 FROM tokens WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    public static boolean hasAuth(String auth) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM tokens WHERE auth = ?")) {
                preparedStatement.setString(1, auth);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count != 0;
                    }
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
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
