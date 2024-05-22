package handlers;

import requests.LoginReq;

import responses.LoginRes;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
        try {
            LoginReq userRequest = gson.fromJson(request.body(), LoginReq.class);
            String authToken = loginService.loginUser(userRequest.getUsername(), userRequest.getPassword());

            LoginRes loginResponse = new LoginRes(userRequest.getUsername(), authToken);
            response.type("application/json");
            response.status(200);
            return gson.toJson(loginResponse);
        }
        catch (DataAccessException e) {
            response.status(401);
            return "{\"message\": \"Error: unauthorized\"}";
        }
    };
}
