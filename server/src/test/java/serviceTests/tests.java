package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.authDAO;
import dataAccess.gameDAO;
import dataAccess.userDAO;
import model.Records;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import server.Server;
import server.clearApplication.Service;
import server.register.Request;
import server.register.Response;

import static org.junit.jupiter.api.Assertions.*;


import java.util.*;

public class tests {

    private static final ChessGame testChessGame = new ChessGame();
    private static final Records.UserData testUser = new Records.UserData("name", "password", "email");

    private static final Records.AuthData testAuth = new Records.AuthData("RandomToken", "name");

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
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        Assertions.assertEquals(userDAO.getUser("name"), testUser);
    }

    @Test
    @DisplayName("usernames must be unique")
    public void failRegisterUser() throws TestException{

        Request request1 = new Request("name", "password", "firstEmail");
        Request request = new Request("name", "password", "email");
        Records.UserData newUser1 = new Records.UserData(request1.username, request1.password, request1.email);
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response1 = new Response(userDAO.createUser(newUser1), authDAO.createAuth(request.username));
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));
        assertNotEquals(userDAO.getUser("name").email(), testUser.email());
    }

    @Test
    @DisplayName("Clear database")
    public void clearDatabase() throws TestException, DataAccessException {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        server.createGame.Request request2 = new server.createGame.Request("game name", response.authToken);
        Records.GameData gameData = new Records.GameData(1234, null, null, "game name", testChessGame);
        server.createGame.Response response2 = new server.createGame.Response(gameDAO.createGame(gameData));

        server.clearApplication.Request requestclear = new server.clearApplication.Request();
        assertNull(Service.clearApplication(requestclear).getMessage());
    }

    @Test
    @DisplayName("Games can be created")
    public void createGame() throws TestException{
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        server.createGame.Request request2 = new server.createGame.Request("game name", response.authToken);
        Records.GameData gameData = new Records.GameData(1234, null, null, "game name", testChessGame);
        server.createGame.Response response2 = new server.createGame.Response(gameDAO.createGame(gameData));

        assertEquals(gameDAO.getGame(1234), testGame);
    }

    @Test
    @DisplayName("Games must have a name")
    public void badCreateGame() throws TestException, DataAccessException {
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        server.createGame.Request request2 = new server.createGame.Request(null, response.authToken);
        server.createGame.Response response2 = null;
        try {
            response2 = server.createGame.Service.createGame(request2);
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

        server.createGame.Request request2 = new server.createGame.Request("game name", response.authToken);
        Records.GameData gameData = new Records.GameData(1234, null, null, "game name", testChessGame);
        server.createGame.Response response2 = new server.createGame.Response(gameDAO.createGame(gameData));

        server.join.Request request1 = new server.join.Request(ChessGame.TeamColor.WHITE, "1234");
        request1.auth = response.authToken;
        try {
            server.join.Response response1 = server.join.Service.joinGame(request1);
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
            server.join.Response response1 = server.join.Service.joinGame(request1);
        } catch (DataAccessException e) {
            gameDAO.setWhitePlayer(1234, "already taken");
        }

        assertNotEquals(gameDAO.getGame(1234).whiteUsername(), "name");
    }

    @Test
    @DisplayName("Can login")
    public void login() {
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
        }

        assertNotNull(authDAO.getAuth(response1.authToken));
    }

    @Test
    @DisplayName("Cant login with bad password")
    public void badLogin() {
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
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        server.createGame.Request request2 = new server.createGame.Request("game name", response.authToken);
        Records.GameData gameData = new Records.GameData(1234, null, null, "game name", testChessGame);
        server.createGame.Response response2 = new server.createGame.Response(gameDAO.createGame(gameData));

        server.createGame.Request request3 = new server.createGame.Request("game 2", response.authToken);
        Records.GameData gameData3 = new Records.GameData(1234, null, null, "game 2", testChessGame);
        server.createGame.Response response3 = new server.createGame.Response(gameDAO.createGame(gameData3));//note

        HashSet<Records.GameData> expectedList = new HashSet<Records.GameData>();
        expectedList.add(gameData);
        expectedList.add(gameData3);

        assertEquals(gameDAO.getList(response.authToken), expectedList);
    }

    @Test
    @DisplayName("Gameslist isnt generated")
    public void badList() throws TestException{
        Request request = new Request("name", "password", "email");
        Records.UserData newUser = new Records.UserData(request.username, request.password, request.email);
        Response response = new Response(userDAO.createUser(newUser), authDAO.createAuth(request.username));

        server.createGame.Request request2 = new server.createGame.Request("game name", response.authToken);
        Records.GameData gameData = new Records.GameData(1234, null, null, "game name", testChessGame);
        server.createGame.Response response2 = new server.createGame.Response(gameDAO.createGame(gameData));

        Records.GameData gameData2 = new Records.GameData(12345, null, null, "game 3", testChessGame);


        server.createGame.Request request3 = new server.createGame.Request("game 2", response.authToken);
        Records.GameData gameData3 = new Records.GameData(123456, null, null, "game 2", testChessGame);
        server.createGame.Response response3 = new server.createGame.Response(gameDAO.createGame(gameData3));

        gameDAO.createGame(gameData2);

        HashSet<Records.GameData> expectedList = new HashSet<Records.GameData>();
        expectedList.add(gameData);
        expectedList.add(gameData3);

        assertNotEquals(gameDAO.getList(response.authToken), expectedList);
    }
}
