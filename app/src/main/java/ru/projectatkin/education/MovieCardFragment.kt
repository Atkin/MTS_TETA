package ru.projectatkin.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.projectatkin.education.data.dto.MovieDto
import ru.projectatkin.education.data.features.movies.MovieActorDataBase
import ru.projectatkin.education.data.features.movies.MovieSecondDataBase
import ru.projectatkin.education.data.features.movies.MoviesDataSourceImpl

class MovieCardFragment() : Fragment() {
    private lateinit var movies: MutableList<MovieDto>
    private lateinit var movie: MovieDto
    private lateinit var moviesModel: MoviesModel
    private lateinit var movieImage: ImageView
    private lateinit var movieGenre: TextView
    private lateinit var movieDate: TextView
    private lateinit var movieAge: TextView
    private lateinit var movieDescription: TextView
    private lateinit var movieTitle: TextView
    private lateinit var raitingStar1: ImageView
    private lateinit var raitingStar2: ImageView
    private lateinit var raitingStar3: ImageView
    private lateinit var raitingStar4: ImageView
    private lateinit var raitingStar5: ImageView
    private lateinit var actorsModel: ActorsModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_movie_details, container, false)
        this.movieImage = view.findViewById(R.id.movie_main_image)
        this.movieGenre = view.findViewById(R.id.movie_genre_text)
        this.movieDate = view.findViewById(R.id.movie_date)
        this.movieAge = view.findViewById(R.id.age_limit)
        this.movieTitle = view.findViewById(R.id.movie_title)
        this.movieDescription = view.findViewById(R.id.movie_description)
        raitingStar1 = view.findViewById(R.id.raiting_star_1)
        raitingStar2 = view.findViewById(R.id.raiting_star_2)
        raitingStar3 = view.findViewById(R.id.raiting_star_3)
        raitingStar4 = view.findViewById(R.id.raiting_star_4)
        raitingStar5 = view.findViewById(R.id.raiting_star_5)

        val position = arguments?.getInt("MOVIE_POSITION")
        val isUpdated = arguments?.getBoolean("IS_UPDATED")
        if (isUpdated == true) {
            this.moviesModel = MoviesModel(MovieSecondDataBase())
        } else {
            this.moviesModel = MoviesModel(MoviesDataSourceImpl())
        }
        this.movies = moviesModel.getMovies()
        this.movie = movies[position!!]


        val recyclerView: RecyclerView = view.findViewById(R.id.actor_recyclerview)
        actorsModel = ActorsModel(MovieActorDataBase())
        val adapter = MovieActorAdapter(actorsModel.getActors())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 1, RecyclerView.HORIZONTAL, false)
        updateUI()

        return view
    }


    private fun updateUI() {
        this.movieImage.load(this.movie.imageUrl ?: "https://i.ibb.co/Bf42WH6/900-600.jpg")
        this.movieAge.text = this.movie.ageRestriction ?: "99+"
        this.movieTitle.text = this.movie.title ?: "Фильму быть!"
        this.movieDescription.text = this.movie.description
            ?: "Здесь должно быть описание фильма. Возможно, когда-нибудь оно будет. Оставайтесь с нами"
        this.movieGenre.text = this.movie.genre ?: "Жанр"
        this.movieDate.text = this.movie.date ?: "00.00.1900"

        when (movie.rateScore) {
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

    companion object {
        fun newInstance(position: Int): MovieCardFragment {
            val args = Bundle()
            args.apply {
                putInt("MOVIE_POSITION", position)
            }
            val fragment = MovieCardFragment()
            fragment.arguments = args
            return fragment
        }
    }
}