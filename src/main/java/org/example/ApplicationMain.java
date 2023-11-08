package org.example;
import org.example.exceptions.ExceptionHandler;
import org.example.controller.GenreController;
import org.example.controller.MovieController;
import org.example.controller.UserController;
import org.example.Service.SecurityService;
import spark.Spark;

public class ApplicationMain {
    private static SecurityService securityService = SecurityService.getInstance();

    public static void main(String[] args) {

        Spark.init();

        UserController userController = new UserController();
        MovieController movieController = new MovieController();
        GenreController genreController = new GenreController();

        securityService.registerSecurityRoutes();

        movieController.registerRoutes();
        userController.registerRoutes();
        genreController.registerRoutes();

        ExceptionHandler.registerExceptions();

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        Spark.awaitInitialization();
    }
}