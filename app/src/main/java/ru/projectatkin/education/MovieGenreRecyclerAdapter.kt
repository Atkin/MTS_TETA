package ru.projectatkin.education

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.data.dto.MovieGenre

class MovieGenreRecyclerAdapter(
    private val genres: MutableList<MovieGenre>,
    private val cellClickListener: CellClickListenerGenre
) : RecyclerView.Adapter<MovieGenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGenreViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_genre, parent, false)
        return MovieGenreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieGenreViewHolder, position: Int) {
        holder.bind(genres[position].name)
        holder.genreLayout?.setOnClickListener {
            cellClickListener.onCellClickListenerGenre(genres[position].name)
        }
    }

    override fun getItemCount(): Int {
        return genres.size
    }
}