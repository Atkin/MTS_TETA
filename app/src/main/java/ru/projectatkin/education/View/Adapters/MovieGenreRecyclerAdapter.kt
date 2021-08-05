package ru.projectatkin.education.View.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.CellClickListenerGenre
import ru.projectatkin.education.ModelAndData.data.dto.MovieGenre
import ru.projectatkin.education.R
import ru.projectatkin.education.View.ViewHolders.MovieGenreViewHolder

class MovieGenreRecyclerAdapter(private var cellClickListener: CellClickListenerGenre) : RecyclerView.Adapter<MovieGenreViewHolder>() {
    private var genres: MutableList<MovieGenre> = ArrayList()



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

    fun updateList(newGenres: List<MovieGenre>) {
        if (genres!=null){
            genres.clear()
            genres.addAll(newGenres)
            notifyDataSetChanged()
            Log.d("initDataBlock", "size  = $itemCount")
        }
    }
}