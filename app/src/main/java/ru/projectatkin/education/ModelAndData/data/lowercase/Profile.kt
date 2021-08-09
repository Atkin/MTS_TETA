package ru.projectatkin.education.ModelAndData.data.lowercase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profile")
data class Profile(
    @ColumnInfo(name = "movie_age_restriction")
    var name: String?,
    @ColumnInfo(name = "movie_image_url")
    var password: String?,
    @ColumnInfo(name = "movie_genre")
    var eMail: String?,
    @ColumnInfo(name = "movie_date")
    var telephone: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int
)