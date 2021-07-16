import ru.projectatkin.education.data.dto.MovieGenre

interface MovieGenreDataSource {
  fun getMovieGenre(): MutableList<MovieGenre>
}