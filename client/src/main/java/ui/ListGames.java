package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.Records;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;

public class ListGames {
    public static HashSet<Records.GameData> listGames(int port) throws Exception {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("authorization", Util.getToken());
        http.setDoOutput(true);

        http.connect();

        String responseBody;

        if(http.getResponseCode() == 200) {
            System.out.println("\u001b[1mList of games:\n");
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                JsonArray array = jsonObject.getAsJsonArray("games");
                Type setType = new TypeToken<HashSet<Records.GameData>>() {}.getType();
                return gson.fromJson(array, setType);
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
        return null;
    }
}
