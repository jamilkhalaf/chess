package handlers;

import Requests.BaseReq;
import Requests.CreateGameReq;
import Responses.BaseRes;
import Responses.CreateGameRes;
import Responses.ListGamesRes;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import service.CreateGameService;
import service.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ListGamesHandler {
    private ListGamesService listGamesService;
    private final Gson gson = new Gson();

    public ListGamesHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.listGamesService = new ListGamesService(userDAO, gameDAO, authDAO);
    }

    public Route handleListGames = (Request request, Response response) -> {

        try {
            BaseReq listGamesRequest = gson.fromJson(request.body(), BaseReq.class);
            String authToken = request.headers("Authorization");
            String AuthToken = listGamesService.getAuth(authToken);
            List<GameData> gamesList = listGamesService.listGames(AuthToken);

            ListGamesRes listGamesResponse = new ListGamesRes(gamesList);
            response.status(200);
            response.type("application/json");

            return gson.toJson(listGamesResponse);
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
