package server.join;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.authDAO;
import dataAccess.gameDAO;
import model.Records;

import java.util.Objects;
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
    public Response joinGame(Request request) throws DataAccessException {
        String username = authDAO.getAuth(request.auth);
        int ID = Integer.parseInt(request.gameID);
        if(request.gameID == null || request.gameID.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(!authDAO.hasAuth(request.auth)) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        else if(!gameDAO.findGame(Integer.parseInt(request.gameID))) {
            throw new DataAccessException("Error: game doesn't exist, 500");
        }
        else if(Objects.equals(request.playerColor, "WHITE") && (gameDAO.getGame(Integer.parseInt(request.gameID))).whiteUsername() != null) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else if(Objects.equals(request.playerColor, "BLACK") && (gameDAO.getGame(Integer.parseInt(request.gameID))).blackUsername() != null) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else {
            if(Objects.equals(request.playerColor, "WHITE")) {
                gameDAO.setWhitePlayer(ID, username);
                return new Response();
            }
            else if(Objects.equals(request.playerColor, "BLACK")) {
                gameDAO.setBlackPlayer(ID, username);
                return new Response();
            }
            else {
                gameDAO.addObserver(ID, username);
                return new Response();
            }
        }
    }
}
