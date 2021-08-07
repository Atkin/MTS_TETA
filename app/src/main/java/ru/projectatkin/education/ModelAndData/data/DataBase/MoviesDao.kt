package ru.projectatkin.education.ModelAndData.data.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface  MoviesDao {
    @Query("SELECT * from Movies")
    fun getMoviesList(): LiveData<List<Movies>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movies: Movies)

    @Update
    suspend fun updateMovie(movies: Movies)

    @Query("DELETE FROM Movies")
    suspend fun deleteAllMovies(): Long
}