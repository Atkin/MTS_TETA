package ru.projectatkin.education.View.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies
import ru.projectatkin.education.R

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var movieImage: ImageView? = null
    var movieTitle: TextView? = null
    var movieDescription: TextView? = null
    var movieBarrierDescription: Barrier? = null
    var movieAgeRestriction: TextView? = null
    var movieLayout: ConstraintLayout? = null
    var raitingStar1: ImageView? = null
    var raitingStar2: ImageView? = null
    var raitingStar3: ImageView? = null
    var raitingStar4: ImageView? = null
    var raitingStar5: ImageView? = null

    init {
        movieImage = itemView.findViewById(R.id.movieImage)
        movieTitle = itemView.findViewById(R.id.movieTitle)
        movieDescription = itemView.findViewById(R.id.movieDescription)
        movieBarrierDescription = itemView.findViewById(R.id.movieBarrierDescription)
        movieAgeRestriction = itemView.findViewById(R.id.movieAgeRestriction)
        movieLayout = itemView.findViewById(R.id.root_movie)
        raitingStar1 = itemView.findViewById(R.id.ic_raiting_star_1)
        raitingStar2 = itemView.findViewById(R.id.ic_raiting_star_2)
        raitingStar3 = itemView.findViewById(R.id.ic_raiting_star_3)
        raitingStar4 = itemView.findViewById(R.id.ic_raiting_star_4)
        raitingStar5 = itemView.findViewById(R.id.ic_raiting_star_5)
    }

    fun bind(movies: Movies) {
        movieImage?.load(movies.imageUrl ?: "https://i.ibb.co/Bf42WH6/900-600.jpg")
        movieTitle?.text = movies.title ?: "Фильму быть!"
        movieDescription?.text = movies.description
            ?: "Здесь должно быть описание фильма. Возможно, когда-нибудь оно будет. Оставайтесь с нами"
        movieAgeRestriction?.text = movies.ageRestriction ?: "99+"
        when (movies.rateScore) {
            0 -> {
                raitingStar1!!.setImageResource(R.drawable.ic_star_white)
                raitingStar2!!.setImageResource(R.drawable.ic_star_white)
                raitingStar3!!.setImageResource(R.drawable.ic_star_white)
                raitingStar4!!.setImageResource(R.drawable.ic_star_white)
                raitingStar5!!.setImageResource(R.drawable.ic_star_white)
            }
            1 -> {
                raitingStar1!!.setImageResource(R.drawable.ic_star_black)
                raitingStar2!!.setImageResource(R.drawable.ic_star_white)
                raitingStar3!!.setImageResource(R.drawable.ic_star_white)
                raitingStar4!!.setImageResource(R.drawable.ic_star_white)
                raitingStar5!!.setImageResource(R.drawable.ic_star_white)
            }
            2 -> {
                raitingStar1!!.setImageResource(R.drawable.ic_star_black)
                raitingStar2!!.setImageResource(R.drawable.ic_star_black)
                raitingStar3!!.setImageResource(R.drawable.ic_star_white)
                raitingStar4!!.setImageResource(R.drawable.ic_star_white)
                raitingStar5!!.setImageResource(R.drawable.ic_star_white)
            }
            3 -> {
                raitingStar1!!.setImageResource(R.drawable.ic_star_black)
                raitingStar2!!.setImageResource(R.drawable.ic_star_black)
                raitingStar3!!.setImageResource(R.drawable.ic_star_black)
                raitingStar4!!.setImageResource(R.drawable.ic_star_white)
                raitingStar5!!.setImageResource(R.drawable.ic_star_white)
            }
            4 -> {
                raitingStar1!!.setImageResource(R.drawable.ic_star_black)
                raitingStar2!!.setImageResource(R.drawable.ic_star_black)
                raitingStar3!!.setImageResource(R.drawable.ic_star_black)
                raitingStar4!!.setImageResource(R.drawable.ic_star_black)
                raitingStar5!!.setImageResource(R.drawable.ic_star_white)
            }
            5 -> {
                raitingStar1!!.setImageResource(R.drawable.ic_star_black)
                raitingStar2!!.setImageResource(R.drawable.ic_star_black)
                raitingStar3!!.setImageResource(R.drawable.ic_star_black)
                raitingStar4!!.setImageResource(R.drawable.ic_star_black)
                raitingStar5!!.setImageResource(R.drawable.ic_star_black)
            }
            else -> {
                raitingStar1!!.setImageResource(R.drawable.ic_star_white)
                raitingStar2!!.setImageResource(R.drawable.ic_star_white)
                raitingStar3!!.setImageResource(R.drawable.ic_star_white)
                raitingStar4!!.setImageResource(R.drawable.ic_star_white)
                raitingStar5!!.setImageResource(R.drawable.ic_star_white)
            }
        }
    }
}


