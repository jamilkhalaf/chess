package ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.Server;
import java.util.Scanner;


public class PreLoginUI {
    private static Scanner scanner;
    private static Server server;
    private static State currentState = State.LOGGED_OUT;
    private static String authToken;

    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }

    public static void setCurrentState(State currentState) {
        PreLoginUI.currentState = currentState;
    }

    public static void init() {
        scanner = new Scanner(System.in);
        server = new Server();
        var port = server.run(4510);
        System.out.println(EscapeSequences.SET_TEXT_ITALIC +"♕ Welcome to 240 chess. Type Help to get started. ♕ ");
    }

    public static void display() {
        displayMenu();
        while (true) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR + getPrompt());
            String command = scanner.nextLine().trim().toLowerCase();
            String[] commandParts = command.split(" ");
            switch (commandParts[0]) {
                case "help":
                    displayHelp();
                    break;
                case "quit":
                    System.exit(0);
                    break;
                case "login":
                    if (commandParts.length == 3) {
                        handleLogin(commandParts[1], commandParts[2]);
                    } else {
                        System.out.println("Usage: login <USERNAME> <PASSWORD>");
                    }
                    break;
                case "register":
                    if (commandParts.length == 4) {
                        handleRegister(commandParts[1], commandParts[2], commandParts[3]);
                    } else {
                        System.out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
                    }
                    break;
                default:
                    System.out.println("Unknown command. Type 'Help' for a list of commands.");
            }
        }
    }

    private static void handleRegister(String username, String password, String email) {
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"email\":\"%s\"}", username, password, email);
        try {
            String response = HandleClientRequest.sendPostRequest("http://localhost:4510/user", json);
            System.out.println("Server response: " + response);

            PreLoginUI.display();

        } catch (Exception e) {
            System.out.println("Failed to register: " + e.getMessage());
        }
    }

    private static void handleLogin(String username, String password) {
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        try {
            String response = HandleClientRequest.sendPostRequest("http://localhost:4510/session", json);
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            authToken = jsonObject.get("authToken").getAsString();
            System.out.println("Server response: " + response);
            currentState = State.LOGGED_IN;
            PostLoginUI.display();

        } catch (Exception e) {
            System.out.println("Failed to register: " + e.getMessage());
        }
    }



    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        PreLoginUI.authToken = authToken;
    }

    public static void displayMenu() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - to create an account");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - to play chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "quit" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - playing chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - with possible commands");
    }

    public static void displayHelp() {
        System.out.println("helping");
    }



    private static String getPrompt() {
        String stateStr;
        switch (currentState) {
            case LOGGED_IN:
                stateStr = "[LOGGED_IN]";
                break;
            case IN_GAME:
                stateStr = "[IN_GAME]";
                break;
            case LOGGED_OUT:
            default:
                stateStr = "[LOGGED_OUT]";
                break;
        }
        return stateStr + " >>> ";
    }
}
