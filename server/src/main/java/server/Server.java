package server;

import spark.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Scanner;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");
        Spark.get("/", (req, res) -> {
            String htmlContent = readHtmlFile("web/index.html");
            res.type("text/html");
            res.body(htmlContent);
            return htmlContent;
        });
        Spark.get("/hello", (req, res) -> "hello jamil");
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
