package server.join;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.authDAO;
import dataaccess.gameDAO;

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
    public static Response joinGame(Request request) throws DataAccessException {
        ChessGame.TeamColor team;
        if(Objects.equals(request.playerColor, ChessGame.TeamColor.WHITE)) {
            team = ChessGame.TeamColor.WHITE;
        }
        else if(Objects.equals(request.playerColor, ChessGame.TeamColor.BLACK)) {
            team = ChessGame.TeamColor.BLACK;
        }
        else {
            team = null;
        }
        String username = authDAO.getAuth(request.auth);
        int ID = Integer.parseInt(request.gameID);
        if(request.gameID == null || request.gameID.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        else if(!authDAO.hasAuth(request.auth)) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        else if(!gameDAO.findGame(Integer.parseInt(request.gameID))) {
            throw new DataAccessException("Error: game doesn't exist", 400);
        }
        else if(Objects.equals(team, ChessGame.TeamColor.WHITE) && !Objects.equals((gameDAO.getGame(Integer.parseInt(request.gameID))).whiteUsername(), null)) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else if(Objects.equals(team, ChessGame.TeamColor.BLACK) && !Objects.equals((gameDAO.getGame(Integer.parseInt(request.gameID))).blackUsername(), null)) {
            throw new DataAccessException("Error: already taken", 403);
        }
        else {
            if(Objects.equals(request.playerColor, ChessGame.TeamColor.WHITE)) {
                gameDAO.setWhitePlayer(ID, username);
                return new Response();
            }
            else if(Objects.equals(request.playerColor, ChessGame.TeamColor.BLACK)) {
                gameDAO.setBlackPlayer(ID, username);
                return new Response();
            }
            else {
                return new Response();
            }
        }
    }
}
