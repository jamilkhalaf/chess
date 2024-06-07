package server;

import dataaccess.*;
import handlers.*;
import spark.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Scanner;

public class Server {
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    public int run(int desiredPort) {

        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();

        Spark.port(desiredPort);

        //websocket spark

        Spark.webSocket("/connect", new WSServer(authDAO,gameDAO,userDAO));


        Spark.staticFiles.location("/web");
        Spark.get("/", (req, res) -> {
            String htmlContent = readHtmlFile("web/index.html");
            res.type("text/html");
            res.body(htmlContent);
            return htmlContent;
        });

        try {DatabaseManager.createTables();}
        catch (DataAccessException e) {
        }



        Spark.post("/user", (req, res) -> (new RegisterHandler(userDAO, gameDAO, authDAO)).handleCreateUser.handle(req, res));
        Spark.post("/session", (req, res) -> (new LoginHandler(userDAO, gameDAO, authDAO)).handleLoginUser.handle(req, res));
        Spark.delete("/session", (req, res) -> (new LogoutHandler(userDAO, gameDAO, authDAO)).handleLogoutUser.handle(req, res));
        Spark.delete("/db", (req, res) -> (new ClearHandler(userDAO, gameDAO, authDAO)).handleClearEverything.handle(req, res));
        Spark.post("/game", (req, res) -> (new CreateGameHandler(userDAO, gameDAO, authDAO)).handleCreateGame.handle(req, res));
        Spark.get("/game", (req, res) -> (new ListGamesHandler(userDAO, gameDAO, authDAO)).handleListGames.handle(req, res));
        Spark.put("/game", (req, res) -> (new JoinGameHandler(userDAO, gameDAO, authDAO)).handleJoinGame.handle(req, res));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private static String readHtmlFile(String filePath) {
        try (InputStream inputStream = Server.class.getClassLoader().getResourceAsStream(filePath)) {
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                return scanner.useDelimiter("\\A").next();
            }
        } catch (IOException e) {
            System.out.println("error");
            return "<html><body><h1>Error loading HTML file</h1></body></html>";
        }
    }

}
