package ru.projectatkin.education.ModelAndData.data.lowercase.Actors

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ActorsDao {
    @Query("SELECT * from Actors")
    fun getActorsList(): LiveData<List<Actors>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actors: Actors)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDefault(actors: Actors)

    @Query("DELETE FROM Actors")
    suspend fun deleteAllActors()
}