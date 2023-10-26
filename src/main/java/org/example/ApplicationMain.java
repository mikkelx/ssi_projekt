package org.example;
import org.example.exceptions.ExceptionHandler;
import org.example.controller.GenreController;
import org.example.controller.MovieController;
import org.example.controller.UserController;
import spark.Spark;

import static spark.Spark.get;

public class ApplicationMain {
    public static void main(String[] args) {
        /*
        try {
            MovieDao movieDao = MovieDao.getInstance();
            UserDao userDao = UserDao.getInstance();
            GenreDao genreDao = GenreDao.getInstance();


            Genre genre = new Genre("Horror");
            genreDao.getGenreDao().update(genre);
            Integer id = genreDao.getGenreDao().extractId(genre);
            Movie newMovie = new Movie("Nowy-Tytul", new Date(), 9.0, genre);
            movieDao.getMovieDao().create(newMovie);

            for (Movie movie : movieDao.getMovieDao().queryForAll()) {
                System.out.println(movie);
                System.out.println();
            }
//            User user = new User("test1", "test2", "X", null);
//            userDao.getUserDao().create(user);
//            for (User user1 : userDao.getUserDao().queryForAll()) {
//                System.out.println(user1);
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
         */

        Spark.init();
        MovieController.registerRoutes();
        UserController.registerRoutes();
        GenreController.registerRoutes();
        ExceptionHandler.registerExceptions();
        Spark.awaitInitialization();
        get("/hello", (req, res) -> "Hello World");
    }
}