package org.example;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.example.dao.JDBCConnection;
import org.example.entity.Movie;
import org.example.entity.User;

import java.sql.SQLException;

public class ApplicationMain {
    public static void main(String[] args) {
        try {
            Dao<Movie, Long> movieDao = DaoManager.createDao(JDBCConnection.getInstance().getConnection(), Movie.class);
            Dao<User, Long> userDao = DaoManager.createDao(JDBCConnection.getInstance().getConnection(), User.class);
            for (Movie movie : movieDao.queryForAll()) {
                System.out.println(movie);
                System.out.println();
            }
            User user = new User(1L, "test", "test", "X", null);
            userDao.create(user);
            for (User user1 : userDao.queryForAll()) {
                System.out.println(user1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        dao.getAll();
//        get("/hello", (req, res) -> "Hello World");
    }
}