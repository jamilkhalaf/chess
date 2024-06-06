package client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;
import server.Server;
import gameplay.ServerFacade;

import java.util.UUID;

public class ServerFacadeTests {

    private static Server server;
    private static String baseUrl;

    @BeforeAll
    public static void init() {
        // Start the server and get the port
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        // Set the base URL to the server address
        baseUrl = "http://localhost:" + port;
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
    public void testRegisterSuccess() throws Exception {
        String url = baseUrl + "/user";
        String uniqueUser = "user" + UUID.randomUUID();
        String uniquePass = "pass" + UUID.randomUUID();
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"newuser@example.com\"}", uniqueUser, uniquePass);
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertTrue(response.contains("authToken"));
    }

    @Test
    public void testRegisterFailure() throws Exception {
        String url = baseUrl + "/user";
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"newuser@example.com\"}", "j", "j");
        String response = ServerFacade.sendPostRequest(url, json);
        String json2 = "{\"username\":\"j\",\"password\":\"j\",\"email\":\"newuser@example.com\"}";
        String response2 = ServerFacade.sendPostRequest(url, json2);
        Assertions.assertFalse(response2.contains("authToken"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String url = baseUrl + "/session";
        String json = "{\"username\":\"j\",\"password\":\"j\"}";
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertTrue(response.contains("authToken"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        String url = baseUrl + "/session";
        String json = "{\"username\":\"wronguser\",\"password\":\"wrongpass\"}";
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertFalse(response.contains("authToken"));
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        String loginUrl = baseUrl + "/session";
        String json = "{\"username\":\"j\",\"password\":\"j\"}";
        String loginResponse = ServerFacade.sendPostRequest(loginUrl, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();

        if (jsonObject.has("authToken")) {
            String authToken = jsonObject.get("authToken").getAsString();
            String logoutUrl = baseUrl + "/session";
            String response = ServerFacade.sendDeleteRequest(logoutUrl, authToken);
            Assertions.assertEquals("{}", response);
        } else {
            Assertions.fail("Login response did not contain authToken");
        }
    }

    @Test
    public void testLogoutFailure() {
        String authToken = "";
        String url = baseUrl + "/session";
        Assertions.assertThrows(Exception.class, () -> ServerFacade.sendDeleteRequest(url, authToken));
    }

    @Test
    public void testCreateGameSuccess() throws Exception {
        String loginUrl = baseUrl + "/session";
        String json = "{\"username\":\"j\",\"password\":\"j\"}";
        String loginResponse = ServerFacade.sendPostRequest(loginUrl, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String gameUrl = baseUrl + "/game";
        String json2 = "{\"gameName\":\"newgame\"}";
        String response = ServerFacade.sendGameRequest(gameUrl, json2, authToken);
        Assertions.assertNotEquals("{\"message\":\"error: unauthorized\"}", response);
    }

    @Test
    public void testCreateGameFailure() throws Exception {
        String url = baseUrl + "/game";
        String json = "{\"gameName\":\"\"}";
        String response = ServerFacade.sendPostRequest(url, json);
        Assertions.assertNotEquals("{\"status\":\"success\"}", response);
    }

    @Test
    public void testJoinGameSuccess() throws Exception {
        String loginUrl = baseUrl + "/session";
        String json = "{\"username\":\"j\",\"password\":\"j\"}";
        String loginResponse = ServerFacade.sendPostRequest(loginUrl, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String gameUrl = baseUrl + "/game";
        String json2 = "{\"gameName\":\"newgame\"}";
        String response = ServerFacade.sendGameRequest(gameUrl, json2, authToken);
        String json3 = "{\"playerColor\": \"WHITE\", \"gameID\": 2}";
        String response2 = ServerFacade.sendPutRequest(gameUrl, json3, authToken);
        Assertions.assertEquals("{\"message\": \"Error: bad request\"}", response2);
    }

    @Test
    public void testJoinGameFailure() throws Exception {
        String loginUrl = baseUrl + "/session";
        String json = "{\"username\":\"j\",\"password\":\"j\"}";
        String loginResponse = ServerFacade.sendPostRequest(loginUrl, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String gameUrl = baseUrl + "/game";
        String json2 = "{\"playerColor\": \"WHITE\", \"gameID\": 2}";
        String response = ServerFacade.sendPutRequest(gameUrl, json2, authToken);
        Assertions.assertEquals("{}", response);
    }

    @Test
    public void testListGamesSuccess() throws Exception {
        String loginUrl = baseUrl + "/session";
        String json = "{\"username\":\"j\",\"password\":\"j\"}";
        String loginResponse = ServerFacade.sendPostRequest(loginUrl, json);
        JsonObject jsonObject = JsonParser.parseString(loginResponse).getAsJsonObject();
        String authToken = jsonObject.get("authToken").getAsString();
        String gameUrl = baseUrl + "/game";
        String response = ServerFacade.sendGetRequest(gameUrl, authToken);
        Assertions.assertTrue(response.contains("games"));
    }

    @Test
    public void testListGamesFailure() {
        String url = baseUrl + "/invalidEndpoint";
        Assertions.assertThrows(Exception.class, () -> ServerFacade.sendGetRequest(url, ""));
    }
}
