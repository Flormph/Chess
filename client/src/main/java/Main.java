import chess.*;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;

    public static void main(String[] args) {
        displayPreloginUI();
    }

    private static void displayPreloginUI() {
        while (true) {
            System.out.println("Prelogin UI");
            System.out.println("Commands:");
            System.out.println("1. Help");
            System.out.println("2. Quit");
            System.out.println("3. Login");
            System.out.println("4. Register");

            String command = scanner.nextLine().toLowerCase();
            switch (command) {
                case "help":
                    displayHelp();
                    break;
                case "quit":
                    System.exit(0);
                    break;
                case "login":
                    login();
                    break;
                case "register":
                    register();
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private static void displayPostloginUI() {
        while (true) {
            System.out.println("Postlogin UI");
            System.out.println("Commands:");
            System.out.println("1. Help");
            System.out.println("2. Logout");
            System.out.println("3. Create Game");
            System.out.println("4. List Games");
            System.out.println("5. Join Game");
            System.out.println("6. Join Observer");

            String command = scanner.nextLine().toLowerCase();
            switch (command) {
                case "help":
                    displayHelp();
                    break;
                case "logout":
                    isLoggedIn = false;
                    displayPreloginUI();
                    break;
                case "create game":
                    createGame();
                    break;
                case "list games":
                    listGames();
                    break;
                case "join game":
                    joinGame();
                    break;
                case "join observer":
                    joinObserver();
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private static void displayHelp() {
        System.out.println("Help - Displays text informing the user what actions they can take.");
    }

    private static void login() {
        // Implementation of login functionality with server API
        System.out.println("Logged in successfully!");
        isLoggedIn = true;
        displayPostloginUI();
    }

    private static void register() {
        // Implementation of registration functionality with server API
        System.out.println("Registered and logged in successfully!");
        isLoggedIn = true;
        displayPostloginUI();
    }

    private static void createGame() {
        // Implementation of creating a game with server API
        System.out.println("Game created successfully!");
    }

    private static void listGames() {
        // Implementation of listing games from server API
        System.out.println("List of games:");
        // Display list of games retrieved from server
    }

    private static void joinGame() {
        // Implementation of joining a game with server API
        System.out.println("Joined game successfully!");
    }

    private static void joinObserver() {
        // Implementation of joining game as an observer with server API
        System.out.println("Joined game as observer successfully!");
    }
}
