package org.example.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

import java.util.Date;

@DatabaseTable(tableName = "Movie")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Movie {
    @DatabaseField(generatedId = true, columnName = "movieId")
    private Long id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "releaseDate", dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
    private Date releaseDate;
    @DatabaseField(columnName = "rating")
    private double rating;
    @DatabaseField(columnName = "genreId", foreign = true)
    private Genre genre;
}
