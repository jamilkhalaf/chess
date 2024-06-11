package gameplay;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Scanner;


public class PreLoginUI {
    private static Scanner scanner;
    private static State currentState = State.LOGGED_OUT;
    private static String authToken;
    public static WSClient wsClient;
    private static String usernameP;


    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }

    public static String getUsernameP() {
        return usernameP;
    }

    public static void setCurrentState(State currentState) {
        PreLoginUI.currentState = currentState;
    }

    public static void init(WSClient client) {
        scanner = new Scanner(System.in);
        wsClient = client;
        System.out.println(EscapeSequences.SET_TEXT_ITALIC + "♕ Welcome to 240 chess. Type Help to get started. ♕ ");
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

    public static void handleRegister(String username, String password, String email) {
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"email\":\"%s\"}", username, password, email);
        try {
            String response = ServerFacade.sendPostRequest("http://localhost:4510/user", json);
//            System.out.println("Server response: " + response);
            String knownErrorResponse = "{\"message\": \"Error: already taken\"}";

            if (response.equals(knownErrorResponse)) {
                System.out.println("Already Taken username");
                PreLoginUI.display();
            } else {
                System.out.println("Registered successfully");
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                authToken = jsonObject.get("authToken").getAsString();
                currentState = State.LOGGED_IN;
                usernameP = username;
                PostLoginUI.display();
            }



        } catch (Exception e) {
            System.out.println("Failed to register: ");
            PreLoginUI.display();
        }
    }

    public static void handleLogin(String username, String password) {
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        try {
            String response = ServerFacade.sendPostRequest("http://localhost:4510/session", json);

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            authToken = jsonObject.get("authToken").getAsString();
//            System.out.println("Server response: " + response);
            currentState = State.LOGGED_IN;
            usernameP = username;
            PostLoginUI.display();

        }
        catch (Exception e) {
            System.out.println("Unregistered User");
            PreLoginUI.display();
        }
    }



    public static String getAuthToken() {
        return authToken;
    }

    public static void displayMenu() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - to create an account");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - to play chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "quit" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - playing chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - with possible commands");
    }

    public static void displayHelp() {
        System.out.println("Here is a list of available commands and their descriptions:");

        System.out.println("\nregister <USERNAME> <PASSWORD> <EMAIL>");
        System.out.println("    - This command allows you to create a new account. You need to provide a unique username, a password, and a valid email address.");

        System.out.println("\nlogin <USERNAME> <PASSWORD>");
        System.out.println("    - This command allows you to log into your existing account. You need to provide your username and password.");

        System.out.println("\nquit");
        System.out.println("    - This command will exit the application. Use this command when you want to stop playing.");

        System.out.println("\nhelp");
        System.out.println("    - This command displays this help menu, providing details about all available commands.");
    }



    public static String getPrompt() {
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
