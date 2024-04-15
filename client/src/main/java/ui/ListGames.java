package ui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ListGames {
    public static int listGames(int port) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        System.out.println("Game created successfully!");
        http.setRequestMethod("GET");
        http.setRequestProperty("authorization", Util.getToken());
        http.setDoOutput(true);

        http.connect();

        String responseBody;

        if(http.getResponseCode() == 200) {
            System.out.println("List of games:");
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                System.out.println(responseBody);
            }
        }
        else {
            System.out.println("Failed to retrieve list of games:");
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
            }
        }
        return http.getResponseCode();
    }
}
