package ru.projectatkin.education.ModelAndData.data.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel (application: Application): AndroidViewModel(application) {


    val readAllData: LiveData<List<Movies>>
    private val repository: MoviesRepository

    init {
        val userDao = MoviesDatabase.getDatabase(application).moviesDao()
        repository = MoviesRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(movies: Movies) {
        //Coroutine function
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movies)
        }
    }

    fun updateUser(movies: Movies) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovie(movies)
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMovies()
        }
    }
}