package ru.projectatkin.education.ModelAndData.data.lowercase.Genre

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Genres")
data class Genres(
    @ColumnInfo(name = "movie_genre")
    var genreTitle: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idInGenres")
    var idInGenres: Int
)