package server.login;

import dataAccess.DataAccessException;
import dataAccess.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(request.username == null || request.username.isEmpty() || request.password == null || request.password.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(!userDAO.hasUser(request.username)) {
            throw new DataAccessException("Error: username or password incorrect", 401);
        }
        else if(!encoder.matches(request.password, userDAO.getUser(request.username).password())) {
            throw new DataAccessException("Error: username or password incorrect", 401);
        }
        else {
            return new server.login.Response(request.username, authDAO.createAuth(request.username));
        }
    }
}
