package org.example.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@DatabaseTable(tableName = "Genre")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genre {
    @DatabaseField(generatedId = true, columnName = "genreId")
    private Long id;
    @DatabaseField(columnName = "genre")
    private String genre;
}
