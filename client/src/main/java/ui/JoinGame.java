package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.Records;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import static ui.Util.convertWords;

public class JoinGame {
    public static Records.GameData joinGame(String line, int port) {
        String[] words = convertWords(line);
        try {
            URI uri = new URI("http://localhost:" + port + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("PUT");
            http.setRequestProperty("authorization", Util.getToken());
            http.setDoOutput(true);

            String ID = words[1];
            Map<String, ? extends java.lang.constant.Constable> body;

            if (words.length == 3) {
                ChessGame.TeamColor team = ChessGame.TeamColor.valueOf(words[2]);
                body = Map.of("playerColor", team, "gameID", ID);
            } else {
                body = Map.of("playerColor", "OBSERVER", "gameID", ID);
            }
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            http.connect();

            String responseBody;

            if (http.getResponseCode() == 200) {
                try (InputStream respBody = http.getInputStream()) {
                    Gson gson = new Gson();
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    Records.GameData game = gson.fromJson(jsonObject.get("gameData"), Records.GameData.class);
                    Util.setGame(game);
                    System.out.println("Joined game successfully!");
                    return game;
                } catch (Exception e) {
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
            return null;
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return null;
    }
}
