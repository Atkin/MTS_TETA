package ru.projectatkin.education.ModelAndData.data.lowercase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDao {
    @Query("SELECT * from Profile")
    fun getProfilesList(): LiveData<List<Profile>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Query("DELETE FROM Profile")
    suspend fun deleteAllProfiles()
}