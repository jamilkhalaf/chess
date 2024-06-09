package gameplay;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;



import java.util.Collection;
import java.util.List;
import java.util.Scanner;


public class ObserverGameUI {
    private static Scanner scanner;
    private static ChessGame.TeamColor playerColor;
    private static int gameID;
    public static boolean resigned = false;



    public static void init() {
        scanner = new Scanner(System.in);

    }

    public static ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public static void setPlayerColor(ChessGame.TeamColor playerColor) {
        ObserverGameUI.playerColor = playerColor;
    }

    public static int getGameID() {
        return gameID;
    }

    public static void setGameID(int gameID) {
        ObserverGameUI.gameID = gameID;
    }



    public static void display() {
        init();
        displayMenu();
        while (true) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR + PreLoginUI.getPrompt());

            String command = scanner.nextLine().trim().toLowerCase();
            String[] commandParts = command.split(" ");

            switch (commandParts[0]) {

                case "redraw":
                    redrawBoard(gameID);
                    break;
                case "highlight":
                    if (commandParts.length == 2) {
                        handleHighlight(convertPosition(commandParts[1]), getGameID());
                    }

                    break;
                case "leave":
                    handleLeave();
                    break;
                case "help":
                    displayHelp();
                    ObserverGameUI.displayMenu();
                    break;
                default:
                    ObserverGameUI.displayMenu();
            }
        }
    }


    public static void displayMenu() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "redraw" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Highlight" + EscapeSequences.SET_TEXT_COLOR_MAGENTA );
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Leave" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - playing chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + " - with possible commands");
    }

    public static void displayHelp() {
        System.out.println("Here is the list of available commands");
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


    public static void handleLeave() {
        PostLoginUI.display();
    }


    public static void redrawBoard(Integer gameID) {
        getBoard(gameID);
    }
}