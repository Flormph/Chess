package ui;

import java.util.Scanner;

public class Ui {
    public static Ui ui;
    private static final Scanner scanner = new Scanner(System.in);


    public static void displayPreLoginUI() throws Exception{
        while (true) {
            if(Util.getToken() == null) {
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
        while (Util.getToken() != null) {
            System.out.print("[LOGGED_IN] >>> ");
            String command = scanner.nextLine().toLowerCase();
            String[] words = command.trim().split("\\s");
            switch (words[0]) {
                case "help":
                    displayHelp();
                    displayPostLoginUI();
                    break;
                case "logout":
                    Logout.logout(Util.getPort());
                    displayPreLoginUI();
                    break;
                case "create":
                    CreateGame.createGame(command, Util.getPort());
                    displayPostLoginUI();

                    break;
                case "list":
                    ListGames.listGames(Util.getPort());
                    displayPostLoginUI();
                    break;
                case "join":
                    JoinGame.joinGame(command, Util.getPort());
                    displayPostLoginUI();
                    break;
                case "observe":
                    JoinObserver.joinObserver(command, Util.getPort());
                    displayPostLoginUI();
                    break;
                case "quit":
                    Quit.quit(Util.getPort());
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
}
