package ru.projectatkin.education

import MovieActorDataSource

class ActorsModel(
    private val movieActorDataSource: MovieActorDataSource
) {

    fun getActors() = movieActorDataSource.getMovieActor()
}