package ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Actors

import androidx.room.Entity

data class ActorsDB(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)