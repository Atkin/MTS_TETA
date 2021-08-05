package ru.projectatkin.education.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.ModelAndData.data.dto.MovieActor
import ru.projectatkin.education.R
import ru.projectatkin.education.View.ViewHolders.MovieActorViewHolder

class MovieActorAdapter(
    private var actors: MutableList<MovieActor>
) : RecyclerView.Adapter<MovieActorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieActorViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.actor_item, parent, false)
        return MovieActorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemCount(): Int {
        return actors.size
    }
}