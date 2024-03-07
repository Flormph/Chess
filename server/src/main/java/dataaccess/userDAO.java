package dataaccess;

import model.Records;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {

    public static String createUser(Records.UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            if(!hasUser(conn, user.username())) {
                try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
                    preparedStatement.setString(1, user.username());
                    preparedStatement.setString(2, user.password());
                    preparedStatement.setString(3, user.email());
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
        return user.username();
    }

    public static Records.UserData getUser(String username) throws DataAccessException {
        Records.UserData User;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT name, password, email FROM users WHERE name = ?")) {
                preparedStatement.setString(1, username);
                ResultSet result = preparedStatement.executeQuery();
                User = new Records.UserData(result.getString("name"), result.getString("password"), result.getString("email"));
            }
            catch(Exception e) {
                throw new DataAccessException("SQL error", 500);
            }
        }
        catch(Exception e) {
            throw new DataAccessException("Failed to connect to server", 500);
        }
        return null;
    }

    private static boolean hasUser(Connection conn, String username) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT 1 FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public static void clearApplication() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE users")) {
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
