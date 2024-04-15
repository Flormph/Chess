package server.register;

import dataAccess.*;
import model.Records;

/**
 * ClearApplicationService - Clears the database. Removes all users, games, and authTokens.
 */
public class Service extends server.extenders.Service{
    /**
     * register - attempts to register a user with the given request and returns a success or failcase
     * @param request provided username, password, and email to create a user from
     * @return returns success response or a fail response
     */
    public static server.register.Response register(Request request) throws DataAccessException {
        if(request.username == null || request.username.isEmpty() || request.email == null || request.email.isEmpty() || request.password == null || request.password.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(userDAO.hasUser(request.username)) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else {
            try {
                Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
                return new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));
            }
            catch(DataAccessException e) {
                throw new DataAccessException(e.getMessage(), e.getCode());
            }
        }
    }
}
