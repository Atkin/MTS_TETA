package ru.projectatkin.education.ModelAndData.data.lowercase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel (application: Application): AndroidViewModel(application) {


    val readAllData: LiveData<List<Profile>>
    private val repository: ProfileRepository

    init {
        val profileDao = MoviesDatabase.getDatabase(application).profileDao()
        repository = ProfileRepository(profileDao)
        readAllData = repository.readAllData
    }

    fun addProfile(profile: Profile) {
        //Coroutine function
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProfile(profile)
        }
    }

    fun updateMovie(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(profile)
        }
    }

    fun deleteAllMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllProfiles()
        }
    }
}