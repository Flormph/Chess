package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import static ui.Util.convertWords;

public class JoinGame {
    public static int joinGame(String line, int port) throws Exception {
        String[] words = convertWords(line);
        if(words.length == 3) {
            try {
                URI uri = new URI("http://localhost:" + port + "/game");
                HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
                http.setRequestMethod("PUT");
                http.setRequestProperty("authorization", Util.getToken());
                http.setDoOutput(true);

                String ID = words[1];
                ChessGame.TeamColor team = ChessGame.TeamColor.valueOf(words[2]);

                var body = Map.of("playerColor", team, "gameID", ID);
                try (var outputStream = http.getOutputStream()) {
                    var jsonBody = new Gson().toJson(body);
                    outputStream.write(jsonBody.getBytes());
                }

                http.connect();

                String responseBody;

                if (http.getResponseCode() == 200) {
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        Records.GameData game = new Gson().fromJson(inputStreamReader, Records.GameData.class);
                        Util.setGame(game);
                        System.out.println("Joined game successfully!");
                    }
                    catch(Exception e) {
                        System.out.print(e.getMessage());
                    }
                } else {
                    System.out.println("Failed to join game:");
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                        System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                    }
                }
                return http.getResponseCode();
            }
            catch(Exception e) {
                System.out.print(e.getMessage());
            }
        }
        if(words.length == 2) {
            try {
                URI uri = new URI("http://localhost:" + port + "/game");
                HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
                http.setRequestMethod("PUT");
                http.setRequestProperty("authorization", Util.getToken());
                http.setDoOutput(true);

                String ID = words[1];

                var body = Map.of("playerColor", "OBSERVER", "gameID", ID);
                try (var outputStream = http.getOutputStream()) {
                    var jsonBody = new Gson().toJson(body);
                    outputStream.write(jsonBody.getBytes());
                }

                http.connect();

                String responseBody;

                if (http.getResponseCode() == 200) {
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        Records.GameData game = new Gson().fromJson(inputStreamReader, Records.GameData.class);
                        Util.setGame(game);
                        System.out.println("Joined game successfully!");
                    }
                    catch(Exception e) {
                        System.out.print(e.getMessage());
                    }
                } else {
                    System.out.println("Failed to join game:");
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                        System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                    }
                }
                return http.getResponseCode();
            }
            catch(Exception e) {
                System.out.print(e.getMessage());
            }
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
        return 0;
    }
}
