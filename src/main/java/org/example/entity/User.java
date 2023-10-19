package org.example.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import org.example.util.Role;

import java.util.List;

@DatabaseTable(tableName = "User")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @DatabaseField(id = true, columnName = "userId")
    private Long id;
    @DatabaseField(columnName = "username")
    private String username;
    @DatabaseField(columnName = "password")
    private String password;
    @DatabaseField(columnName = "role")
    private String role;
    private List<Movie> favouriteMovies;
}
