package ru.projectatkin.education.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.projectatkin.education.ModelAndData.data.lowercase.Genre.Genres
import ru.projectatkin.education.ModelAndData.data.lowercase.Genre.GenresRepository
import ru.projectatkin.education.ModelAndData.data.lowercase.MoviesDatabase

class GenresViewModel (application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Genres>>
    private val repository: GenresRepository

    init {
        val genresDao = MoviesDatabase.getInstance(application).genresDao()
        repository = GenresRepository(genresDao)
        readAllData = repository.readAllData
    }

    fun addProfile(genres: Genres) {
        //Coroutine function
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGenre(genres)
        }
    }

    fun deleteAllGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllGenres()
        }
    }
}