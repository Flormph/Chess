package model;

import chess.ChessGame;

import java.util.UUID;

public class Records {
    public record UserData(String username, String password, String email) {

    }
    public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        @Override
        public String toString() {
            String out = "";
            out += "\u001b[1m";
            out += "Game Name:\t\t" + gameName + "\n";
            out += "Game ID:\t\t" + gameID + "\n";
            out += "White player:\t" + whiteUsername + "\n";
            out += "Black player:\t" + blackUsername + "\n";
            out += game.toString() + "\n";
            return out;
        }
    }

    public record AuthData(String authToken, String username) {
    }

}


