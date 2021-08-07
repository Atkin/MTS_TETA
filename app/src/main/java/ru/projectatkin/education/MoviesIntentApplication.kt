package ru.projectatkin.education

import android.app.Application
import ru.projectatkin.education.ModelAndData.data.DataBase.MoviesRepository

class MoviesIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MoviesRepository.initialize(this)
    }
}