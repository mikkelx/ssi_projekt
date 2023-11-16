package org.example.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DatabaseTable(tableName = "Favourite")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Favourite {
    @DatabaseField(generatedId = true, columnName = "favouriteId")
    private int id;
    @DatabaseField(columnName = "movieId", foreign = true, foreignAutoRefresh = true)
    private Movie movie;
    @DatabaseField(columnName = "userId")
    private Integer userId;
}
