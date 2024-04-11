package ui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class Login {
    private static void login(String line) throws Exception{
        String[] words = Util.convertWords(line);
        if(words.length == 3) {
            URI uri = new URI("http://localhost:" + port + "/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            String username = words[1];
            String password = words[2];

            http.setDoOutput(true);
            var body = Map.of("username", username, "password", password);
            try(var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();


            if(http.getResponseCode() == 200) {
                System.out.println("Logged in successfully!");
                isLoggedIn = true;
                setToken(http.getHeaderField("authToken"));
                displayPostLoginUI();
            }
            else {
                System.out.println("Login failed");
                String responseBody;
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
