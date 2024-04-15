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
    public static int quit(int port) throws Exception {
        if(getToken() == null) {
            return 200;
        }

        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("authorization", getToken());
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
                return 0;
            }
            else {
                return http.getResponseCode();
            }
        }
        return http.getResponseCode();
    }
}
