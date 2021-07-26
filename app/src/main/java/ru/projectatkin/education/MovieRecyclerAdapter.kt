package ru.projectatkin.education

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import ru.projectatkin.education.data.dto.MovieDto
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MovieRecyclerAdapter(
    private var movies: MutableList<MovieDto>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MovieViewHolder>() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.movieLayout?.setOnClickListener {
            cellClickListener.onCellClickListener(position.toString())
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }


    fun updateList(movies: MutableList<MovieDto>) {
        val diffCallback = MoviesCallback(this.movies, movies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        diffResult.dispatchUpdatesTo(this)
        this.movies = movies
    }

    suspend fun downloadFilm(): String {
        return suspendCoroutine { continuation ->
            fun onSuccess(result: String) {
                Thread.sleep(5000)
                continuation.resume(result)
            }

            fun onFailure(error: Exception) {
                continuation.resumeWithException(error)
            }
        }
    }
}

interface download {
    fun onSuccess(string: String)

    fun inError(string: String)
}