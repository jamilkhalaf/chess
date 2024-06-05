package ui;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDAO;


import java.util.Scanner;


public class GameUI {
    private static Scanner scanner;
    private static State currentState = State.LOGGED_IN;
    private static String color;

    public static void init() {
        scanner = new Scanner(System.in);
    }

    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }

    public static void setColor(String color) {
        GameUI.color = color;
    }

    public static void display() {
        init();
        displayMenu();
        while (true) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR + PreLoginUI.getPrompt());
            String command = scanner.nextLine().trim().toLowerCase();
            String[] commandParts = command.split(" ");
            switch (commandParts[0]) {
                case "make-move":
                    if (commandParts.length == 4) {
                        ChessMove move = convertToMove(commandParts[1], commandParts[2]);
                        handleMakeMove(move, Integer.parseInt(commandParts[3]));
                    } else {
                        System.out.println("Usage: make move <initial position> <final position> <gameID>");
                    }
                    break;
                case "resign":
                    break;
                case "redraw":
                    if (commandParts.length == 2) {
                        redrawBoard(Integer.parseInt(commandParts[1]));
                    } else {
                        System.out.println("Usage: make move <initial position> <final position> <gameID>");
                    }
                case "highlight valid moves":
                    PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_OUT);
                    PreLoginUI.display();
                    break;
                case "leave":
                    PostLoginUI.display();
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
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "make-move" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "resign" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "redraw" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Highlight Legal Moves" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Leave" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - playing chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - with possible commands");
    }

    public static void displayHelp() {
        System.out.println("Here is the list of available commands");
    }

    private static ChessMove convertToMove(String initialPosition, String finalPosition) {
        return new ChessMove(convertPosition(initialPosition), convertPosition(finalPosition),null);
    }

    private static ChessPosition convertPosition(String position) {
        if (position == null || position.length() != 2) {
            throw new IllegalArgumentException("Invalid position format. Expected format is 'a1', 'b2', etc.");
        }
        char columnChar = position.charAt(0);
        char rowChar = position.charAt(1);

        int column = columnChar - 'a' + 1;
        int row = rowChar - '1' + 1;

        if (column < 1 || column > 8 || row < 1 || row > 8) {
            throw new IllegalArgumentException("Invalid position. Column must be a-h and row must be 1-8.");
        }

        return new ChessPosition(row,column);
    }

    private static void getBoard(Integer gameID) {
        ChessBoard board = PostLoginUI.getBoard(gameID);
        if (color.equals("WHITE")) {
            PostLoginUI.printWhiteBoard();
        }
        if (color.equals("BLACK")) {
            PostLoginUI.printBlackBoard();
        }
        if (color.equals("Observer")) {
            PostLoginUI.printWhiteBoard();
            PostLoginUI.printBlackBoard();
        }
    }

    private static void handleMakeMove(ChessMove move, Integer gameID) {
        try {
            SQLGameDAO gameDao = new SQLGameDAO();
            gameDao.makeChessMove(move, gameID);
        }
        catch (DataAccessException e) {
            System.out.println("error");
            redrawBoard(gameID);
            GameUI.display();
        }

        getBoard(gameID);

        GameUI.display();
    }

    private static void redrawBoard(Integer gameID) {
        getBoard(gameID);
        GameUI.display();
    }


}