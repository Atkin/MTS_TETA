package ru.projectatkin.education.ModelAndData.data.lowercase.Movies

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.Actors


@Dao
interface MoviesDao {
    @Query("SELECT * FROM Movies")
    fun getMoviesList(): LiveData<List<Movies>>

    @Query("SELECT Movies.movie_title, Movies.movie_description, Movies.movie_rate_score, Movies.movie_age_restriction, Movies.movie_image_url, Movies.movie_date, Genres.movie_genre, Movies.movie_actor_id FROM Movies, Genres WHERE Movies.movie_genre_id = Genres.idInGenres")
    fun getMovieWIthGenre(): LiveData<List<MoviesAndGenres>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movies: Movies)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDefault(movies: Movies)

    @Update
    suspend fun updateMovie(movies: Movies)

    @Query("DELETE FROM Movies")
    suspend fun deleteAllMovies()
}

@Entity(tableName = "MoviesAndGenres")
data class MoviesAndGenres(
    @ColumnInfo(name = "movie_title")
    var title: String,
    @ColumnInfo(name = "movie_description")
    var description: String,
    @ColumnInfo(name = "movie_rate_score")
    var rateScore: Int?,
    @ColumnInfo(name = "movie_age_restriction")
    var ageRestriction: String?,
    @ColumnInfo(name = "movie_image_url")
    var imageUrl: String?,
    @ColumnInfo(name = "movie_date")
    var date: String?,
    @ColumnInfo(name = "movie_genre")
    var genreTitle: String,
    @ColumnInfo(name = "movie_actor_id")
    var actorId: Int,
    @Relation(parentColumn = "movie_actor_id", entityColumn = "actor_id")
    var actorsList: List<Actors>
)
