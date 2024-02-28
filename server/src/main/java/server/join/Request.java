package server.join;

import chess.ChessGame;

public class Request {
    public ChessGame.TeamColor playerColor;
    public String gameID;
    public String auth;
    public Request(ChessGame.TeamColor playerColor, String gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
}
