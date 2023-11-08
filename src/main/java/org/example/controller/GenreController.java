package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.GenreDao;
import org.example.entity.Genre;
import org.example.exceptions.ResourceNotFoundException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class GenreController {

    private static GenreDao genreDao = GenreDao.getInstance();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DOMAIN = "Gatunek";

    public static Route getAllGenres = (Request request, Response response) -> {
        response.type("application/json");
        List<Genre> genres = genreDao.getGenreDao().queryForAll();
        return objectMapper.writeValueAsString(genres);
    };

    public static Route getGenreByName = (Request request, Response response) -> {
        response.type("application/json");
        String genreName = request.params("genreName");
        Genre genre = genreDao.getGenreByName(genreName);
        return objectMapper.writeValueAsString(genre);
    };

    public static Route getGenreById = (Request request, Response response) -> {
        response.type("application/json");
        int genreId = Integer.parseInt(request.params("genreId"));
        Genre genre = genreDao.getGenreDao().queryForId(genreId);
        if (genre != null) {
            return objectMapper.writeValueAsString(genre);
        } else {
            throw new ResourceNotFoundException(DOMAIN, genreId);
        }
    };

    public static Route createGenre = (Request request, Response response) -> {
        response.type("application/json");
        Genre newGenre = objectMapper.readValue(request.body(), Genre.class);
        genreDao.getGenreDao().create(newGenre);
        response.status(201);
        return objectMapper.writeValueAsString(newGenre);
    };

    public static Route deleteGenre = (Request request, Response response) -> {
        int genreId = Integer.parseInt(request.params("genreId"));
        int deleted = genreDao.getGenreDao().deleteById(genreId);
        if (deleted == 1) {
            response.status(200);
            return "";
        } else {
            throw new ResourceNotFoundException(DOMAIN, genreId);
        }
    };

    public static void registerRoutes() {
        get("/genre", getAllGenres);
        get("/genre/:genreId", getGenreById);
        get("/genre/name/:genreName", getGenreByName);
        post("/admin/genre", createGenre);
        delete("/admin/genre/:genreId", deleteGenre);
    }
}

