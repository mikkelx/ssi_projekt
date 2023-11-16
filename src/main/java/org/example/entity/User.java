package org.example.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "User")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @DatabaseField(id = true, columnName = "userId")
    private Integer id;
    @DatabaseField(columnName = "username")
    private String username;
    @DatabaseField(columnName = "password")
    private String password;
    @DatabaseField(columnName = "role")
    private String role;
    private List<Movie> favouriteMovies;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.favouriteMovies = new ArrayList<>();
    }
}
