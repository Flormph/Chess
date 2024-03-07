package serviceTests;

import model.Records;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import server.Server;
import dataaccess.*;
import server.*;
import chess.*;
import server.clearapplication.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class dataAccessTests {
    private static TestServerFacade serverFacade;
    private static Server server;

    Records.UserData existingUser = new Records.UserData("Joe", "pass", "email");
    Records.AuthData existingToken = new Records.AuthData("SomeCode", "Joe");


    @BeforeAll
    public static void init() {
        startServer();
        serverFacade.clear();
        try {
            DatabaseManager.createDatabase();
        }
        catch (DataAccessException e) {
        }
    }

    @AfterAll
    static void stopServer() {
        try {
            Service.clearApplication(new Request());
        }
        catch (DataAccessException e) {

        }
        server.stop();
    }

    @BeforeEach
    void clearDatabase() {
        try {
            Service.clearApplication(new Request());
        }
        catch (DataAccessException e) {

        }
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

    @Test
    @DisplayName("Register user")
    public void registerTest() throws Exception {
        try {
            userDAO.createUser(existingUser);
        }
        catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Bad register")
    public void badRegister() throws Exception {
        try {
            userDAO.createUser(existingUser);
            assertThrows(DataAccessException.class, () -> userDAO.createUser(existingUser));
        }
        catch(DataAccessException e) {

        }
    }

    @Test
    @DisplayName("Register authToken")
    public void tokenTest() throws Exception {
        try {
            authDAO.createAuth(existingToken.username());
        }
        catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Bad register")
    public void badToken() throws Exception {
        try {
            authDAO.createAuth(existingToken.username());
            assertThrows(DataAccessException.class, () -> authDAO.createAuth(existingToken.username()));
        }
        catch(DataAccessException e) {

        }
    }
}
