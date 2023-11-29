package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.exceptions.ResourceNotFoundException;
import org.example.request.LoginRequest;
import org.example.request.RegisterRequest;
import org.example.service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class UserController {

    //TODO - access to all endpoints in this class will be restricted after introducing authorization mechanism

    private static UserDao userDao = UserDao.getInstance();
    private static final String DOMAIN = "Użytkownik";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final UserService userService = UserService.getInstance();


    public Route getAllUsers = (Request request, Response response) -> {
        response.type("application/json");
        List<User> users = userDao.getUserDao().queryForAll().stream().
                peek(user -> user.setPassword(""))
                .collect(Collectors.toList());
        return objectMapper.writeValueAsString(users);
    };

    public Route getUserById = (Request request, Response response) -> {
        response.type("application/json");
        int userId = Integer.parseInt(request.params("userId"));
        User user = userDao.getUserDao().queryForId(userId);
        if (user != null) {
            user.setPassword("");
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
        return "";
    };

    public Route deleteUser = (Request request, Response response) -> {
        int userId = Integer.parseInt(request.params("userId"));
        int deleted = userDao.getUserDao().deleteById(userId);
        if (deleted == 1) {
            response.status(200);
            return "";
        } else {
            throw new ResourceNotFoundException(DOMAIN, userId);
        }
    };

    public Route loginUser = (Request request, Response response) -> {
        response.type("application/json");
        LoginRequest loginRequest = objectMapper.readValue(request.body(), LoginRequest.class);
        String jwt = userService.login(loginRequest);
        response.status(201);
        return jwt;
    };

    public Route updateUser = (Request request, Response response) -> {
        response.type("application/json");
        User newUser = objectMapper.readValue(request.body(), User.class);
        User existingUser = userDao.getUserDao().queryForId(newUser.getId());
        existingUser.setUsername(newUser.getUsername());
        existingUser.setRole(newUser.getRole());
        existingUser.setBlocked(newUser.getBlocked());
        int updated = userDao.getUserDao().update(existingUser);
        if (updated == 1) {
            response.status(204);
            return "";
        } else {
            throw new ResourceNotFoundException(DOMAIN, newUser.getId());
        }
    };

    public Route changePassword = (Request request, Response response) -> {
        response.type("application/json");
        String password = request.body();
        Integer userId = Integer.parseInt(request.params("userId"));
        User user = userDao.getUserDao().queryForId(userId);
        user.setPassword(password);
        int updated = userDao.getUserDao().update(user);
        if (updated == 1) {
            response.status(204);
            return "";
        } else {
            throw new ResourceNotFoundException(DOMAIN, userId);
        }
    };

    public void registerRoutes() {
        get("/admin/user", getAllUsers);
        get("/admin/user/:userId", getUserById);
        post("/user/register", registerUser);
        post("/user/login", loginUser);
        put("/admin/user", updateUser);
        put("/admin/user/changePassword/:userId", changePassword);
        delete("/admin/user/:userId", deleteUser);
    }


}

