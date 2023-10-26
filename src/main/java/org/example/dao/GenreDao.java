package org.example.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.example.entity.Genre;
import org.example.exceptions.ResourceNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class GenreDao {
    private static final String DOMAIN = "Gatunek";
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

    //TODO - method to save only uniqie values of Genre
    // We would like to avoid scenario with duplicated Genres
//    public Integer create(Genre genre) throws SQLException {
//        genreDao.createIfNotExists(genre)''
//
//    }

    public Genre getGenreByName(String genreName) throws SQLException, ResourceNotFoundException {
        QueryBuilder<Genre, Integer> genreQueryBuilder = genreDao.queryBuilder();
        Where<Genre, Integer> where = genreQueryBuilder.where();
        where.eq("name", genreName);
        List<Genre> genres = genreQueryBuilder.query();
        if (genres.isEmpty()) {
            throw new ResourceNotFoundException(DOMAIN, genreName);
        }
        return genres.get(0);
    }

}

