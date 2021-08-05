package ru.projectatkin.education.ModelAndData.data.features.movies

import MovieGenreDataSource
import ru.projectatkin.education.ModelAndData.data.dto.MovieGenre

class MovieGenreDataBase : MovieGenreDataSource {
    override fun getMovieGenre() = mutableListOf(
        MovieGenre(
            name = "Вестерн",
            id = 0
        ),
        MovieGenre(
            name = "Боевик",
            id = 1
        ),
        MovieGenre(
            name = "Мультик",
            id = 2
        ),
        MovieGenre(
            name = "Комедия",
            id = 3
        ), MovieGenre(
            name = "Криминал",
            id = 4
        ), MovieGenre(
            name = "Драма",
            id = 5
        ), MovieGenre(
            name = "Семейный",
            id = 6
        ), MovieGenre(
            name = "Фентези",
            id = 7
        ), MovieGenre(
            name = "Ужасы",
            id = 8
        ), MovieGenre(
            name = "Музыка",
            id = 9
        ), MovieGenre(
            name = "Мистика",
            id = 10
        ), MovieGenre(
            name = "Триллер",
            id = 11
        ), MovieGenre(
            name = "Военный",
            id = 12
        )
    )
}