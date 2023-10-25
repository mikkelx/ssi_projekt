package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.example.dao.MovieDao;
import org.example.entity.Movie;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class MovieController {

    private static MovieDao movieDao = MovieDao.getInstance();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // data w formacie "yyyy-MM-dd"
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static Route getAllMovies = (Request request, Response response) -> {
        response.type("application/json");
        List<Movie> movies = movieDao.getMovieDao().queryForAll();
        return objectMapper.writeValueAsString(movies);
    };

    public static Route getMovieById = (Request request, Response response) -> {
        response.type("application/json");
        int movieId = Integer.parseInt(request.params("movieId"));
        Movie movie = movieDao.getMovieDao().queryForId(movieId);
        if (movie != null) {
            return objectMapper.writeValueAsString(movie);
        } else {
            response.status(404);
            return "Film o podanym ID nie został znaleziony.";
        }
    };

    public static Route createMovie = (Request request, Response response) -> {
        response.type("application/json");
        Movie newMovie = objectMapper.readValue(request.body(), Movie.class);
        try {
            movieDao.getMovieDao().create(newMovie);
            response.status(201); // Status 201 Created
            return objectMapper.writeValueAsString(newMovie);
        } catch (SQLException e) {
            response.status(500);
            return "Wystąpił błąd podczas zapisywania filmu.";
        }
    };

    public static Route deleteMovie = (Request request, Response response) -> {
        int movieId = Integer.parseInt(request.params("movieId"));
        try {
            int deleted = movieDao.getMovieDao().deleteById(movieId);
            if (deleted == 1) {
                response.status(200);
                return "";
            } else {
                response.status(404);
                return "Film o podanym ID nie został znaleziony.";
            }
        } catch (SQLException e) {
            response.status(500);
            return "Wystąpił błąd podczas usuwania filmu.";
        }
    };

    public static void registerRoutes() {
        get("/movie", getAllMovies);
        get("/movie/:movieId", getMovieById);
        post("/movie", createMovie);
        delete("/movie/:movieId", deleteMovie);
    }

}
