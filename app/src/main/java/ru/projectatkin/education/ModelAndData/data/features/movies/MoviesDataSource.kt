package ru.projectatkin.education.ModelAndData.data.features.movies

import ru.projectatkin.education.ModelAndData.data.dto.MovieDto

interface MoviesDataSource {
	fun getMovies(): MutableList<MovieDto>
}