package ui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import static ui.Util.convertWords;

public class CreateGame {
    static void createGame(String line, int port) throws Exception{
        Util util = Util.getInstance();
        String[] words = convertWords(line);
        if(words.length == 2) {
            URI uri = new URI("http://localhost:" + port + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            System.out.println("Game created successfully!");
            http.setRequestMethod("POST");
            http.setRequestProperty("authToken", util.getToken());
            String gameName = words[1];

            http.setDoOutput(true);
            var body = Map.of("gameName", gameName);
            try(var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();

            String responseBody;

            if(http.getResponseCode() == 200) {
                System.out.println("Game successfully created");
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println(responseBody);
                }
            }
            else {
                System.out.println("Failed to create game");
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                }
            }
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }
}
