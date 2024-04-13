package ui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class Logout {
    public static void logout(int port) throws Exception{
        Util util = Util.getInstance();
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("authToken", util.getToken());

        http.setDoOutput(true);

        http.connect();

        if(http.getResponseCode() == 200) {
            System.out.println("Logged out successfully!");
            util.setToken(null);
            Ui.displayPreLoginUI();
        }
        else {
            String responseBody;
            System.out.println("Failed to logout");
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
            }
        }
    }
}
