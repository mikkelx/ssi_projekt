package org.example;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.example.dao.GenreDao;
import org.example.dao.JDBCConnection;
import org.example.dao.MovieDao;
import org.example.dao.UserDao;
import org.example.entity.Genre;
import org.example.entity.Movie;
import org.example.entity.User;

import java.sql.SQLException;
import java.util.Date;

public class ApplicationMain {
    public static void main(String[] args) {
        try {
            MovieDao movieDao = MovieDao.getInstance();
            UserDao userDao = UserDao.getInstance();
//            GenreDao genreDao = GenreDao.getInstance();
            Dao<Genre, Integer> genreDao = DaoManager.createDao(JDBCConnection.getInstance().getConnection(), Genre.class);


            Genre genre = new Genre("thriller");
            genreDao.create(genre);
//            Movie newMovie = new Movie("Tytul", new Date(), 9.0, new Genre("thriller"));
//            movieDao.getMovieDao().create(newMovie);

            for (Movie movie : movieDao.getMovieDao().queryForAll()) {
                System.out.println(movie);
                System.out.println();
            }
            User user = new User("test", "test", "X", null);
            userDao.getUserDao().create(user);
            for (User user1 : userDao.getUserDao().queryForAll()) {
                System.out.println(user1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        dao.getAll();
//        get("/hello", (req, res) -> "Hello World");
    }
}