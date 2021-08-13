package ru.projectatkin.education.ModelAndData.data.lowercase.Profile

import androidx.lifecycle.LiveData

class ProfileRepository (private val profileDao: ProfileDao) {
    val readAllData: LiveData<List<Profile>> = profileDao.getProfilesList()

    suspend fun addProfile(profile: Profile) {
        profileDao.insert(profile)
    }

    suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(profile)
    }

    suspend fun deleteAllProfiles(){
        profileDao.deleteAllProfiles()
    }
}