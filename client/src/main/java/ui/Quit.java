package ui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.Scanner;

import static ui.Util.*;

public class Quit {
    static void quit(int port) throws Exception {
        Util util = getInstance();
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("authToken", getToken());
        http.setDoOutput(true);

        http.connect();

        if(http.getResponseCode() == 200) {
            System.out.println("Logged out successfully!");
            setToken(null);
        }
        else {
            String responseBody;
            System.out.println("Failed to logout");
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
            }
            System.out.println("quit anyway? (y/n)");
            if(new Scanner(System.in).next().equalsIgnoreCase("n")) {
                return;
            }
            else {
                System.exit(0);
            }
        }
        setToken(null);
        System.exit(0);
    }
}
