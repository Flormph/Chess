package ui;

import model.Records;

import java.util.Objects;
import java.util.Scanner;

public class Ui {
    private static final Scanner scanner = new Scanner(System.in);

    public static void displayPreLoginUI() throws Exception{
        while (Util.getToken() == null) {
            System.out.print("[LOGGED_OUT] >>> ");
            String command = scanner.nextLine();
            String[] words = command.trim().split("\\s+");
            switch (words[0].toLowerCase()) {
                case "help":
                    displayHelp();
                    displayPreLoginUI();
                    break;
                case "quit":
                    Quit.quit(Util.getPort());
                    System.exit(0);
                    break;
                case "login":
                    Login.login(command, Util.getPort());
                    displayPostLoginUI();
                    break;
                case "register":
                    Register.register(command, Util.getPort());
                    displayPostLoginUI();
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    static void displayPostLoginUI() throws Exception{
        Records.GameData game;
        Records.GameData tempgame;
        while (Util.getToken() != null) {
            System.out.print("[LOGGED_IN] >>> ");
            String command = scanner.nextLine();
            String[] words = command.trim().split("\\s");
            switch (words[0].toLowerCase()) {
                case "help":
                    displayHelp();
                    displayPostLoginUI();
                    break;
                case "logout":
                    Logout.logout(Util.getPort());
                    Util.setToken(null);
                    displayPreLoginUI();
                    break;
                case "create":
                    CreateGame.createGame(command, Util.getPort());
                    Util.setBoard();
                    displayPostLoginUI();
                    break;
                case "list":
                    for(Records.GameData l : Objects.requireNonNull(ListGames.listGames(Util.getPort()))) {
                        System.out.print(l.toString());
                    }
                    displayPostLoginUI();
                    break;
                case "join", "observe":
                    game = JoinGame.joinGame(command, Util.getPort());
                    displayGame(game);
                    displayPostLoginUI();
                    break;
                case "quit":
                    Quit.quit(Util.getPort());
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }
    private static void displayHelp() {
        if(Util.getToken() == null) {
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

    private static void displayGame(Records.GameData game) {
        if(game == null) {
            System.out.println("No active game");
        }
        else {
            System.out.println(game.game());
        }
    }
}
