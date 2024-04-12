package ui;

import java.util.Scanner;

public class Ui {
    public static Ui ui;
    private static final Scanner scanner = new Scanner(System.in);
    static Util util = Util.getInstance();


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
                    break;
                case "quit":
                    Quit.quit(util.getPort());
                    System.exit(0);
                    break;
                case "login":
                    Login.login(command, util.getPort());
                    break;
                case "register":
                    Register.register(command, util.getPort());
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    static void displayPostLoginUI() throws Exception{
        while (util.getToken() != null) {
            System.out.print("[LOGGED_IN] >>> ");
            String command = scanner.nextLine().toLowerCase();
            String[] words = command.trim().split("\\s");
            switch (words[0]) {
                case "help":
                    displayHelp();
                    break;
                case "logout":
                    Logout.logout(util.getPort());
                    displayPreLoginUI();
                    break;
                case "create":
                    CreateGame.createGame(command, util.getPort());
                    break;
                case "list":
                    ListGames.listGames(util.getPort());
                    break;
                case "join":
                    JoinGame.joinGame(command, util.getPort());
                    break;
                case "observe":
                    JoinObserver.joinObserver(command, util.getPort());
                    break;
                case "quit":
                    Quit.quit(util.getPort());
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
