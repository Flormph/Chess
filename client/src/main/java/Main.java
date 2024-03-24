import chess.*;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;

    public static void main(String[] args) {
        System.out.println("Welcome to 240 Chess. Type \"help\" to get started");
        displayPreloginUI();
    }

    private static void displayPreloginUI() {
        while (true) {
            if(!isLoggedIn) {
                System.out.print("[LOGGED_OUT] >>> ");
            }
            else {
                displayPostloginUI();
            }
            String command = scanner.nextLine().toLowerCase();
            switch (command) {
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

    private static void displayPostloginUI() {
        while (isLoggedIn) {
            System.out.print("[LOGGED_IN] >>> ");
            String command = scanner.nextLine().toLowerCase();
            switch (command) {
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
            System.out.println("login <USERNAME> <PASSWORD>");
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

    private static void login(String line) {
        if(countWordsInLine(line) == 3) {
            scanner.next();
            String username = scanner.next();
            String password = scanner.next();
            // Implementation of login functionality with server API
            System.out.println("Logged in successfully!");
            isLoggedIn = true;
            displayPostloginUI();
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void logout() {
        // Implementation of login functionality with server API
        System.out.println("Logged out successfully!");
        isLoggedIn = false;
        displayPreloginUI();
    }

    private static void quit() {
        // logout api here if logged in
        System.exit(0);
    }

    private static void register(String line) {
        if(countWordsInLine(line) == 4) {
            scanner.next();
            String username = scanner.next();
            String password = scanner.next();
            String email = scanner.next();
            // Implementation of registration functionality with server API
            System.out.println("Registered and logged in successfully!");
            isLoggedIn = true;
            displayPostloginUI();
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void createGame(String line) {
        if(countWordsInLine(line) == 2) {
            scanner.next();
            String gameName = scanner.next();
            // Implementation of creating a game with server API
            System.out.println("Game created successfully!");
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void listGames() {
        // Implementation of listing games from server API
        System.out.println("List of games:");
        // Display list of games retrieved from server
    }

    private static void joinGame(String line) {
        if(countWordsInLine(line) == 3) {
            scanner.next();
            String ID = scanner.next();
            String team = scanner.next();
            // Implementation of joining a game with server API
            System.out.println("Joined game successfully!");
        }
        else if(countWordsInLine(line) == 2) {
            scanner.next();
            String ID = scanner.next();
            // Implementation of joining a game with server API
            System.out.println("Joined game successfully!");
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static void joinObserver(String line) {
        if(countWordsInLine(line) == 2) {
            scanner.next();
            String ID = scanner.next();
            // Implementation of joining game as an observer with server API
            System.out.println("Joined game as observer successfully!");
        }
        else {
            System.out.println("Invalid command. Please try again.");
        }
    }

    private static int countWordsInLine(String line) {
        if (line == null || line.isEmpty()) {
            return 0;
        }

        // Split the line into words using whitespace as delimiter
        String[] words = line.trim().split("\\s+");

        // Return the number of words
        return words.length;
    }
}
