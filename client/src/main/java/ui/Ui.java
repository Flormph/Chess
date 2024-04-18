package ui;

import model.Records;
import webSocketMessages.userCommands.*;

import java.util.Objects;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Ui {
    private static final Scanner scanner = new Scanner(System.in);
    static boolean inGame = false;

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
                    new UserGameCommand(Util.getToken());
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
                    displayGameUI(game);
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
    static void displayGameUI(Records.GameData game) throws Exception{
        inGame = true;
        while (true) {
            System.out.print("[IN_GAME] >>> ");
            String command = scanner.nextLine();
            String[] words = command.trim().split("\\s");


            var ws = new WebSocketClient(Util.getPort());

            System.out.println("Enter a message you want to echo");
            while (true) {
                switch (words[0].toLowerCase()) {
                    case "help":
                        displayHelp();
                        displayGameUI(game);
                        break;
                    case "refresh":
                        displayGame(game);
                        displayGameUI(game);
                        break;
                    case "leave":
                        ws.send(UserGameCommand.CommandType.LEAVE.toString());
                        displayPostLoginUI();
                        break;
                    case "move":
                        ws.send(UserGameCommand.CommandType.MAKE_MOVE.toString() + " " + words[1] + " " + words[2]);
                        displayGameUI(game);
                        break;
                    case "resign":
                        ws.send(UserGameCommand.CommandType.RESIGN.toString());
                        displayGameUI(game);
                        break;
                    case "highlight":
                        Util.highlight(game);
                        displayGameUI(game);
                        break;
                    default:
                        System.out.println("Invalid command. Please try again.");
                }
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
        else if(inGame) {
            System.out.println("Refresh - Redraws the chess board");
            System.out.println("Leave - this game");
            System.out.println("Move <Position> <Position> - Move piece from first to second position");
            System.out.println("Resign - to forfeit the game");
            System.out.println("Highlight <Position> - Highlights possible moves of given piece");
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
