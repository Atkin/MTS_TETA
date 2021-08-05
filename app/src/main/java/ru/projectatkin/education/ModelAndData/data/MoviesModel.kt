package ru.projectatkin.education.ModelAndData.data

import ru.projectatkin.education.ModelAndData.data.features.movies.MoviesDataSource

class MoviesModel(
    private val moviesDataSource: MoviesDataSource
) {

    fun getMovies() = moviesDataSource.getMovies()
}