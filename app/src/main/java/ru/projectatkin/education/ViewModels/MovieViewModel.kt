package ru.projectatkin.education.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.projectatkin.education.ModelAndData.data.MoviesModel
import ru.projectatkin.education.ModelAndData.data.dto.MovieDto
import ru.projectatkin.education.ModelAndData.data.features.movies.MovieSecondDataBase
import ru.projectatkin.education.ModelAndData.data.features.movies.MoviesDataSourceImpl

class MovieViewModel: ViewModel() {

    private var movieViewModel = MoviesModel(MoviesDataSourceImpl())

    val dataListMovie: LiveData<List<MovieDto>> get() = _dataListMovie
    private val _dataListMovie = MutableLiveData<List<MovieDto>>()

    fun loadMovies() {
        _dataListMovie.postValue(movieViewModel.getMovies())
    }

    fun tempUpdate() {
        _dataListMovie.postValue(MoviesModel(MovieSecondDataBase()).getMovies())
    }
}