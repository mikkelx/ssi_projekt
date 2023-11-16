package org.example;

import org.example.controller.FavouriteController;
import org.example.controller.GenreController;
import org.example.controller.MovieController;
import org.example.controller.UserController;
import org.example.exceptions.ExceptionHandler;
import org.example.service.SecurityService;
import spark.Spark;

public class ApplicationMain {
    private static SecurityService securityService = SecurityService.getInstance();

    public static void main(String[] args) {

        Spark.init();

        UserController userController = new UserController();
        MovieController movieController = new MovieController();
        GenreController genreController = new GenreController();
        FavouriteController favouriteController = new FavouriteController();

        securityService.registerSecurityRoutes();

        movieController.registerRoutes();
        userController.registerRoutes();
        genreController.registerRoutes();
        favouriteController.registerRoutes();

        ExceptionHandler.registerExceptions();

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        Spark.awaitInitialization();
    }
}