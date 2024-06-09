package gameplay;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDAO;
import model.GameData;
import websocket.commands.UserGameCommand;


import java.util.Scanner;


public class GameUI {
    private static Scanner scanner;
    private static ChessGame.TeamColor playerColor;
    private static int gameID;



    public static void init() {
        scanner = new Scanner(System.in);

    }

    public static ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public static void setPlayerColor(ChessGame.TeamColor playerColor) {
        GameUI.playerColor = playerColor;
    }

    public static int getGameID() {
        return gameID;
    }

    public static void setGameID(int gameID) {
        GameUI.gameID = gameID;
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
                    if (commandParts.length == 3) {
                        if (PostLoginUI.getGame().getTeamTurn().equals(playerColor)) {
                            ChessMove move = convertToMove(commandParts[1], commandParts[2]);
                            handleMakeMove(move, gameID);
                        }
                        else {
                            System.out.println("Not your turn");
                            GameUI.displayMenu();
                        }
                    } else {
                        System.out.println("Usage: make move <initial position> <final position> <gameID>");
                    }
                    break;
                case "resign":
                    handleResign(gameID, playerColor);
                    break;
                case "redraw":
                    redrawBoard(gameID);
                    break;
                case "highlight":
                    break;
                case "leave":
                    handleLeave();
                    break;
                case "help":
                    displayHelp();
                    GameUI.displayMenu();
                    break;
                default:
                    PostLoginUI.display();
            }
        }
    }


    public static void displayMenu() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "make-move" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "resign" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "redraw" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Highlight" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
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

    public static void getBoard(Integer gameID) {
        ChessBoard board = PostLoginUI.getBoard(gameID);
        if (playerColor == ChessGame.TeamColor.WHITE) {
            PostLoginUI.printWhiteBoard();
        }
        if (playerColor == ChessGame.TeamColor.BLACK) {
            PostLoginUI.printBlackBoard();
        }
        if (playerColor == ChessGame.TeamColor.empty) {
            PostLoginUI.printWhiteBoard();
            PostLoginUI.printBlackBoard();
        }
    }

    private static void handleMakeMove(ChessMove move, Integer gameID) {
//        WSClient client = PreLoginUI.wsClient;
//        String authToken = PreLoginUI.getAuthToken();
//        UserGameCommand gameCommand = new UserGameCommand(authToken, gameID, move);
//
//        Gson gson = new Gson();
//        String message = gson.toJson(gameCommand);
//
//        System.out.println("Sending message: " + message);
//
//        client.sendMessage(message);
        GameUI.display();
        System.out.println(
        );

    }

    private static void handleResign(Integer gameID, ChessGame.TeamColor playerColor) {
//        WSClient client = PreLoginUI.wsClient;
//        String authToken = PreLoginUI.getAuthToken();
//        UserGameCommand gameCommand = new UserGameCommand(authToken, gameID, playerColor);
//        gameCommand.setCommandType(UserGameCommand.CommandType.RESIGN);
//
//        Gson gson = new Gson();
//        String message = gson.toJson(gameCommand);
//
//        System.out.println("Sending message: " + message);
//
//        client.sendMessage(message);
        GameUI.display();
        System.out.println(
        );

    }

    public static void handleLeave() {
        PostLoginUI.display();
    }


    public static void redrawBoard(Integer gameID) {
        getBoard(gameID);
    }
}