package ui;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import model.GameData;


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
            System.out.print(EscapeSequences.RESET_TEXT_COLOR + PreLoginUI.getPrompt());
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
                        GameUI.display();
                    } else {
                        System.out.println("Usage: create <gameName>");
                    }

                    break;
                case "observe":
                    if (commandParts.length == 2) {
                        handleObserveGame(Integer.parseInt(commandParts[1]));
                        getBoard(Integer.parseInt(commandParts[1]));
                        GameUI.display();
                        GameUI.setColor("Observer");
                    } else {
                        System.out.println("Usage: create <gameName>");
                    }
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
            String response = ServerFacade.sendGameRequest("http://localhost:4510/game", json, PreLoginUI.getAuthToken());
            System.out.println("Server response: " + response);

        } catch (Exception e) {
            System.out.println("Failed to register: " + e.getMessage());
        }
    }

    public static ChessBoard getBoard(Integer gameID) {
        try {
            String response = ServerFacade.sendGetRequest("http://localhost:4510/game",PreLoginUI.getAuthToken());

            GameDataResponseWrapper responseData = new Gson().fromJson(response, GameDataResponseWrapper.class);
            List<GameData> gameDataList = responseData.getGames();

            for (GameData gameData : gameDataList) {
                if (gameData.getGameID() == gameID) {
                    game = gameData.getGame();

                    board = game.getBoard();


                }
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch games: " + e.getMessage());
        }
        return board;
    }

    private static void handleListGames() {
        try {
            String response = ServerFacade.sendGetRequest("http://localhost:4510/game",PreLoginUI.getAuthToken());
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
            String response = ServerFacade.sendPutRequest(url, json, PreLoginUI.getAuthToken());
            String knownErrorResponse = "{\"message\": \"Error: bad request\"}";

            if (response.equals(knownErrorResponse)) {
                System.out.println("Game Not found");
            } else {
                System.out.println("Joined Game");
                getBoard(gameID);
                if (playerColor.equals("BLACK")) {
                    printBlackBoard();
                    GameUI.setColor("BLACK");
                }
                if (playerColor.equals("WHITE")) {
                    printWhiteBoard();
                    GameUI.setColor("WHITE");
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }

    private static void handleObserveGame(Integer gameID) {
        String playerColor = "empty";
        try {
            String url = "http://localhost:4510/game";
            String json = "{\"playerColor\": \"" + playerColor + "\", \"gameID\": " + gameID + "}";
            String response = ServerFacade.sendPutRequest(url, json, PreLoginUI.getAuthToken());
            String knownErrorResponse = "{\"message\": \"Error: bad request\"}";
            if (response.equals(knownErrorResponse)) {
                System.out.println("Game Not found");
            } else {
                System.out.println("Observing game");
                getBoard(gameID);
                printWhiteBoard();
                printBlackBoard();
                GameUI.setColor("Observer");
            }
        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }


    private static void handleLogout() {
        try {
            String response = ServerFacade.sendDeleteRequest("http://localhost:4510/session", PreLoginUI.getAuthToken());
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            System.out.println("Logout Success");
            PreLoginUI.setCurrentState(PreLoginUI.State.LOGGED_OUT);
            PreLoginUI.display();



        } catch (Exception e) {
            System.out.println("Failed to logout: " + e.getMessage());
        }
    }


    public static void printWhiteBoard() {
        System.out.println(EscapeSequences.ERASE_SCREEN);

        System.out.print("   ");
        for (int i = 0; i < 8; i++) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + (char)('a' + i) + " " + EscapeSequences.RESET_BG_COLOR);
        }
        System.out.println();

        for (int row = 8; row >= 1; row--) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + row + " " + EscapeSequences.RESET_BG_COLOR);

            for (int column = 1; column <= 8; column++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, column));
                String pieceSymbol = getPieceSymbol(piece);
                boolean isBlackSquare = (row + column) % 2 != 0;
                String square = isBlackSquare ? String.format(EscapeSequences.SET_BG_COLOR_BLACK + pieceSymbol + EscapeSequences.RESET_BG_COLOR) : String.format(EscapeSequences.SET_BG_COLOR_WHITE + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
                System.out.print(square);
            }
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + row + " " + EscapeSequences.RESET_BG_COLOR);
        }

        System.out.print("   ");
        for (int i = 0; i < 8; i++) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + (char)('a' + i) + " " + EscapeSequences.RESET_BG_COLOR);
        }
        System.out.println();
    }

    public static void printBlackBoard() {
        System.out.println(EscapeSequences.ERASE_SCREEN);

        System.out.print("   ");
        for (int i = 7; i >= 0; i--) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + (char)('a' + i) + " " + EscapeSequences.RESET_BG_COLOR);
        }
        System.out.println();

        for (int row = 1; row <= 8; row++) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + row + " " + EscapeSequences.RESET_BG_COLOR);

            for (int col = 8; col >= 1; col--) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String pieceSymbol = getPieceSymbol(piece);
                boolean isBlackSquare = (row + col) % 2 != 0;
                String square = isBlackSquare ? String.format(EscapeSequences.SET_BG_COLOR_BLACK + pieceSymbol + EscapeSequences.RESET_BG_COLOR) : String.format(EscapeSequences.SET_BG_COLOR_WHITE + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
                System.out.print(square);
            }
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + row + " " + EscapeSequences.RESET_BG_COLOR);
        }

        System.out.print("   ");
        for (int i = 7; i >= 0; i--) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + " " + (char)('a' + i) + " " + EscapeSequences.RESET_BG_COLOR);
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
                        return EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.WHITE_KING + EscapeSequences.RESET_BG_COLOR;
                    case QUEEN:
                        return EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.WHITE_QUEEN+ EscapeSequences.RESET_BG_COLOR;
                    case BISHOP:
                        return EscapeSequences.SET_TEXT_COLOR_BLUE +EscapeSequences.WHITE_BISHOP+ EscapeSequences.RESET_BG_COLOR;
                    case KNIGHT:
                        return EscapeSequences.SET_TEXT_COLOR_BLUE +EscapeSequences.WHITE_KNIGHT+ EscapeSequences.RESET_BG_COLOR;
                    case ROOK:
                        return EscapeSequences.SET_TEXT_COLOR_BLUE +EscapeSequences.WHITE_ROOK+ EscapeSequences.RESET_BG_COLOR;
                    case PAWN:
                        return EscapeSequences.SET_TEXT_COLOR_BLUE +EscapeSequences.WHITE_PAWN+ EscapeSequences.RESET_BG_COLOR;
                }
                break;
            case BLACK:
                switch (type) {
                    case KING:
                        return EscapeSequences.SET_TEXT_COLOR_RED +EscapeSequences.BLACK_KING+ EscapeSequences.RESET_BG_COLOR;
                    case QUEEN:
                        return EscapeSequences.SET_TEXT_COLOR_RED +EscapeSequences.BLACK_QUEEN+ EscapeSequences.RESET_BG_COLOR;
                    case BISHOP:
                        return EscapeSequences.SET_TEXT_COLOR_RED +EscapeSequences.BLACK_BISHOP+ EscapeSequences.RESET_BG_COLOR;
                    case KNIGHT:
                        return EscapeSequences.SET_TEXT_COLOR_RED +EscapeSequences.BLACK_KNIGHT+ EscapeSequences.RESET_BG_COLOR;
                    case ROOK:
                        return EscapeSequences.SET_TEXT_COLOR_RED +EscapeSequences.BLACK_ROOK+ EscapeSequences.RESET_BG_COLOR;
                    case PAWN:
                        return EscapeSequences.SET_TEXT_COLOR_RED +EscapeSequences.BLACK_PAWN+ EscapeSequences.RESET_BG_COLOR;
                }
                break;
        }
        return EscapeSequences.EMPTY;
    }
}