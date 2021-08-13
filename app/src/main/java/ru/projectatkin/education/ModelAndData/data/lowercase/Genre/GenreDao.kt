package ru.projectatkin.education.ModelAndData.data.lowercase.Genre

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {
    @Query("SELECT * from Genres")
    fun getGenresList(): LiveData<List<Genres>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(genres: Genres)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDefault(genres: Genres)

    @Query("DELETE FROM Genres")
    suspend fun deleteAllGenres()
}