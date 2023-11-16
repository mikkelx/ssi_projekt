package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.FavouriteDao;
import org.example.entity.Favourite;
import org.example.entity.Movie;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.get;
import static spark.Spark.post;

public class FavouriteController {

    private static FavouriteDao favouriteDao = FavouriteDao.getInstance();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DOMAIN = "Ulubiony film";

    public Route getFavouritesForUser = (Request request, Response response) -> {
        response.type("application/json");
        return objectMapper.writeValueAsString(
                favouriteDao.getFavouriteMoviesForUser(Integer.parseInt(request.attribute("userId"))));
    };

    public Route createFavourite = (Request request, Response response) -> {
        response.type("application/json");
        Movie movie = objectMapper.readValue(request.body(), Movie.class);
        Favourite newFavourite = new Favourite();
        newFavourite.setMovie(movie);
        newFavourite.setUserId(Integer.parseInt(request.attribute("userId")));
        favouriteDao.getFavouriteDao().create(newFavourite);
        response.status(201);
        return objectMapper.writeValueAsString(newFavourite);
    };

    public void registerRoutes() {
        get("/protected/favourite", getFavouritesForUser);
        post("/protected/favourite", createFavourite);
    }
}
