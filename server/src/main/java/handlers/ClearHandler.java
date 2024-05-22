package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.ClearService;
import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler {
    private ClearService clearService;
    private final Gson gson = new Gson();

    public ClearHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    public Route handleClearEverything = (Request request, Response response) -> {

        String authToken = request.headers("Authorization");

        if (authToken == null) {
            response.status(401);
            return "No authentication token provided";
        }


        clearService.clearUser(authToken);
        response.status(200);
        return gson.toJson("clear successful");


    };
}
