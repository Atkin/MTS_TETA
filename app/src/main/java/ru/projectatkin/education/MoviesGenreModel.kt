package ru.projectatkin.education

import MovieGenreDataSource

class MoviesGenreModel(private val moviesGenreDataSourse: MovieGenreDataSource) {
    fun getMoviesGenre() = moviesGenreDataSourse.getMovieGenre()
}



