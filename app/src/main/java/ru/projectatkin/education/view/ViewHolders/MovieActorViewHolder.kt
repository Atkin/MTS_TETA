package ru.projectatkin.education.view.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.Actors
import ru.projectatkin.education.R

class MovieActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var actorPhoto: ImageView? =  null
    var actorName: TextView? = null

    init {
        actorPhoto = itemView.findViewById(R.id.actor_photo)
        actorName = itemView.findViewById(R.id.actor_name)
    }

    fun bind(actor: Actors) {
        actorPhoto?.load(actor.actorURL ?: "https://i.ibb.co/Bf42WH6/900-600.jpg")
        actorName?.text   = actor.actorName?: "Фильму быть!"
    }
}