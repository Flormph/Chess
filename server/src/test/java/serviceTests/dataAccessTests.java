package serviceTests;

import chess.ChessGame;
import model.Records;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import server.Server;
import dataaccess.*;
import server.clearapplication.*;

import java.sql.SQLException;
import java.util.HashSet;

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
    public void startSQL() throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            Assertions.assertNotNull(conn);
        }
    }

    @Test
    @DisplayName("Register user")
    public void register() {
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
    public void createToken() {
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
            authDAO.createAuth(existingToken.username());
            Assertions.assertTrue(authDAO.hasUser(existingToken.username()));
            authDAO.deleteAuth("Incorrect Token");
            Assertions.assertTrue(authDAO.hasUser(existingToken.username()));
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Can get username from token")
    public void GetAuth() throws DataAccessException {
        try {
            String expectedUsername = "testUsername";
            String token = authDAO.createAuth(expectedUsername);
            Assertions.assertEquals(authDAO.getAuth(token), expectedUsername);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    @DisplayName("Doesn't give username if bad token")
    public void badGetAuth() throws DataAccessException {
        try {
            String expectedUsername = "testUsername";
            authDAO.createAuth(expectedUsername);
            assertThrows(DataAccessException.class, () -> authDAO.getAuth(existingToken.username()));
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create game")
    public void createGame() {
        try {
            Records.GameData gameData = new Records.GameData(1, "white", "black", "Chess", new ChessGame());
            int gameId = 0;
            gameId = gameDAO.createGame(gameData);
            Assertions.assertNotEquals(gameId, 0);
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Failed to create game: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Cant create two games with same ID")
    public void badCreateGame() {
        try {
            Records.GameData gameData = new Records.GameData(1, "white", "black", "Chess", new ChessGame()); // Assuming you have ChessGame implemented
            gameDAO.createGame(gameData);
            assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData));
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Created game: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Check if game exists")
    public void hasGame() {
        try {
            Records.GameData gameData = new Records.GameData(1, "white", "black", "Chess", new ChessGame()); // Assuming you have ChessGame implemented
            gameDAO.createGame(gameData);
            Assertions.assertTrue(gameDAO.hasGame(1));
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Failed to check if game exists: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("can't find non-existent game")
    public void badHasGame() {
        try {
            Assertions.assertFalse(gameDAO.hasGame(1));
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Found game when it didn't exist" + e.getMessage());
        }
    }

    @Test
    @DisplayName("cant get non-existant game")
    public void badGetGame() {
        try {
            assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Get game by ID")
    public void getGame() {
        try {
            Records.GameData gameData = new Records.GameData(1, "white", "black", "Chess", new ChessGame()); // Assuming you have ChessGame implemented
            gameDAO.createGame(gameData);
            Records.GameData retrievedGameData = gameDAO.getGame(1);
            Assertions.assertNotNull(retrievedGameData);
            Assertions.assertEquals(1, retrievedGameData.gameID());
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Failed to get game by ID: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Set white player")
    public void setWhitePlayer() {
        try {
            Records.GameData gameData = new Records.GameData(1, "white", "black", "Chess", new ChessGame()); // Assuming you have ChessGame implemented
            gameDAO.createGame(gameData);
            gameDAO.setWhitePlayer(1, "newWhite");
            Records.GameData retrievedGameData = gameDAO.getGame(1);
            Assertions.assertEquals("newWhite", retrievedGameData.whiteUsername());
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Failed to set white player: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Bad set white player")
    public void badSetWhitePlayer() {
        try {
            Assertions.assertThrows(DataAccessException.class, () -> gameDAO.setWhitePlayer(1, "newWhite"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Set black player")
    public void setBlackPlayerTest() {
        try {
            Records.GameData gameData = new Records.GameData(1, "white", "black", "Chess", new ChessGame()); // Assuming you have ChessGame implemented
            gameDAO.createGame(gameData);
            gameDAO.setBlackPlayer(1, "newBlack");
            Records.GameData retrievedGameData = gameDAO.getGame(1);
            Assertions.assertEquals("newBlack", retrievedGameData.blackUsername());
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Failed to set black player: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("bad set black player")
    public void badSetBlackPlayer() {
        try {
            Assertions.assertThrows(DataAccessException.class, () -> gameDAO.setBlackPlayer(1, "newBlack"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Get game list")
    public void getGameList() {
        try {
            Records.GameData gameData1 = new Records.GameData(1, "white1", "black1", "Chess1", new ChessGame()); // Assuming you have ChessGame implemented
            Records.GameData gameData2 = new Records.GameData(2, "white2", "black2", "Chess2", new ChessGame()); // Assuming you have ChessGame implemented
            gameDAO.createGame(gameData1);
            gameDAO.createGame(gameData2);
            HashSet<Records.GameData> gameList = gameDAO.getList();
            Assertions.assertEquals(2, gameList.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Failed to get game list: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Bad get game list")
    public void badGetGameList() {
        try {
            HashSet<Records.GameData> gameList = gameDAO.getList();
            Assertions.assertEquals(0, gameList.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            Assertions.fail("Failed to get game list: " + e.getMessage());
        }
    }
}
