package ru.projectatkin.education.view.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.CellClickListenerGenre
import ru.projectatkin.education.ModelAndData.data.lowercase.Genre.Genres
import ru.projectatkin.education.R
import ru.projectatkin.education.view.ViewHolders.MovieGenreViewHolder

class MovieGenreRecyclerAdapter(private var cellClickListener: CellClickListenerGenre) : RecyclerView.Adapter<MovieGenreViewHolder>() {
    private var genres: List<Genres> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGenreViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_genre, parent, false)
        return MovieGenreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieGenreViewHolder, position: Int) {
        holder.bind(genres[position].genreTitle)
        holder.genreLayout?.setOnClickListener {
            cellClickListener.onCellClickListenerGenre(genres[position].genreTitle)
        }
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    fun updateList(newGenres: List<Genres>) {
        this.genres = newGenres
        notifyDataSetChanged()
    }
}