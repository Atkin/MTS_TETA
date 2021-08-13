package ru.projectatkin.education.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.Actors
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.ActorsRepository
import ru.projectatkin.education.ModelAndData.data.lowercase.MoviesDatabase

class ActorsViewModel (application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Actors>>
    private val repository: ActorsRepository

    init {
        val actorsDao = MoviesDatabase.getInstance(application).actorsDao()
        repository = ActorsRepository(actorsDao)
        readAllData = repository.readAllData
    }

    fun addActor(actors: Actors) {
        //Coroutine function
        viewModelScope.launch(Dispatchers.IO) {
            repository.addActor(actors)
        }
    }

    fun deleteAllActors() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllActors()
        }
    }
}