package ru.projectatkin.education.View.ViewHolders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.R

class MovieGenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var movieGenreTitle: TextView? = null
    var genreLayout: ConstraintLayout? = null

    init {
        movieGenreTitle = itemView.findViewById(R.id.movie_genre_title)
        genreLayout = itemView.findViewById(R.id.root_genre)
    }

    fun bind(genres: String) {
        movieGenreTitle!!.text = genres
    }
}