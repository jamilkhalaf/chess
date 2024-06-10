package gameplay;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;


import java.util.Collection;
import java.util.Scanner;


public class GameUI {
    private static Scanner scanner;
    private static ChessGame.TeamColor playerColor;
    private static int gameID;
    public static boolean resigned = false;

    public static boolean stopRun = false;

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
                    if (resigned) {
                        System.out.println("Can't make a move, you resigned");
                    }
                    if (stopRun) {
                        PostLoginUI.display();
                    }
                    else if (commandParts.length == 3) {
                        if (PostLoginUI.getGame().getTeamTurn().equals(playerColor)) {
                            ChessMove move = convertToMove(commandParts[1], commandParts[2]);
                            handleMakeMove(move, getGameID());
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
                    if (stopRun) {
                        PostLoginUI.display();
                        break;
                    }
                    handleResign(gameID);
                    break;
                case "redraw":
                    if (stopRun) {
                        PostLoginUI.display();
                        break;
                    }
                    redrawBoard(gameID);
                    break;
                case "highlight":
                    if (stopRun) {
                        PostLoginUI.display();
                        break;
                    }
                    if (commandParts.length == 2) {
                        handleHighlight(convertPosition(commandParts[1]), getGameID());
                    }

                    break;
                case "leave":
                    if (stopRun) {
                        handleLeave();
                        break;
                    }
                    handleLeave();
                    break;
                case "help":
                    if (stopRun) {
                        PostLoginUI.display();
                        break;
                    }
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
        WSClient client = PreLoginUI.wsClient;
        String authToken = PreLoginUI.getAuthToken();
        UserGameCommand gameCommand = new UserGameCommand(authToken, gameID, move, UserGameCommand.CommandType.MAKE_MOVE);

        Gson gson = new Gson();
        String message = gson.toJson(gameCommand);

        client.sendMessage(message);
        GameUI.display();

    }

    private static void handleHighlight(ChessPosition position, Integer gameID) {
        ChessGame game = PostLoginUI.getGame();
        Collection<ChessMove> moves = game.validMoves(position);

        ChessBoard board = PostLoginUI.getBoard(gameID);
        if (playerColor == ChessGame.TeamColor.WHITE) {
            GameUI.displayMenu();
            PostLoginUI.printWhiteBoardHighlighted(moves);

        }
        if (playerColor == ChessGame.TeamColor.BLACK) {
            GameUI.displayMenu();
            PostLoginUI.printBlackBoardHighlighted(moves);

        }
        if (playerColor == ChessGame.TeamColor.empty) {
            GameUI.displayMenu();
            PostLoginUI.printWhiteBoardHighlighted(moves);
            PostLoginUI.printBlackBoardHighlighted(moves);
        }



    }

    private static void handleResign(Integer gameID) {
        System.out.print("Are you sure you want to resign? Press Enter to confirm or type 'cancel' to abort: ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("") || confirmation.equals("confirm")) {
            WSClient client = PreLoginUI.wsClient;
            String authToken = PreLoginUI.getAuthToken();
            UserGameCommand gameCommand = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.RESIGN);

            Gson gson = new Gson();
            String message = gson.toJson(gameCommand);

            client.sendMessage(message);
        }
        GameUI.display();

    }

    public static void handleLeave() {
        System.out.print("Are you sure you want to leave? Press Enter to confirm or type 'cancel' to abort: ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("") || confirmation.equals("confirm")) {
            WSClient client = PreLoginUI.wsClient;
            String authToken = PreLoginUI.getAuthToken();
            UserGameCommand gameCommand = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.LEAVE);

            Gson gson = new Gson();
            String message = gson.toJson(gameCommand);

            client.sendMessage(message);
        }
        PostLoginUI.display();
    }


    public static void redrawBoard(Integer gameID) {
        getBoard(gameID);
    }
}