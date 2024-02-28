package server.login;

import dataAccess.DataAccessException;
import dataAccess.authDAO;
import dataAccess.userDAO;
import model.Records;

import java.util.Objects;

/**
 * ClearApplicationService - Clears the database. Removes all users, games, and authTokens.
 */
public class Service extends server.extenders.Service{
    /**
     * register - attempts to register a user with the given request and returns a success or failcase
     * @param request provided username, password, and email to create a user from
     * @return returns success response or a fail response
     */
    public static Response login(Request request) throws DataAccessException {
        database = server.database.Database.getInstance();
        if(request.username == null || request.username.isEmpty() || request.password == null || request.password.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(!database.usersContains(request.username)) {
            throw new DataAccessException("Error: username or password incorrect", 401);
        }
        else if(!Objects.equals(database.getUser(request.username).password(), request.password)) {
            throw new DataAccessException("Error: username or password incorrect", 401);
        }
        else {
            return new server.login.Response(request.username, authDAO.createAuth(request.username));
        }
    }
}
