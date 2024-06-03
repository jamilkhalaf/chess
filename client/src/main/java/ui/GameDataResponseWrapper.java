
package ui;


import java.util.List;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
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

public class GameDataResponseWrapper {
    private List<GameData> games;

    public List<GameData> getGames() {
        return games;
    }
}
