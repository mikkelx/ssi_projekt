package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.example.dao.MovieDao;
import org.example.entity.Movie;
import org.example.exceptions.ResourceNotFoundException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static spark.Spark.*;

public class MovieController {

    private static MovieDao movieDao = MovieDao.getInstance();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DOMAIN = "Film";

    static {
        // data w formacie "yyyy-MM-dd"
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public Route getAllMovies = (Request request, Response response) -> {
        response.type("application/json");
        List<Movie> movies = movieDao.getMovieDao().queryForAll();
        return objectMapper.writeValueAsString(movies);
    };

    public Route getMovieById = (Request request, Response response) -> {
        response.type("application/json");
        int movieId = Integer.parseInt(request.params("movieId"));
        Movie movie = movieDao.getMovieDao().queryForId(movieId);
        if (movie != null) {
            return objectMapper.writeValueAsString(movie);
        } else {
            throw new ResourceNotFoundException(DOMAIN, movieId);
        }
    };

    public Route createMovie = (Request request, Response response) -> {
        response.type("application/json");
        Movie newMovie = objectMapper.readValue(request.body(), Movie.class);
        int id = movieDao.getMovieDao().create(newMovie);
        response.status(201); // Status 201 Created
        return objectMapper.writeValueAsString(id);
    };

    public Route deleteMovie = (Request request, Response response) -> {
        int movieId = Integer.parseInt(request.params("movieId"));
        int deleted = movieDao.getMovieDao().deleteById(movieId);
        if (deleted == 1) {
            response.status(200);
            return "";
        } else {
            throw new ResourceNotFoundException(DOMAIN, movieId);
        }
    };

    public Route updateMovie = (Request request, Response response) -> {
        response.type("application/json");
        Movie movie = objectMapper.readValue(request.body(), Movie.class);
        int updated = movieDao.getMovieDao().update(movie);
        if (updated == 1) {
            response.status(204);
            return "";
        } else {
            throw new ResourceNotFoundException(DOMAIN, movie.getId());
        }
    };



    public Route getMoviesByReleaseDate = (Request request, Response response) -> {
        response.type("application/json");
        String releaseDateString = request.params("releaseDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = dateFormat.parse(releaseDateString);
        List<Movie> movies = movieDao.getMoviesByReleaseDate(releaseDate);
        return objectMapper.writeValueAsString(movies);
    };


    public Route getMoviesByRating = (Request request, Response response) -> {
        response.type("application/json");
        double minRating = Double.parseDouble(request.params("minRating"));
        List<Movie> movies = movieDao.getMoviesByRating(minRating);
        return objectMapper.writeValueAsString(movies);
    };

    public Route getMoviesByGenreName = (Request request, Response response) -> {
        response.type("application/json");
        String genreName = request.params("genreName");
        List<Movie> movies = movieDao.getMoviesByGenreName(genreName);
        return objectMapper.writeValueAsString(movies);
    };

    public void registerRoutes() {
        get("/movie", getAllMovies);
        get("/movie/:movieId", getMovieById);
        get("/movie/byDate/:releaseDate", getMoviesByReleaseDate);
        get("/movie/byRating/:minRating", getMoviesByRating);
        get("/movie/byGenre/:genreName", getMoviesByGenreName);
        post("/admin/movie", createMovie);
        put("/admin/movie", updateMovie);
        delete("/admin/movie/:movieId", deleteMovie);
    }

}
