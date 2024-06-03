package client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;
import server.Server;
import ui.PreLoginUI;
import ui.ServerFacade;

import java.util.UUID;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testRegister_Success() throws Exception {
        String url = "http://localhost:4510/user";
        String uniqueUser = "user" + UUID.randomUUID();
        String uniquePass = "pass" + UUID.randomUUID();
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"newuser@example.com\"}", uniqueUser, uniquePass);
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertTrue(response.contains("authToken"));
    }

    @Test
    public void testRegister_Failure() throws Exception {
        String url = "http://localhost:4510/user";
        String json = "{\"username\":\"j\",\"password\":\"j\",\"email\":\"newuser@example.com\"}";
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertFalse(response.contains("authToken"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        String url = "http://localhost:4510/session";
        String json = "{\"username\":\"j\",\"password\":\"k\"}";
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertTrue(response.contains("authToken"));
    }

    @Test
    public void testLogin_Failure() throws Exception{
        String url = "http://localhost:4510/session";
        String json = "{\"username\":\"wronguser\",\"password\":\"wrongpass\"}";
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertFalse(response.contains("authToken"));
    }

    // Register Test Cases

    // Logout Test Cases
    @Test
    public void testLogout_Success() throws Exception {
        String url1 = "http://localhost:4510/session";

        String json = "{\"username\":\"j\",\"password\":\"k\"}";
        String loginResponse = ServerFacade.sendPostRequest(url1, json);
        System.out.println("Login Response: " + loginResponse);  // Debugging login response

        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();  // Debugging auth token
        System.out.println(authToken);
        String url2 = "http://localhost:4510/session";
        String response = ServerFacade.sendDeleteRequest(url2,authToken);
        Assertions.assertEquals("{}", response);
    }

    @Test
    public void testLogout_Failure() {
        String authTOken = "";
        String url = "http://localhost:4510/session";
        Assertions.assertThrows(Exception.class, () -> ServerFacade.sendDeleteRequest(url,authTOken));
    }

    @Test
    public void testCreateGame_Success() throws Exception {
        String url1 = "http://localhost:4510/session";
        String json = "{\"username\":\"j\",\"password\":\"k\"}";
        String loginResponse = ServerFacade.sendPostRequest(url1, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String url = "http://localhost:4510/game";
        String json2 = "{\"gameName\":\"newgame\"}";
        String response = ServerFacade.sendGameRequest(url, json2,authToken);
        Assertions.assertNotEquals("{\"message\":\"error: unauthorized\"}", response);
    }

    @Test
    public void testCreateGame_Failure() throws Exception {
        String url = "http://localhost:4510/game";
        String json = "{\"gameName\":\"\"}";
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertNotEquals("{\"status\":\"success\"}", response);

    }

    // Join Game Test Cases
    @Test
    public void testJoinGame_Success() throws Exception {
        String url1 = "http://localhost:4510/session";

        String json = "{\"username\":\"j\",\"password\":\"k\"}";
        String loginResponse = ServerFacade.sendPostRequest(url1, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String url = "http://localhost:4510/game";
        String json2 = "{\"playerColor\": \"WHITE\", \"gameID\": 1}";
        String response = ServerFacade.sendPutRequest(url, json2, authToken);
        Assertions.assertEquals("{}", response);
    }

    @Test
    public void testJoinGame_Failure() throws Exception {
        String url1 = "http://localhost:4510/session";
        String json = "{\"username\":\"j\",\"password\":\"k\"}";
        String loginResponse = ServerFacade.sendPostRequest(url1, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String url = "http://localhost:4510/game";
        String json2 = "{\"playerColor\": \"BLACK\", \"gameID\": 1}";
        String response = ServerFacade.sendPutRequest(url, json, authToken);
        Assertions.assertEquals("{\"message\": \"Error: bad request\"}", response);
    }

    // List Games Test Cases
    @Test
    public void testListGames_Success() throws Exception {
        String url1 = "http://localhost:4510/session";

        String json = "{\"username\":\"j\",\"password\":\"k\"}";
        String loginResponse = ServerFacade.sendPostRequest(url1, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String url = "http://localhost:4510/game";
        String response = ServerFacade.sendGetRequest(url,authToken);
        Assertions.assertTrue(response.contains("games"));
    }

    @Test
    public void testListGames_Failure() {
        String url = "http://localhost:4510/invalidEndpoint";
        Assertions.assertThrows(Exception.class, () -> ServerFacade.sendGetRequest(url,""));
    }

    // Create Game Test Cases


}
