package server.creategame;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.authDAO;
import dataAccess.gameDAO;
import model.Records;

import java.util.UUID;

/**
 * ClearApplicationService - Clears the database. Removes all users, games, and authTokens.
 */
public class Service extends server.extenders.Service{
    /**
     * register - attempts to register a user with the given request and returns a success or failcase
     * @param request provided username, password, and email to create a user from
     * @return returns success response or a fail response
     */
    public static Response createGame(Request request) throws DataAccessException {
        if(request.gameName == null || request.gameName.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(!authDAO.hasAuth(request.auth)) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        else {
            int gameID = UUID.randomUUID().hashCode();
            if(gameID < 0) { gameID *= -1; }
            Records.GameData game = new Records.GameData(gameID, null, null, request.gameName, new ChessGame());
            return new Response(gameDAO.createGame(game));
        }
    }
}
