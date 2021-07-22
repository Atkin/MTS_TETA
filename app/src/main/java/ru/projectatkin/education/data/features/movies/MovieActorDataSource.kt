import ru.projectatkin.education.data.dto.MovieActor

interface MovieActorDataSource {
    fun getMovieActor(): MutableList<MovieActor>
}