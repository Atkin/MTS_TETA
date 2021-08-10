package ru.projectatkin.education.ModelAndData.data.lowercase.Actors

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Actors")
data class Actors (
    @ColumnInfo(name = "actor_name")
    var actorName: String,
    @ColumnInfo(name = "actor_url_photo")
    var actorURL: String,
    @ColumnInfo(name = "actor_id")
    var actorId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int
)