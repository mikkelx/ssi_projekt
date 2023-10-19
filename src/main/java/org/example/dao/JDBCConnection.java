package org.example.dao;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class JDBCConnection {
    private static final JDBCConnection connectionInstance = new JDBCConnection();
    private ConnectionSource connection;

    private JDBCConnection(){
        try {
            connection = new JdbcPooledConnectionSource("jdbc:sqlite:db_ssi_project.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JDBCConnection getInstance() {
        return connectionInstance;
    }

    public ConnectionSource getConnection() {
        return connection;
    }
}
