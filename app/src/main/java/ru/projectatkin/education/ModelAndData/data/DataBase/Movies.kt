package ru.projectatkin.education.ModelAndData.data.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class Movies(
    @ColumnInfo(name = "movie_title")
    var title: String,
    @ColumnInfo(name = "movie_description")
    var description: String,
    @ColumnInfo(name = "movie_rate_score")
    var rateScore: Int?,
    @ColumnInfo(name = "movie_age_restriction")
    var ageRestriction: String?,
    @ColumnInfo(name = "movie_image_url")
    var imageUrl: String?,
    @ColumnInfo(name = "movie_genre")
    var genre: String?,
    @ColumnInfo(name = "movie_date")
    var date: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int
)