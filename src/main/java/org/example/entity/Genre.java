package org.example.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@DatabaseTable(tableName = "Genre")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Genre {
    @DatabaseField(generatedId = true, columnName = "genreId")
    private int id;
    @DatabaseField(columnName = "name", unique = true)
    private String genreName;

    public Genre(String genreName) {
        this.genreName = genreName;
    }
}
