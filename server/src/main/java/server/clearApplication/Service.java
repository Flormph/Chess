package server.clearApplication;

import dataAccess.DataAccessException;

/**
 * ClearApplicationService - Clears the database. Removes all users, games, and authTokens.
 */
public class Service extends server.extenders.Service{
    /**
     * clearApplication - attempts to clear database with given request and returns a clearApplication response
     * @param request - A ClearApplicationRequest object containing information for the request.
     * @return ClearApplicationResponse return of either success or failure
     */
    public server.clearApplication.Response clearApplication(Request request) throws DataAccessException {
        database.clearApplication();
        return new Response();
    }
}
