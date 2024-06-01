package ui;

import server.Server;
import java.util.Scanner;


public class PostLoginUI {
    private static Scanner scanner;
    private static State currentState = State.LOGGED_IN;

    public static void init() {
        scanner = new Scanner(System.in);
    }

    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }

    public static void display() {
        init();
        displayMenu();
        while (true) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR + getPrompt());
            String command = scanner.nextLine().trim().toLowerCase();
            String[] commandParts = command.split(" ");
            switch (commandParts[0]) {
                case "create":
                    if (commandParts.length == 2) {
                        handleCreateGame(commandParts[1]);
                    } else {
                        System.out.println("Usage: create <gameName>");
                    }
                    break;
                case "list":
                    System.exit(0);
                    break;
                case "join":
                    break;
                case "observe":

                    break;
                case "logout":
                    PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_OUT);
                    PreLoginUI.display();
                    break;
                case "quit":
                    PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_OUT);
                    PreLoginUI.display();
                    break;
                case "help":
                    displayHelp();
                    break;
                default:
                    System.out.println("Unknown command. Type 'Help' for a list of commands.");
            }
        }
    }


    public static void displayMenu() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "create <NAME>" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - a game");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "list" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - games");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - a game");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "observe <ID>" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - a game");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "logout" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - when you are done");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "quit" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - playing chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - with possible commands");
    }

    public static void displayHelp() {
        System.out.println("helping");
    }

    private static void handleCreateGame(String gameName) {
        String json = String.format("{\"gameName\":\"%s\"}", gameName);
        try {
            String response = HandleClientRequest.sendGameRequest("http://localhost:4510/game", json);
            System.out.println("Server response: " + response);

            GameUI.display();

        } catch (Exception e) {
            System.out.println("Failed to register: " + e.getMessage());
        }
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