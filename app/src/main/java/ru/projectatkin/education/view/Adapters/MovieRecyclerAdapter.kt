package ru.projectatkin.education.view.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.CellClickListener
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.Movies
import ru.projectatkin.education.R
import ru.projectatkin.education.view.ViewHolders.MovieViewHolder

class MovieRecyclerAdapter(
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MovieViewHolder>() {
    private var movies = emptyList<Movies>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.movieLayout?.setOnClickListener {
            cellClickListener.onCellClickListener(
                position,
                movies[position].moviesId,
                movies[position].genreId
            )
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateMoviesList(newMovies: List<Movies>) {
        this.movies = newMovies
        notifyDataSetChanged()
    }
}