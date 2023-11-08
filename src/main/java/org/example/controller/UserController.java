package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Service.UserService;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.exceptions.ResourceNotFoundException;
import org.example.util.RegisterRequest;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class UserController {

    //TODO - access to all endpoints in this class will be restricted after introducing authorization mechanism

    private static UserDao userDao = UserDao.getInstance();
    private static final String DOMAIN = "Użytkownik";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private UserService userService = UserService.getInstance();


    public static Route getAllUsers = (Request request, Response response) -> {
        response.type("application/json");
        List<User> users = userDao.getUserDao().queryForAll();
        return objectMapper.writeValueAsString(users);
    };

    public static Route getUserById = (Request request, Response response) -> {
        response.type("application/json");
        int userId = Integer.parseInt(request.params("userId"));
        User user = userDao.getUserDao().queryForId(userId);
        if (user != null) {
            return objectMapper.writeValueAsString(user);
        } else {
            throw new ResourceNotFoundException(DOMAIN, userId);
        }
    };

    public Route registerUser = (Request request, Response response) -> {
        response.type("application/json");
        RegisterRequest registerRequest = objectMapper.readValue(request.body(), RegisterRequest.class);
        userService.register(registerRequest);
        response.status(201);
        return objectMapper.writeValueAsString(id);
    };

    public static Route deleteUser = (Request request, Response response) -> {
        int userId = Integer.parseInt(request.params("userId"));
        int deleted = userDao.getUserDao().deleteById(userId);
        if (deleted == 1) {
            response.status(200);
            return "";
        } else {
            throw new ResourceNotFoundException(DOMAIN, userId);
        }
    };

    public static void registerRoutes() {
        get("/user", getAllUsers);
        get("/user/:userId", getUserById);
        post("/user", registerUser);
        delete("/user/:userId", deleteUser);
    }
}

