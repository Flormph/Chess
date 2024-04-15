package clientTests;

import server.clearapplication.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Util;


public class ServerFacadeTests {

    private static Server tempserver;
    static ServerFacade facade;
    private final String existingUsername = "joe";
    private final String existingPass = "pass";
    private final String existingEmail = "email";
    private String existingAuth;
    private String existingGameID;
    private void registerExistingUser() throws Exception{
        existingAuth = server.register.Service.register(new server.register.Request(existingUsername, existingPass, existingEmail)).authToken;
    }

    private void registerAltExistingUser() throws Exception{
        existingAuth = server.register.Service.register(new server.register.Request(existingUsername + "1", existingPass, existingEmail)).authToken;
    }

    private void createExistingGame() throws Exception{
        String existingGameName = "myGame";
        existingGameID = server.creategame.Service.createGame(new server.creategame.Request(existingGameName, existingAuth)).gameID;
    }

    @BeforeAll
    public static void init() {
        tempserver = new Server();
        var port = tempserver.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void reset() throws Exception {
        Service.clearApplication(new Request());
    }

    @AfterAll
    static void stopServer() {
        tempserver.stop();
    }


    @Test
    void register() throws Exception {
        int serverResponse = facade.register("register Joseph Pass Lee", facade.port);
        Assertions.assertEquals(200, serverResponse);
    }

    @Test
    void badRegister() throws Exception {
        int serverResponse = facade.register("register Joseph PassLee", facade.port);
        Assertions.assertNotEquals(200, serverResponse);
    }

    @Test
    void logout() throws Exception {
        registerExistingUser();
        Util.setToken(existingAuth);
        int serverResponse = facade.logout(facade.port);
        Assertions.assertEquals(200, serverResponse);
    }

    @Test
    void badLogout() {
        Assertions.assertThrows(java.io.IOException.class, () -> facade.logout(facade.port));

    }

    @Test
    void badLogin() {
        Assertions.assertThrows(java.io.IOException.class, () -> facade.login("login Joseph ee", facade.port));
    }

    @Test
    void login() throws Exception {
        registerExistingUser();
        Util.setToken(existingAuth);
        facade.logout(facade.port);
        int serverResponse = facade.login("login " + existingUsername + " " + existingPass, facade.port);
        Assertions.assertEquals(200, serverResponse);
    }

    @Test
    void badCreateGame() throws Exception {
        int serverResponse = facade.createGame("create", facade.port);
        Assertions.assertNotEquals(200, serverResponse);
    }

    @Test
    void createGame() throws Exception {
        registerExistingUser();
        Util.setToken(existingAuth);
        int serverResponse = facade.createGame("create Name", facade.port);
        Assertions.assertEquals(200, serverResponse);
    }

    @Test
    void badJoinGame() throws Exception {
        int serverResponse = facade.joinGame("join", facade.port);
        Assertions.assertNotEquals(200, serverResponse);
    }

    @Test
    void joinGame() throws Exception {
        registerExistingUser();
        Util.setToken(existingAuth);
        createExistingGame();
        facade.logout(facade.port);
        registerAltExistingUser();
        Util.setToken(existingAuth);
        int serverResponse = facade.joinGame("join " + existingGameID + " BLACK", facade.port);
        Assertions.assertEquals(200, serverResponse);
    }

    @Test
    void badJoinObserver() throws Exception {
        int serverResponse = facade.joinObserver("join", facade.port);
        Assertions.assertNotEquals(200, serverResponse);
    }

    @Test
    void joinObserver() throws Exception {
        registerExistingUser();
        Util.setToken(existingAuth);
        createExistingGame();
        facade.logout(facade.port);
        registerAltExistingUser();
        Util.setToken(existingAuth);
        int serverResponse = facade.joinObserver("join " + existingGameID, facade.port);
        Assertions.assertEquals(200, serverResponse);
    }

    @Test
    void listGames() throws Exception {
        registerExistingUser();
        Util.setToken(existingAuth);
        createExistingGame();
        int serverResponse = facade.listGames(facade.port);
        Assertions.assertEquals(200, serverResponse);
    }

    @Test
    void badListGames() {
        Assertions.assertThrows(java.io.IOException.class, () -> facade.listGames(facade.port));
    }

    @Test
    void quit() throws Exception {
        registerExistingUser();
        Util.setToken(existingAuth);
        int serverResponse = facade.quit(facade.port);
        Assertions.assertEquals(200, serverResponse);
    }
}
