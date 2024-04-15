package clientTests;

public class ServerFacade {
    ServerFacade(int port) {
        this.port = port;
    }

    int port;

    public int register(String command, int port) throws Exception{
        return ui.Register.register(command, port);
    }
    public int login(String command, int port) throws Exception{
        return ui.Login.login(command, port);
    }

    public int logout(int port) throws Exception{
        return ui.Logout.logout(port);
    }

    public int createGame(String command, int port) throws Exception{
        return ui.CreateGame.createGame(command, port);
    }

    public int listGames(int port) throws Exception{
        return ui.ListGames.listGames(port);
    }

    public int joinGame(String command, int port) throws Exception{
        return ui.JoinGame.joinGame(command, port);
    }

    public int joinObserver(String command, int port) throws Exception {
        return ui.JoinObserver.joinObserver(command, port);
    }

    public int quit(int port) throws Exception {
        return ui.Quit.quit(port);
    }
 }
