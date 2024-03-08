package serviceTests;

import model.Records;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import server.Server;
import dataaccess.*;
import server.clearapplication.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("NewClassNamingConvention")
public class dataAccessTests {
    private static TestServerFacade serverFacade;
    private static Server server;

    Records.UserData existingUser = new Records.UserData("Joe", "pass", "email");
    Records.AuthData existingToken = new Records.AuthData("SomeCode", "Joe");


    @BeforeAll
    public static void init() throws DataAccessException{
        startServer();
        serverFacade.clear();
        try {
            DatabaseManager.createDatabase();
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage(), e.getCode());
        }
    }

    @AfterAll
    static void stopServer() throws DataAccessException{
        try {
            Service.clearApplication(new Request());
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage(), e.getCode());
        }
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws DataAccessException{
        try {
            Service.clearApplication(new Request());
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage(), e.getCode());
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
    public void registerTest() {
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
            throw new Exception();
        }
    }

    @Test
    @DisplayName("Register authToken")
    public void tokenTest() {
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
            throw new Exception();
        }
    }

    @Test
    @DisplayName("Delete token")
    public void deleteToken() {
        try {
            String token = authDAO.createAuth(existingToken.username());
            Assertions.assertTrue(authDAO.hasUser(existingToken.username()));
            authDAO.deleteAuth(token);
            Assertions.assertFalse(authDAO.hasUser(existingToken.username()));
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Bad Delete token")
    public void badDeleteToken() throws Exception {
        try {
            Assertions.assertTrue(authDAO.hasUser(existingToken.username()));
            authDAO.deleteAuth("Incorrect Token");
            Assertions.assertTrue(authDAO.hasUser(existingToken.username()));
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
