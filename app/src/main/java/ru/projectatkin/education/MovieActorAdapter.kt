package ru.projectatkin.education

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.data.dto.MovieActor

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