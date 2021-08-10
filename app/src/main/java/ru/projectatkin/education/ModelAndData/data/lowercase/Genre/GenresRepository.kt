package ru.projectatkin.education.ModelAndData.data.lowercase.Genre

import androidx.lifecycle.LiveData

class GenresRepository (private val genresDao: GenreDao) {
    val readAllData: LiveData<List<Genres>> = genresDao.getGenresList()

    suspend fun addGenre(genres: Genres) {
        genresDao.insert(genres)
    }

    suspend fun deleteAllGenres(){
        genresDao.deleteAllGenres()
    }
}