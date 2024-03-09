package server.logout;

import dataAccess.DataAccessException;
import dataAccess.authDAO;

/**
 * ClearApplicationService - Clears the database. Removes all users, games, and authTokens.
 */
public class Service extends server.extenders.Service{
    /**
     * register - attempts to register a user with the given request and returns a success or failcase
     * @param request provided username, password, and email to create a user from
     * @return returns success response or a fail response
     */
    public Response logout(Request request) throws DataAccessException {
        if(request.token == null || request.token.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(!authDAO.hasAuth(request.token)) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        else {
            authDAO.deleteAuth(request.token);
            return new Response();
        }
    }
}
