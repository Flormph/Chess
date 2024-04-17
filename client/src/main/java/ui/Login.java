package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class Login {
    public static String login(String line, int port) throws Exception{
        String[] words = Util.convertWords(line);
        if(words.length == 3) {
            try {
                URI uri = new URI("http://localhost:" + port + "/session");
                HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
                http.setRequestMethod("POST");
                String username = words[1];
                String password = words[2];

                http.setDoOutput(true);
                var body = Map.of("username", username, "password", password);
                try (var outputStream = http.getOutputStream()) {
                    var jsonBody = new Gson().toJson(body);
                    outputStream.write(jsonBody.getBytes());
                }
                http.connect();


                if (http.getResponseCode() == 200) {
                    System.out.println("Logged in successfully!");
                    String responseBody;
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        Util.setToken(jsonObject.get("authToken").getAsString());
                    }
                    return Util.getToken();
                } else {
                    System.out.println("Login failed");
                    String responseBody;
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                        throw new Exception("Error: " + http.getResponseCode() + " " + responseBody);
                    }
                }
            }
            catch (Exception e) {
                throw e;
            }
        }
        else {
            System.out.println("Invalid command. Please try again.");
            return null;
        }
    }
}
