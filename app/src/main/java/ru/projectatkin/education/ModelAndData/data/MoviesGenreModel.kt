package ru.projectatkin.education.ModelAndData.data

import MovieGenreDataSource

class MoviesGenreModel(private val moviesGenreDataSourse: MovieGenreDataSource) {
    fun getMoviesGenre() = moviesGenreDataSourse.getMovieGenre()
}



