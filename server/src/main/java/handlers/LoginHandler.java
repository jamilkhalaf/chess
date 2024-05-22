package handlers;

import Requests.LoginReq;

import Responses.LoginRes;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler {
    private LoginService loginService;
    private final Gson gson = new Gson();

    public LoginHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.loginService = new LoginService(userDAO, gameDAO, authDAO);
    }

    public Route handleLoginUser = (Request request, Response response) -> {

        LoginReq userRequest = gson.fromJson(request.body(), LoginReq.class);
        String authToken = loginService.loginUser(userRequest.getUsername(), userRequest.getPassword());

        LoginRes loginResponse = new LoginRes(userRequest.getUsername(), authToken);
        response.type("application/json");

        return gson.toJson(loginResponse);

    };
}
