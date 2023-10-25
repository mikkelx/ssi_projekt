package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.UserDao;
import org.example.entity.User;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class UserController {

    //TODO - access to all endpoints in this class will be restricted after introducing authorization mechanism

    private static UserDao userDao = UserDao.getInstance();

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
            response.status(404);
            return "Użytkownik o podanym ID nie został znaleziony.";
        }
    };

    public static Route createUser = (Request request, Response response) -> {
        response.type("application/json");
        User newUser = objectMapper.readValue(request.body(), User.class);
        try {
            userDao.getUserDao().create(newUser);
            response.status(201);
            return objectMapper.writeValueAsString(newUser);
        } catch (SQLException e) {
            response.status(500);
            return "Wystąpił błąd podczas tworzenia użytkownika.";
        }
    };

    public static Route deleteUser = (Request request, Response response) -> {
        int userId = Integer.parseInt(request.params("userId"));
        try {
            int deleted = userDao.getUserDao().deleteById(userId);
            if (deleted == 1) {
                response.status(200);
                return "";
            } else {
                response.status(404);
                return "Użytkownik o podanym ID nie został znaleziony.";
            }
        } catch (SQLException e) {
            response.status(500);
            return "Wystąpił błąd podczas usuwania użytkownika.";
        }
    };

    public static void registerRoutes() {
        get("/user", getAllUsers);
        get("/user/:userId", getUserById);
        post("/user", createUser);
        delete("/user/:userId", deleteUser);
    }
}

