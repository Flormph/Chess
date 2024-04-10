import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;

    private static String loginToken = null;
    private static String getToken() {
        return loginToken;
    }

    public Main() {
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Welcome to 240 Chess. Type \"help\" to get started");
        displayPreLoginUI();
    }

    public static void setToken(String token) {
        loginToken = token;
    }

    private static void displayPreLoginUI() throws Exception{
        while (true) {
            if(!isLoggedIn) {
                System.out.print("[LOGGED_OUT] >>> ");
            }
            else {
                displayPostLoginUI();
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

    private static void displayPostLoginUI() throws Exception{
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
                    displayPreLoginUI();
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
            System.out.println("observe <ID> - a game");
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

    private static void logout() throws Exception{
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("authToken", getToken());

        http.setDoOutput(true);

        http.connect();

        if(http.getResponseCode() == 200) {
            System.out.println("Logged out successfully!");
            setToken(null);
            isLoggedIn = false;
            displayPreLoginUI();
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

    private static void quit() throws Exception {
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("authToken", getToken());
        http.setDoOutput(true);

        http.connect();

        if(http.getResponseCode() == 200) {
            System.out.println("Logged out successfully!");
            setToken(null);
            isLoggedIn = false;
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
                displayPostLoginUI();
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
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void createGame(String line) throws Exception{
        String[] words = convertWords(line);
        if(words.length == 2) {
            URI uri = new URI("http://localhost:8080/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            System.out.println("Game created successfully!");
            http.setRequestMethod("POST");
            http.setRequestProperty("authToken", getToken());
            String gameName = words[1];

            http.setDoOutput(true);
            var body = Map.of("gameName", gameName);
            try(var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
            http.connect();

            String responseBody;

            if(http.getResponseCode() == 200) {
                System.out.println("Game successfully created");
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println(responseBody);
                }
            }
            else {
                System.out.println("Failed to create game");
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

    private static void listGames() throws Exception {
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        System.out.println("Game created successfully!");
        http.setRequestMethod("POST");
        http.setRequestProperty("authToken", getToken());
        http.setDoOutput(true);

        http.connect();

        String responseBody;

        if(http.getResponseCode() == 200) {
            System.out.println("List of games:");
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                System.out.println(responseBody);
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
    }

    private static void joinGame(String line) throws Exception {
        String[] words = convertWords(line);
        if(words.length == 3) {
            URI uri = new URI("http://localhost:8080/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            System.out.println("Game created successfully!");
            http.setRequestMethod("POST");
            http.setRequestProperty("authToken", getToken());
            http.setDoOutput(true);

            String ID = words[1];
            String team = words[2];

            var body = Map.of("playerColor", team, "gameID", ID);
            try(var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            http.connect();

            String responseBody;

            if(http.getResponseCode() == 200) {
                System.out.println("Joined game successfully!");
            }
            else {
                System.out.println("Failed to join game:");
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                }
            }
            displayPostLoginUI();
        }
        if(words.length == 2) {
            URI uri = new URI("http://localhost:8080/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            System.out.println("Game created successfully!");
            http.setRequestMethod("POST");
            http.setRequestProperty("authToken", getToken());
            http.setDoOutput(true);

            String ID = words[1];

            var body = Map.of("gameID", ID);
            try(var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            http.connect();

            String responseBody;

            if(http.getResponseCode() == 200) {
                System.out.println("Joined game successfully!");
            }
            else {
                System.out.println("Failed to join game:");
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    responseBody = new Gson().fromJson(inputStreamReader, Map.class).toString();
                    System.out.println("Error: " + http.getResponseCode() + " " + responseBody);
                }
            }
            displayPostLoginUI();
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void joinObserver(String line) throws Exception {
        joinGame(line);
    }

    private static String[] convertWords(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        return line.trim().split("\\s+");
    }
}
