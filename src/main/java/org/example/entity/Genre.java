package org.example.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

import java.util.List;

@DatabaseTable(tableName = "Genre")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Genre {
    @DatabaseField(id = true, columnName = "genreId")
    private Integer id;
    @DatabaseField(columnName = "name")
    private String genreName;
}
