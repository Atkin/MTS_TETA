package ru.projectatkin.education

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.projectatkin.education.data.dto.MovieActor

class MovieActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var actorPhoto: ImageView? =  null
    var actorName: TextView? = null

    init {
        actorPhoto = itemView.findViewById(R.id.actor_photo)
        actorName = itemView.findViewById(R.id.actor_name)
    }

    fun bind(actor: MovieActor) {
        actorPhoto?.load(actor.imageUrl ?: "https://i.ibb.co/Bf42WH6/900-600.jpg")
        actorName?.text   = actor.name?: "Фильму быть!"
    }
}