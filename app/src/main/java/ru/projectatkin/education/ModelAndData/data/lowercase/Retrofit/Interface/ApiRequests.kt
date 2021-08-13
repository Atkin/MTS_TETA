package ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.Interface

import retrofit2.Call
import retrofit2.http.GET
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Genres.GenresDB
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Movies.MovieDB

interface ApiRequests {
    @GET("/3/movie/popular?api_key=290f3b220e91dc683ea8eb0d917a1db4&language=ru-Ru&page=1")
    fun getMovieFacts(): Call<MovieDB>

    @GET("/3/genre/movie/list?api_key=290f3b220e91dc683ea8eb0d917a1db4&language=ru-Ru")
    fun getGenresDBList(): Call<GenresDB>
}