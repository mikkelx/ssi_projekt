package org.example.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.example.entity.Favourite;
import org.example.exceptions.ResourceNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class FavouriteDao {
    private static final String DOMAIN = "Ulubiony film";
    private static FavouriteDao instance;
    Dao<Favourite, Integer> favouriteDao;

    private FavouriteDao() {
        try {
            favouriteDao = DaoManager.createDao(JDBCConnection.getInstance().getConnection(), Favourite.class);
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zainicjować GenreDao", e);
        }
    }

    public static FavouriteDao getInstance() {
        if (instance == null) {
            synchronized (GenreDao.class) {
                if (instance == null) {
                    instance = new FavouriteDao();
                }
            }
        }
        return instance;
    }

    public Dao<Favourite, Integer> getFavouriteDao() {
        return favouriteDao;
    }


    public List<Favourite> getFavouriteMoviesForUser(Integer userId) throws SQLException, ResourceNotFoundException {
        QueryBuilder<Favourite, Integer> queryBuilder = favouriteDao.queryBuilder();
        Where<Favourite, Integer> where = queryBuilder.where();
        where.eq("userId", userId);
        List<Favourite> favourites = queryBuilder.query();
        if (favourites.isEmpty()) {
            throw new ResourceNotFoundException(DOMAIN, userId);
        }
        return favourites;
    }
}
