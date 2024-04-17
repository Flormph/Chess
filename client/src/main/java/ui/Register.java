package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import static ui.Util.*;

public class Register {
    public static String register(String line, int port) throws Exception {
        String[] words = convertWords(line);
        if(words.length == 4) {
            try {
                URI uri = new URI("http://localhost:" + port + "/user");
                HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
                http.setRequestMethod("POST");
                String username = words[1];
                String password = words[2];
                String email = words[3];

                http.setDoOutput(true);
                var body = Map.of("username", username, "password", password, "email", email);
                try (var outputStream = http.getOutputStream()) {
                    var jsonBody = new Gson().toJson(body);
                    outputStream.write(jsonBody.getBytes());
                }
                http.connect();


                String responseBody;
                if (http.getResponseCode() == 200) {
                    System.out.println("Registered and logged in successfully!");
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        String token = jsonObject.get("authToken").getAsString();
                        Util.setToken(token);
                        return token;
                    }
                } else {
                    System.out.println("Failed to register user");
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                        responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                        System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                    }
                }
                return null;
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
        return null;
    }
}
