package ui;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import model.GameData;
import server.Server;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;



public class PostLoginUI {
    private static Scanner scanner;
    private static State currentState = State.LOGGED_IN;
    private static ChessBoard board = new ChessBoard();
    private static ChessGame game = new ChessGame();
    private static Gson gson = new Gson();


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
                        PostLoginUI.display();
                    } else {
                        System.out.println("Usage: create <gameName>");
                    }
                    break;
                case "list":
                    handleListGames();

                    System.exit(0);
                    break;
                case "join":
                    if (commandParts.length == 3) {
                        handleJoinGame(Integer.parseInt(commandParts[1]),commandParts[2]);
                        getBoard(Integer.parseInt(commandParts[1]));
                        GameUI.display();
                    } else {
                        System.out.println("Usage: create <gameName>");
                    }

                    break;
                case "observe":

                    break;
                case "logout":
                    handleLogout();

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

        } catch (Exception e) {
            System.out.println("Failed to register: " + e.getMessage());
        }
    }

    private static void getBoard(Integer gameID) {
        try {
            String response = HandleClientRequest.sendGetRequest("http://localhost:4510/game");
            System.out.println("Server response: " + response);

            GameDataResponseWrapper responseData = new Gson().fromJson(response, GameDataResponseWrapper.class);
            List<GameData> gameDataList = responseData.getGames();

            for (GameData gameData : gameDataList) {
                if (gameData.getGameID() == gameID) {
                    game = gameData.getGame();
                    game.makeMove(new ChessMove(new ChessPosition(2,1),new ChessPosition(4,1),null));
                    // Make aggressive moves to lead to a fast checkmate
                    game.makeMove(new ChessMove(new ChessPosition(7, 6), new ChessPosition(6, 6), null)); // Bishop to f5
                    game.makeMove(new ChessMove(new ChessPosition(2, 4), new ChessPosition(4, 4), null));
                    game.makeMove(new ChessMove(new ChessPosition(7, 1), new ChessPosition(6, 1), null));
                    game.makeMove(new ChessMove(new ChessPosition(2, 5), new ChessPosition(4, 5), null));
                    game.makeMove(new ChessMove(new ChessPosition(7, 7), new ChessPosition(5, 7), null));// Pawn to d4
                    game.makeMove(new ChessMove(new ChessPosition(1, 4), new ChessPosition(5, 8), null));// Pawn to d4

// Pawn to d4

                    System.out.println(game.isInCheckmate(ChessGame.TeamColor.BLACK));
                    System.out.println(game.isInCheckmate(ChessGame.TeamColor.WHITE));
                    board = game.getBoard();
                    printWhiteBoard();
                    printBlackBoard();
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch games: " + e.getMessage());
        }
    }

    private static void handleListGames() {
        try {
            String response = HandleClientRequest.sendGetRequest("http://localhost:4510/game");
            System.out.println("Server response: " + response);
            PostLoginUI.display();
        } catch (Exception e) {
            System.out.println("Failed to fetch games: " + e.getMessage());
        }
    }

    private static void handleJoinGame(Integer gameID, String playerColor) {
        try {
            playerColor = playerColor.toUpperCase();
            String url = "http://localhost:4510/game";
            String json = "{\"playerColor\": \"" + playerColor + "\", \"gameID\": " + gameID + "}";
            String response = HandleClientRequest.sendPutRequest(url, json);
            System.out.println("Server response: " + response);
        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }


    private static void handleLogout() {
        try {
            String response = HandleClientRequest.sendDeleteRequest("http://localhost:4510/session");
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            System.out.println("Server response: " + response);
            PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_OUT);
            PreLoginUI.display();



        } catch (Exception e) {
            System.out.println("Failed to logout: " + e.getMessage());
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

    private static void printWhiteBoard() {
        System.out.println("White Perspective:");
        System.out.println(EscapeSequences.ERASE_SCREEN);

        System.out.print("   ");
        for (int i = 0; i < 8; i++) {
            System.out.print(String.format(" "  + (char)('a' + i) + " "));
        }
        System.out.println();

        for (int row = 8; row >= 1; row--) {
            System.out.print(" " + row + " ");

            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String pieceSymbol = getPieceSymbol(piece);
                boolean isBlackSquare = (row + col) % 2 != 0;
                String square = isBlackSquare ? String.format(EscapeSequences.SET_BG_COLOR_BLACK + pieceSymbol + EscapeSequences.RESET_BG_COLOR) : String.format(EscapeSequences.SET_BG_COLOR_WHITE + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
                System.out.print(square);
            }
            System.out.println(" " + row);
        }

        System.out.print("   ");
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + (char)('a' + i) + " ");
        }
        System.out.println();
    }

    private static void printBlackBoard() {
        System.out.println("Black Perspective:");
        System.out.println(EscapeSequences.ERASE_SCREEN);

        System.out.print("   ");
        for (int i = 7; i >= 0; i--) {
            System.out.print(" " + (char)('a' + i) + " ");
        }
        System.out.println();

        for (int row = 1; row <= 8; row++) {
            System.out.print(" " + row + " ");

            for (int col = 8; col >= 1; col--) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String pieceSymbol = getPieceSymbol(piece);
                boolean isBlackSquare = (row + col) % 2 != 0;
                String square = isBlackSquare ? String.format(EscapeSequences.SET_BG_COLOR_BLACK + pieceSymbol + EscapeSequences.RESET_BG_COLOR) : String.format(EscapeSequences.SET_BG_COLOR_WHITE + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
                System.out.print(square);
            }
            System.out.println(" " + row);
        }

        System.out.print("   ");
        for (int i = 7; i >= 0; i--) {
            System.out.print(" " + (char)('a' + i) + " ");
        }
        System.out.println();
    }






    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();
        switch (color) {
            case WHITE:
                switch (type) {
                    case KING:
                        return EscapeSequences.WHITE_KING;
                    case QUEEN:
                        return EscapeSequences.WHITE_QUEEN;
                    case BISHOP:
                        return EscapeSequences.WHITE_BISHOP;
                    case KNIGHT:
                        return EscapeSequences.WHITE_KNIGHT;
                    case ROOK:
                        return EscapeSequences.WHITE_ROOK;
                    case PAWN:
                        return EscapeSequences.WHITE_PAWN;
                }
                break;
            case BLACK:
                switch (type) {
                    case KING:
                        return EscapeSequences.BLACK_KING;
                    case QUEEN:
                        return EscapeSequences.BLACK_QUEEN;
                    case BISHOP:
                        return EscapeSequences.BLACK_BISHOP;
                    case KNIGHT:
                        return EscapeSequences.BLACK_KNIGHT;
                    case ROOK:
                        return EscapeSequences.BLACK_ROOK;
                    case PAWN:
                        return EscapeSequences.BLACK_PAWN;
                }
                break;
        }
        return EscapeSequences.EMPTY;
    }
}