package server;

import dataaccess.*;
import handlers.LoginHandler;
import handlers.RegisterHandler;
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

        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();

        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");
        Spark.get("/", (req, res) -> {
            String htmlContent = readHtmlFile("web/index.html");
            res.type("text/html");
            res.body(htmlContent);
            return htmlContent;
        });
        Spark.post("/user", (req, res) -> (new RegisterHandler(userDAO, gameDAO, authDAO)).handleCreateUser.handle(req, res));
        Spark.post("/session", (req, res) -> (new LoginHandler(userDAO, gameDAO, authDAO)).handleLoginUser.handle(req, res));

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
            e.printStackTrace();
            return "<html><body><h1>Error loading HTML file</h1></body></html>";
        }
    }

}
