package ru.projectatkin.education.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.projectatkin.education.ModelAndData.data.MoviesGenreModel
import ru.projectatkin.education.ModelAndData.data.dto.MovieGenre
import ru.projectatkin.education.ModelAndData.data.features.movies.MovieGenreDataBase

class GenreViewModel: ViewModel() {

    private var moviesGenreModel = MoviesGenreModel(MovieGenreDataBase())

    val dataListGenre: LiveData<List<MovieGenre>> get() = _dataListGenre
    private val _dataListGenre = MutableLiveData<List<MovieGenre>>()

    fun loadGenre() {
        _dataListGenre.postValue(moviesGenreModel.getMoviesGenre())
    }
}