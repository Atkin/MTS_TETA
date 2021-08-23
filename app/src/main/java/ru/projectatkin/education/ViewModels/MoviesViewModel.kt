package ru.projectatkin.education.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.Movies
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.MoviesRepository
import ru.projectatkin.education.ModelAndData.data.lowercase.MoviesDatabase

class MoviesViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Movies>>
    private val repository: MoviesRepository

    init {
        val moviesDao = MoviesDatabase.getInstance(application).moviesDao()
        repository = MoviesRepository(moviesDao)
        readAllData = repository.readAllData
    }

    fun addMovie(movies: Movies) {
        //Coroutine function
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movies)
        }
    }

    fun updateMovie(movies: Movies) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovie(movies)
        }
    }

    fun deleteAllMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMovies()
        }
    }
}