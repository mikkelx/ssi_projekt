package org.example.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.example.entity.Genre;

import java.sql.SQLException;

public class GenreDao {
    private static GenreDao instance;
    Dao<Genre, Integer> genreDao;

    private GenreDao() {
        try {
            genreDao = DaoManager.createDao(JDBCConnection.getInstance().getConnection(), Genre.class);
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zainicjować GenreDao", e);
        }
    }

    public static GenreDao getInstance() {
        if (instance == null) {
            synchronized (GenreDao.class) {
                if (instance == null) {
                    instance = new GenreDao();
                }
            }
        }
        return instance;
    }

    public Dao<Genre, Integer> getGenreDao() {
        return genreDao;
    }

}

