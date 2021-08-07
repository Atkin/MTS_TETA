package ru.projectatkin.education.ModelAndData.data

import ru.projectatkin.education.MovieActorDataSource

class ActorsModel(
    private val movieActorDataSource: MovieActorDataSource
) {

    fun getActors() = movieActorDataSource.getMovieActor()
}