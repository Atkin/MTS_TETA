package ru.projectatkin.education.ModelAndData.data

import ru.projectatkin.education.MovieGenreDataSource

class MoviesGenreModel(private val moviesGenreDataSourse: MovieGenreDataSource) {
    fun getMoviesGenre() = moviesGenreDataSourse.getMovieGenre()
}



