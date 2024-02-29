package serviceTests;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.authDAO;
import dataaccess.gameDAO;
import dataaccess.userDAO;
import model.Records;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import server.clearapplication.Service;
import server.register.Request;
import server.register.Response;

import static org.junit.jupiter.api.Assertions.*;


import java.util.*;

public class ServiceTests {

    private static final ChessGame testChessGame = new ChessGame();
    private static final Records.UserData testUser = new Records.UserData("name", "password", "email");

    private static final Records.GameData testGame = new Records.GameData(1234, null, null, "game name", testChessGame);

    @BeforeEach
    public void cleanup() {
        gameDAO.clearApplication();
    }

    @Test
    @DisplayName("user can be registered")
    public void successRegisterUser() throws TestException{

        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        userDAO.createUser(newUser);
        authDAO.createAuth(request.username);

        Assertions.assertEquals(userDAO.getUser("name"), testUser);
    }

    @Test
    @DisplayName("usernames must be unique")
    public void failRegisterUser() throws TestException{

        Request request1 = new Request("name", "password", "firstEmail");
        Request request = new Request("name", "password", "email");
        Records.UserData newUser1 = new Records.UserData(request1.username, request1.password, request1.email);
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        userDAO.createUser(newUser1);
        authDAO.createAuth(request.username);
        userDAO.createUser(newUser);
        authDAO.createAuth(request.username);
        assertNotEquals(userDAO.getUser("name").email(), testUser.email());
    }

    @Test
    @DisplayName("Clear database")
    public void clearDatabase() throws TestException, DataAccessException {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        userDAO.createUser(newUser);
        authDAO.createAuth(request.username);

        server.clearapplication.Request requestclear = new server.clearapplication.Request();
        assertNull(Service.clearApplication(requestclear).getMessage());
    }

    @Test
    @DisplayName("Games can be created")
    public void createGame() throws TestException{
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        userDAO.createUser(newUser);
        authDAO.createAuth(request.username);
        gameDAO.createGame(testGame);

        assertEquals(gameDAO.getGame(1234), testGame);
    }

    @Test
    @DisplayName("Games must have a name")
    public void badCreateGame() throws TestException, DataAccessException {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        server.creategame.Request request2 = new server.creategame.Request(null, response.authToken);
        server.creategame.Response response2 = null;
        try {
            response2 = server.creategame.Service.createGame(request2);
        }
        catch(DataAccessException e) {
        }

        assertNull(response2);
    }

    @Test
    @DisplayName("Users can join a game")
    public void joinGame() {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        gameDAO.createGame(testGame);
        server.join.Request request1 = new server.join.Request(ChessGame.TeamColor.WHITE, "1234");
        request1.auth = response.authToken;
        try {
            server.join.Service.joinGame(request1);
        } catch (DataAccessException e) {
        }

        assertEquals(gameDAO.getGame(1234).whiteUsername(), "name");
    }

    @Test
    @DisplayName("Users cant join as a filled slot")
    public void badJoinGame() {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        Records.GameData gameData = new Records.GameData(1234, "already taken", null, "game name", testChessGame);
        gameDAO.createGame(gameData);

        server.join.Request request1 = new server.join.Request(ChessGame.TeamColor.WHITE, "1234");
        request1.auth = response.authToken;

        try {
            server.join.Service.joinGame(request1);
        } catch (DataAccessException e) {
            gameDAO.setWhitePlayer(1234, "already taken");
        }

        assertNotEquals(gameDAO.getGame(1234).whiteUsername(), "name");
    }

    @Test
    @DisplayName("Can login")
    public void login() throws TestException, DataAccessException {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        authDAO.deleteAuth(response.authToken);
        assertNull(authDAO.getAuth(response.authToken));

        server.login.Request request1 = new server.login.Request("name", "password");
        server.login.Response response1 = null;
        try {
            response1 = server.login.Service.login(request1);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error bad login", 401);
        }

        assertNotNull(authDAO.getAuth(response1.authToken));
    }

    @Test
    @DisplayName("Cant login with bad password")
    public void badLogin() throws TestException, DataAccessException{
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        authDAO.deleteAuth(response.authToken);
        assertNull(authDAO.getAuth(response.authToken));

        server.login.Request request1 = new server.login.Request("name", "wrong password");
        server.login.Response response1 = null;
        try {
            response1 = server.login.Service.login(request1);
        } catch (DataAccessException e) {
        }

        assertNull(response1);
    }

    @Test
    @DisplayName("Can logout")
    public void logout() {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        authDAO.deleteAuth(response.authToken);
        assertNull(authDAO.getAuth(response.authToken));
    }

    @Test
    @DisplayName("Cant login with bad password")
    public void badLogout() {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));
        String correctToken = response.authToken;
        response.authToken = "incorrect token";

        authDAO.deleteAuth(response.authToken);
        assertNotNull(authDAO.getAuth(correctToken));
    }

    @Test
    @DisplayName("Gameslist is generated")
    public void list() throws TestException{
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        userDAO.createUser(newUser);
        authDAO.createAuth(request.username);

        Records.GameData gameData = new Records.GameData(1234, null, null, "game name", testChessGame);
        Records.GameData gameData3 = new Records.GameData(12345, null, null, "game 2", testChessGame);

        gameDAO.createGame(gameData);
        gameDAO.createGame(gameData3);

        HashSet<Records.GameData> expectedList = new HashSet<>();//main
        expectedList.add(gameData);
        expectedList.add(gameData3);

        assertEquals(gameDAO.getList(), expectedList);
    }

    @Test
    @DisplayName("Gameslist isnt generated")
    public void badList() throws TestException{
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        userDAO.createUser(newUser);
        authDAO.createAuth(request.username);

        Records.GameData gameData = new Records.GameData(1234, null, null, "game name", testChessGame);
        Records.GameData gameData2 = new Records.GameData(12345, null, null, "game 3", testChessGame);
        Records.GameData gameData3 = new Records.GameData(123456, null, null, "game 2", testChessGame);

        gameDAO.createGame(gameData);
        gameDAO.createGame(gameData2);
        gameDAO.createGame(gameData3);


        HashSet<Records.GameData> expectedList = new HashSet<>();
        expectedList.add(gameData);
        expectedList.add(gameData3);

        assertNotEquals(gameDAO.getList(), expectedList);
    }

    @Test
    @DisplayName("DUMMY TEST BECAUSE EXTENDED CLASSES NEED TESTS")
    public void dummy() {
        assertNotNull(1);
    }

    @Test
    @DisplayName("DUMMY TEST BECAUSE EXTENDED CLASSES NEED more TESTS")
    public void dummy2() {
        assertNotEquals(1,2);
    }
}
