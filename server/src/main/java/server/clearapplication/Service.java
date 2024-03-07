package server.clearapplication;

import dataaccess.DataAccessException;
import dataaccess.*;
import server.database.Database;

/**
 * ClearApplicationService - Clears the database. Removes all users, games, and authTokens.
 */
public class Service extends server.extenders.Service{
    /**
     * clearapplication - attempts to clear database with given request and returns a clearapplication response
     * @param request - A ClearApplicationRequest object containing information for the request.
     * @return ClearApplicationResponse return of either success or failure
     */
    public static server.clearapplication.Response clearApplication(Request request) throws DataAccessException {
        try {
            userDAO.clearApplication();
            authDAO.clearApplication();
            gameDAO.clearApplication();
        }
        catch (DataAccessException e) {
            return new Response(e.getMessage());
        }
        DatabaseManager.createTables();
        return new Response();
    }
}
