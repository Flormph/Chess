package serviceTests;

import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import server.Server;
import dataaccess.*;
import server.*;
import chess.*;

public class dataAccessTests {
    private static TestServerFacade serverFacade;
    private static Server server;


    @BeforeAll
    public static void init() {
        startServer();
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    public static void startServer() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
    }

    @Test
    @DisplayName("SQL SERVER STARTS")
    public void startTest() throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            Assertions.assertNotNull(conn);
        }
    }
}
