package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import model.Records.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static String command;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

}
