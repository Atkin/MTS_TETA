import ru.projectatkin.education.ModelAndData.data.dto.MovieActor

interface MovieActorDataSource {
    fun getMovieActor(): MutableList<MovieActor>
}