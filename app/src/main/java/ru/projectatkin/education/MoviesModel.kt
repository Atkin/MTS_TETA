package ru.projectatkin.education

import ru.projectatkin.education.data.features.movies.MoviesDataSource

class MoviesModel(
    private val moviesDataSource: MoviesDataSource
) {

    fun getMovies() = moviesDataSource.getMovies()
}