import chess.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;

    private static String loginToken = null;

    public Main() throws URISyntaxException, IOException {
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Welcome to 240 Chess. Type \"help\" to get started");
        displayPreloginUI();
    }

    public static void setToken(String token) {
        loginToken = token;
    }

    private static void displayPreloginUI() throws Exception{
        while (true) {
            if(!isLoggedIn) {
                System.out.print("[LOGGED_OUT] >>> ");
            }
            else {
                displayPostloginUI();
            }
            String command = scanner.nextLine().toLowerCase();
            String[] words = command.trim().split("\\s+");
            switch (words[0]) {
                case "help":
                    displayHelp();
                    break;
                case "quit":
                    quit();
                    System.exit(0);
                    break;
                case "login":
                    login(command);
                    break;
                case "register":
                    register(command);
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private static void displayPostloginUI() throws Exception{
        while (isLoggedIn) {
            System.out.print("[LOGGED_IN] >>> ");
            String command = scanner.nextLine().toLowerCase();
            String[] words = command.trim().split("\\s");
            switch (words[0]) {
                case "help":
                    displayHelp();
                    break;
                case "logout":
                    logout();
                    isLoggedIn = false;
                    displayPreloginUI();
                    break;
                case "create":
                    createGame(command);
                    break;
                case "list":
                    listGames();
                    break;
                case "join":
                    joinGame(command);
                    break;
                case "observe":
                    joinObserver(command);
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private static void displayHelp() {
        if(!isLoggedIn) {
            System.out.print("\u001b[35;40;m");
            System.out.print("register <USERNAME> <PASSWORD> <EMAIL>");
            System.out.print("- to create an account\n");
            System.out.print("\u001b[35;40;m");
            System.out.print("login <USERNAME> <PASSWORD>-");
            System.out.print("to play chess\n");
        }
        else {
            System.out.println("create <NAME> - a game");
            System.out.println("list - games");
            System.out.println("join <ID> [WHITE|BLACK|<empty>] - a game");
            System.out.println("ovbserve <ID> - a game");
            System.out.println("logout - when you are done");
        }
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    private static void login(String line) throws Exception{
        String[] words = convertWords(line);
        if(words.length == 3) {
            URI uri = new URI("http://localhost:8080/session");
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
                displayPostloginUI();
            }
            else {
                String responseBody = null;
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                }
                System.out.println("Login failed");
            }
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void logout() throws Exception{
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        http.setDoOutput(true);

        http.connect();

        if(http.getResponseCode() == 200) {
            System.out.println("Logged out successfully!");
            setToken(null);
            isLoggedIn = false;
            displayPreloginUI();
        }
        else {
            String responseBody = null;
            System.out.println("Failed to logout");
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
            }
        }
    }

    private static void quit() {
        // logout api here if logged in
        System.exit(0);
    }

    private static void register(String line) throws Exception {
        String[] words = convertWords(line);
        if(words.length == 4) {
            URI uri = new URI("http://localhost:8080/user");
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
                isLoggedIn = true;
                setToken(http.getHeaderField("authToken"));
                displayPostloginUI();
            }
            else {
                String responseBody = null;
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                }
                System.out.println("Registration failed");
            }
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void createGame(String line) {
        String[] words = convertWords(line);
        if(words.length == 2) {
            String gameName = words[1];
            // Implementation of creating a game with server API
            System.out.println("Game created successfully!");
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void listGames() {
        System.out.println("List of games:");
        // Display list of games retrieved from server
    }

    private static void joinGame(String line) {
        String[] words = convertWords(line);
        if(words.length == 3) {
            String ID = words[1];
            String team = words[2];
            // Implementation of joining a game with server API
            System.out.println("Joined game successfully!");
        }
        else if(words.length == 2) {
            String ID = words[1];
            // Implementation of joining a game with server API
            System.out.println("Joined game successfully!");
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void joinObserver(String line) {
        String[] words = convertWords(line);
        if(words.length == 2) {
            String ID = words[1];
            // Implementation of joining game as an observer with server API
            System.out.println("Joined game as observer successfully!");
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static String[] convertWords(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        return line.trim().split("\\s+");
    }
}
