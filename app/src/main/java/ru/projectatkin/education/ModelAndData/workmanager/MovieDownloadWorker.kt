package ru.projectatkin.education.ModelAndData.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.Movies
import ru.projectatkin.education.ModelAndData.data.lowercase.MoviesDatabase
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.Interface.ApiRequests
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Genres.GenresDB
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Movies.MovieDB
import ru.projectatkin.education.R
import ru.projectatkin.education.view.fragments.BASE_POSTER_URL
import ru.projectatkin.education.view.fragments.BASE_URL
import ru.projectatkin.education.view.fragments.FragmentHome

class MovieDownloadWorker(val context: Context, val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private lateinit var moviesDB: MovieDB
    private lateinit var genresDB: GenresDB

    @SuppressLint("WrongThread")
    override fun doWork(): Result {
        downloadFilms()
        Log.d("Downloads", "Downloads")
        return Result.success()
    }

    private fun downloadFilms() {
        runBlocking {
            launch {
                val builder = NotificationCompat.Builder(context, FragmentHome.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_star_black)
                    .setContentTitle("Downloads!")
                    .setContentText("Please, wait!")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(context)) {
                    notify(FragmentHome.NOTIFICATION_ID, builder.build()) // посылаем уведомление
                }
                addMoviesBDList()
            }
        }
    }


    private fun addMoviesBDList() {
        GlobalScope.launch(Dispatchers.IO) {
            MoviesDatabase.getInstance(context).moviesDao().deleteAllMovies()
            val api: ApiRequests = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getMovieFacts().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!

                    for (result in data.results) {
                        val api: ApiRequests = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ApiRequests::class.java)

                        GlobalScope.launch(Dispatchers.IO) {
                            val response = api.getAgeRequest(result.id).awaitResponse()
                            if (response.isSuccessful) {
                                var age = response.body()!!
                                var ageUs = ""
                                var ageRu = ""
                                var resultAge = "12+"
                                for (ages in age.results) {
                                    if (ages.iso_3166_1 == "US") {
                                        ageUs = ages.release_dates[0].certification
                                    } else if (ages.iso_3166_1 == "RU") {
                                        ageRu = ages.release_dates[0].certification
                                    }
                                }
                                if (ageRu != "") {
                                    resultAge = ageRu
                                } else {
                                    resultAge = ageUs
                                }

                                MoviesDatabase.getInstance(context).moviesDao()
                                    .insert(
                                        Movies(
                                            result.original_title,
                                            result.overview,
                                            ((result.vote_average) / 2).toInt(),
                                            if (resultAge != "")
                                                resultAge
                                            else "12+",
                                            if (isValidUrl(BASE_POSTER_URL + result.poster_path)) {
                                                BASE_POSTER_URL + result.poster_path
                                            } else {
                                                "https://i.ibb.co/Bf42WH6/900-600.jpg"
                                            },
                                            result.genre_ids[0],
                                            result.release_date,
                                            result.id
                                        )
                                    )
                            }
                        }
                    }

                }
            }
        }

    }

    fun isValidUrl(url: String?): Boolean {
        if (url != null) {
            val p = Patterns.WEB_URL
            val m = p.matcher(url)
            return m.matches()
        } else return false
    }

}