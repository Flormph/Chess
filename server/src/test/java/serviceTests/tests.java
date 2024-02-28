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
    @DisplayName("Games can be created")
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
}
