package ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.Interface

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.projectatkin.education.ModelAndData.data.lowercase.Age.Age
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Actors.ActorsDB
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Genres.GenresDB
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Movies.MovieDB

interface ApiRequests {
    @GET("/3/movie/popular?api_key=290f3b220e91dc683ea8eb0d917a1db4&language=ru-Ru&page=1")
    fun getMovieFacts(): Call<MovieDB>

    @GET("/3/genre/movie/list?api_key=290f3b220e91dc683ea8eb0d917a1db4&language=ru-Ru")
    fun getGenresDBList(): Call<GenresDB>

    @GET("/3/movie/{moviesId}/credits?api_key=290f3b220e91dc683ea8eb0d917a1db4&language=ru-Ru")
    fun getActorsDBList(
        @Path("moviesId")
        moviesId: Int): Call<ActorsDB>

    @GET("/3/movie/{moviesId}/release_dates?api_key=290f3b220e91dc683ea8eb0d917a1db4&language=ru-Ru")
    fun getAgeRequest(
        @Path("moviesId")
        moviesId: Int): Call<Age>
}