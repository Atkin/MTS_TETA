package ru.projectatkin.education.ModelAndData.data.lowercase.Movies

import androidx.lifecycle.LiveData

class MoviesRepository (private val moviesDao: MoviesDao) {
    val readAllData: LiveData<List<Movies>> = moviesDao.getMoviesList()

    suspend fun addMovie(movies: Movies) {
        moviesDao.insert(movies)
    }

    suspend fun updateMovie(movies: Movies) {
        moviesDao.updateMovie(movies)
    }

    suspend fun deleteAllMovies(){
        moviesDao.deleteAllMovies()
    }
}
