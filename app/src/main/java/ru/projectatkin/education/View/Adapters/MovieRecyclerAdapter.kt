package ru.projectatkin.education.View.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.CellClickListener
import ru.projectatkin.education.ModelAndData.data.DataBase.Movies
import ru.projectatkin.education.ModelAndData.data.dto.MovieDto
import ru.projectatkin.education.R
import ru.projectatkin.education.View.ViewHolders.MovieViewHolder

class MovieRecyclerAdapter(

    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MovieViewHolder>() {
    //private var movies: MutableList<MovieDto> = ArrayList()
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
            cellClickListener.onCellClickListener(position.toString())
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

   // fun updateList(newMovies: List<MovieDto>) {
   //     if (movies!=null){
    //        movies.clear()
    //        movies.addAll(newMovies)
    //        notifyDataSetChanged()
    //        Log.d("initDataBlock", "size  = $itemCount")
    //    }
   // }

    fun updateMoviesList(newMovies: List<Movies>) {
        this.movies = newMovies
        notifyDataSetChanged()
    }
}