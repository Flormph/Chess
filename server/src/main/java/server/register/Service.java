package server.register;

import dataAccess.*;
import model.Records;
import server.database.Database;

/**
 * ClearApplicationService - Clears the database. Removes all users, games, and authTokens.
 */
public class Service extends server.extenders.Service{
    /**
     * register - attempts to register a user with the given request and returns a success or failcase
     * @param request provided username, password, and email to create a user from
     * @return returns success response or a fail response
     */
    public server.register.Response register(Request request) throws DataAccessException {
        if(request.username == null || request.username.isEmpty() || request.email == null || request.email.isEmpty() || request.password == null || request.password.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(database.users.isEmpty() || database.usersContains(request.username)) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else {
            Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
            userDAO.createUser(newUser);
            return new Response(0, authDAO.createAuth(request.username));
        }
    }
}
