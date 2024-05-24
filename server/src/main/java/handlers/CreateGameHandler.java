package handlers;

import requests.CreateGameReq;
import responses.CreateGameRes;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler {
    private CreateGameService gameService;
    private final Gson gson = new Gson();

    public CreateGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.gameService = new CreateGameService(userDAO, gameDAO, authDAO);
    }

    public Route handleCreateGame = (Request request, Response response) -> {

        try {
            CreateGameReq gameRequest = gson.fromJson(request.body(), CreateGameReq.class);
            String authToken = request.headers("Authorization");
            String authsToken = gameService.getAuth(authToken);
            Integer gameID = gameService.createGame(gameRequest.getGameName());

            CreateGameRes gameResponse = new CreateGameRes(gameID);
            response.status(200);
            response.type("application/json");

            return gson.toJson(gameResponse);
        }

        catch (DataAccessException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\"}";
        }
        catch (IllegalArgumentException e) {
            response.status(400);
            return "{\"message\": \"Error: bad request\"}";
        }

    };
}
