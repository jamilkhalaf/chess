package handlers;

import Responses.BaseRes;
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

        clearService.clearUser();
        response.status(200);
        BaseRes clearResponse = new BaseRes();
        return gson.toJson(clearResponse);


    };
}
