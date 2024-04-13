package ui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import static ui.Util.*;

public class Register {
    public static int register(String line, int port) throws Exception {
        Util util = getInstance();
        String[] words = convertWords(line);
        if(words.length == 4) {
            URI uri = new URI("http://localhost:" + port + "/user");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            String username = words[1];
            String password = words[2];
            String email = words[3];

            http.setDoOutput(true);
            var body = Map.of("username", username, "password", password,"email",email);
            try(var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();


            if(http.getResponseCode() == 200) {
                System.out.println("Registered and logged in successfully!");
                setToken(http.getHeaderField("authToken"));
                Ui.displayPostLoginUI();
            }
            else {
                System.out.println("Failed to register user");
                String responseBody;
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                }
            }
            return http.getResponseCode();
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
        return 500;
    }
}
