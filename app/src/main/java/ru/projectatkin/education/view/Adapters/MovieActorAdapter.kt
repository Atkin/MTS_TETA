package ru.projectatkin.education.view.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.Actors
import ru.projectatkin.education.R
import ru.projectatkin.education.view.ViewHolders.MovieActorViewHolder

class MovieActorAdapter() : RecyclerView.Adapter<MovieActorViewHolder>() {
    private var actors: List<Actors> = emptyList()


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

    fun updateActorsList(newActors: List<Actors>) {
        this.actors = newActors
        notifyDataSetChanged()
    }
}