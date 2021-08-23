package ru.projectatkin.education.ModelAndData.data.lowercase.Movies

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MoviesDao {
    @Query("SELECT * FROM Movies")
    fun getMoviesList(): LiveData<List<Movies>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movies: Movies)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDefault(movies: Movies)

    @Update
    suspend fun updateMovie(movies: Movies)

    @Query("DELETE FROM Movies")
    suspend fun deleteAllMovies()
}