package org.example.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.example.entity.User;
import org.example.exceptions.ResourceNotFoundException;

import java.sql.SQLException;
import java.util.List;



public class UserDao {
    private static UserDao instance;
    Dao<User, Integer> userDao;

    private UserDao() {
        try {
            userDao = DaoManager.createDao(JDBCConnection.getInstance().getConnection(), User.class);
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zainicjować UserDao", e);
        }
    }

    public static UserDao getInstance() {
        if (instance == null) {
            synchronized (UserDao.class) {
                if (instance == null) {
                    instance = new UserDao();
                }
            }
        }
        return instance;
    }

    public Dao<User, Integer> getUserDao() {
        return userDao;
    }

    public User getUserByUsername(String username) throws SQLException, ResourceNotFoundException {
        QueryBuilder<User, Integer> genreQueryBuilder = userDao.queryBuilder();
        Where<User, Integer> where = genreQueryBuilder.where();
        where.eq("username", username);
        List<User> users = genreQueryBuilder.query();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("User", username);
        }
        return users.get(0);
    }
}

