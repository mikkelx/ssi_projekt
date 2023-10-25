package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.GenreDao;
import org.example.entity.Genre;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class GenreController {

    private static GenreDao genreDao = GenreDao.getInstance();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Route getAllGenres = (Request request, Response response) -> {
        response.type("application/json");
        List<Genre> genres = genreDao.getGenreDao().queryForAll();
        return objectMapper.writeValueAsString(genres);
    };

    //TODO - add searching genre by it's name
    public static Route getGenreByName = (Request request, Response response) -> {
        response.type("application/json");
        String genreName = request.params("genreName");
        Genre genre = genreDao.getGenreByName(genreName);
        if (genre != null) {
            return objectMapper.writeValueAsString(genre);
        } else {
            response.status(404);
            return "Gatunek o podanym genreName nie został znaleziony.";
        }
    };

    public static Route getGenreById = (Request request, Response response) -> {
        response.type("application/json");
        int genreId = Integer.parseInt(request.params("genreId"));
        Genre genre = genreDao.getGenreDao().queryForId(genreId);
        if (genre != null) {
            return objectMapper.writeValueAsString(genre);
        } else {
            response.status(404);
            return "Gatunek o podanym ID nie został znaleziony.";
        }
    };

    public static Route createGenre = (Request request, Response response) -> {
        response.type("application/json");
        Genre newGenre = objectMapper.readValue(request.body(), Genre.class);
        try {
            genreDao.getGenreDao().create(newGenre);
            response.status(201);
            return objectMapper.writeValueAsString(newGenre);
        } catch (SQLException e) {
            response.status(500);
            return "Wystąpił błąd podczas tworzenia gatunku.";
        }
    };

    public static Route deleteGenre = (Request request, Response response) -> {
        int genreId = Integer.parseInt(request.params("genreId"));
        try {
            int deleted = genreDao.getGenreDao().deleteById(genreId);
            if (deleted == 1) {
                response.status(200);
                return "";
            } else {
                response.status(404);
                return "Gatunek o podanym ID nie został znaleziony.";
            }
        } catch (SQLException e) {
            response.status(500);
            return "Wystąpił błąd podczas usuwania gatunku.";
        }
    };

    public static void registerRoutes() {
        get("/genre", getAllGenres);
        get("/genre/:genreId", getGenreById);
        get("/genre/name/:genreName", getGenreByName);
        post("/genre", createGenre);
        delete("/genre/:genreId", deleteGenre);
    }
}

