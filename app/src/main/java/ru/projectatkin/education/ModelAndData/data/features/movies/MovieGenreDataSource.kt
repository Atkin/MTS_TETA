import ru.projectatkin.education.ModelAndData.data.dto.MovieGenre

interface MovieGenreDataSource {
  fun getMovieGenre(): MutableList<MovieGenre>
}