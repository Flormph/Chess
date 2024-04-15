package ui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class Logout {
    public static int logout(int port) throws Exception{
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("authorization", Util.getToken());

        http.setDoOutput(true);

        http.connect();

        if(http.getResponseCode() == 200) {
            System.out.println("Logged out successfully!");
            Util.setToken(null);
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
        return http.getResponseCode();
    }
}
