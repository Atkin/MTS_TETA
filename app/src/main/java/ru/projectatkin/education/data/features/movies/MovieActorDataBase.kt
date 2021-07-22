package ru.projectatkin.education.data.features.movies

import MovieActorDataSource
import ru.projectatkin.education.data.dto.MovieActor

class MovieActorDataBase : MovieActorDataSource {
    override fun getMovieActor() = mutableListOf(
        MovieActor(
            imageUrl = "https://i.ibb.co/K92rzch/1.jpg",
            name = "Евгений Моргунов"
        ),
        MovieActor(
            imageUrl = "https://i.ibb.co/8D2gJn6/2.jpg",
            name = "Юрий Никулин"
        ),
        MovieActor(
            imageUrl = "https://i.ibb.co/nwN5xw5/3.jpg",
            name = "Георгий Вицин"
        )
    )
}