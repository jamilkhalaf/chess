package handlers;

import responses.BaseRes;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler {
    private LogoutService logoutService;
    private final Gson gson = new Gson();

    public LogoutHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.logoutService = new LogoutService(userDAO, gameDAO, authDAO);
    }

    public Route handleLogoutUser = (Request request, Response response) -> {
        try {
            String authToken = request.headers("Authorization");

            logoutService.logoutUser(authToken);
            response.status(200);
            BaseRes logoutResponse = new BaseRes();
            return gson.toJson(logoutResponse);
        }
        catch (DataAccessException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\"}";

        }

    };
}
