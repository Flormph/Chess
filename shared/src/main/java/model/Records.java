package model;

import chess.ChessGame;

import java.util.UUID;

public class Records {
    public record UserData(String username, String password, String email) {

    }
    public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        @Override
        public String toString() {
            return "{\"gameID\": " +
                    gameID +
                    ", \"whiteUsername\":\"" +
                    whiteUsername +
                    "\", \"blackUsername\":\"" +
                    blackUsername +
                    "\", \"gameName:\"" +
                    gameName +
                    "\"}";
        }
    }

    public record AuthData(String authToken, String username) {
    }

}


