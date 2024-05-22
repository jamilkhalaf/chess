package handlers;

import Requests.LoginReq;
import Responses.LoginRes;
import Responses.LogoutRes;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.LoginService;
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

        String authToken = request.headers("Authorization");

        if (authToken == null) {
            response.status(401);
            return "No authentication token provided";
        }


        logoutService.logoutUser(authToken);
        response.status(200);
        return gson.toJson("Logout successful");


    };
}
