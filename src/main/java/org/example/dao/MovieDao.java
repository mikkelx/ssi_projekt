package org.example.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.example.entity.Movie;

import java.sql.SQLException;
import java.util.List;

public class MovieDao {
    private static MovieDao instance;
    Dao<Movie, Integer> movieDao;

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

}

