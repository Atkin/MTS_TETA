package ru.projectatkin.education.data.features.movies

import ru.projectatkin.education.data.dto.MovieDto

interface MoviesDataSource {
	fun getMovies(): MutableList<MovieDto>
}