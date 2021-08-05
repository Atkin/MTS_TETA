package ru.projectatkin.education.ModelAndData.data

import MovieActorDataSource

class ActorsModel(
    private val movieActorDataSource: MovieActorDataSource
) {

    fun getActors() = movieActorDataSource.getMovieActor()
}