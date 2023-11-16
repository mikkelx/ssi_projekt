package org.example.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import org.example.entity.Genre;
import org.example.entity.Movie;
import org.example.exceptions.ResourceNotFoundException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MovieDao {
    private static final String DOMAIN = "Film";
    private static MovieDao instance;
    Dao<Movie, Integer> movieDao;
    Dao<Genre, Integer> genreDao = GenreDao.getInstance().genreDao;

    private MovieDao() {
        try {
            movieDao = DaoManager.createDao(JDBCConnection.getInstance().getConnection(), Movie.class);
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zainicjować MovieDao", e);
        }
    }

    public static MovieDao getInstance() {
        if (instance == null) {
            synchronized (MovieDao.class) {
                if (instance == null) {
                    instance = new MovieDao();
                }
            }
        }
        return instance;
    }

    public Dao<Movie, Integer> getMovieDao() {
        return movieDao;
    }

    public List<Movie> getMoviesByGenreName(String genreName) throws SQLException, ResourceNotFoundException {
        QueryBuilder<Movie, Integer> movieQueryBuilder = movieDao.queryBuilder();
        QueryBuilder<Genre, Integer> genreQueryBuilder = genreDao.queryBuilder();

        try {
            movieQueryBuilder.join(genreQueryBuilder);

            genreQueryBuilder.where().eq("name", genreName);
            movieQueryBuilder.where().eq("genreId", genreQueryBuilder.query().get(0).getId());

            List<Movie> movies = movieQueryBuilder.query();

            if (movies.isEmpty()) {
                throw new ResourceNotFoundException(DOMAIN, genreName);
            }

            return movies;
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving movies by genre name: " + e.getMessage());
        }
    }


    public List<Movie> getMoviesByReleaseDate(Date releaseDate) throws SQLException, ResourceNotFoundException {
        QueryBuilder<Movie, Integer> movieQueryBuilder = movieDao.queryBuilder();

        try {
            movieQueryBuilder.where().eq("releaseDate", releaseDate);
            List<Movie> movies = movieQueryBuilder.query();
            if (movies.isEmpty()) {
                throw new ResourceNotFoundException(DOMAIN, "No movies found for the specified release date.");
            }
            return movies;
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving movies by release date: " + e.getMessage());
        }
    }

    public List<Movie> getMoviesByRating(double minRating) throws SQLException, ResourceNotFoundException {
        QueryBuilder<Movie, Integer> movieQueryBuilder = movieDao.queryBuilder();
        try {
            movieQueryBuilder.where().ge("rating", minRating);
            List<Movie> movies = movieQueryBuilder.query();
            if (movies.isEmpty()) {
                throw new ResourceNotFoundException(DOMAIN, "No movies found for the specified minimum rating.");
            }
            return movies;
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving movies by rating: " + e.getMessage());
        }
    }


}

