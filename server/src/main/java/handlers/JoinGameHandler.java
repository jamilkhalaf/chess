package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import requests.BaseReq;
import requests.JoinGameReq;
import responses.BaseRes;
import responses.ListGamesRes;
import service.JoinGameService;
import service.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class JoinGameHandler {
    private JoinGameService joinGameService;
    private final Gson gson = new Gson();

    public JoinGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.joinGameService = new JoinGameService(userDAO, gameDAO, authDAO);
    }

    public Route handleJoinGame = (Request request, Response response) -> {

        try {
            JoinGameReq joinGameRequest = gson.fromJson(request.body(), JoinGameReq.class);
            String authToken = request.headers("Authorization");
            String authsToken = joinGameService.getAuth(authToken);

            joinGameService.joinGame(authsToken, joinGameRequest.getGameID(), joinGameRequest.getPlayerColor());
            response.status(200);
            response.type("application/json");
            BaseRes logoutResponse = new BaseRes();
            return gson.toJson(logoutResponse);
        }

        catch (DataAccessException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\"}";
        }
        catch (IllegalArgumentException e) {
            response.status(400);
            return "{\"message\": \"Error: bad request\"}";
        }
        catch (IllegalAccessError e) {
            response.status(403);
            return "{\"message\": \"Error: bad request\"}";
        }

    };
}
