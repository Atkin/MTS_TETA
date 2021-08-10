package ru.projectatkin.education.ModelAndData.data.lowercase.Actors

import androidx.lifecycle.LiveData

class ActorsRepository(private val actorsDao: ActorsDao) {
    val readAllData: LiveData<List<Actors>> = actorsDao.getActorsList()

    suspend fun addActor(actors: Actors) {
        actorsDao.insert(actors)
    }

    suspend fun deleteAllActors(){
        actorsDao.deleteAllActors()
    }
}