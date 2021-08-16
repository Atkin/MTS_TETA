package ru.projectatkin.education.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.projectatkin.education.CellClickListener
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.MoviesAndGenres
import ru.projectatkin.education.R
import ru.projectatkin.education.ViewModels.ActorsViewModel
import ru.projectatkin.education.ViewModels.MoviesViewModel
import ru.projectatkin.education.view.Adapters.MovieActorAdapter
import ru.projectatkin.education.view.Adapters.MovieRecyclerAdapter

const val TAG_DETAILS = "DetailsFragment"

class FragmentDetails() : Fragment(), CellClickListener {
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
    private lateinit var bottomNavigationBar: BottomNavigationView

    private lateinit var movieAdapter: MovieRecyclerAdapter
    private lateinit var moviesViewModel: MoviesViewModel
    lateinit var actorsAdapter: MovieActorAdapter

    private lateinit var  actorsRecyclerView: RecyclerView
    private lateinit var actorsViewModel: ActorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)


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

        val position = arguments?.getInt("position")


        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        actorsViewModel = ViewModelProvider(this).get(ActorsViewModel::class.java)

        actorsRecyclerView = view.findViewById(R.id.actor_recyclerview)
        actorsAdapter = MovieActorAdapter()
        actorsRecyclerView.adapter = actorsAdapter

        movieAdapter = MovieRecyclerAdapter(this)

        actorsRecyclerView.layoutManager = GridLayoutManager(activity, 1, RecyclerView.HORIZONTAL, false)

        actorsViewModel.readAllData.observe(viewLifecycleOwner, Observer {words ->
            words?.let {
                actorsAdapter.updateActorsList(it)
            }
        })

        moviesViewModel.readMovie.observe(viewLifecycleOwner, Observer {words ->
            words?.let {
                movieAdapter.updateMoviesAndGenreList(it)
            }
            updateUI(words[position!!])
        })

        bottomNavigationBar = view.findViewById(R.id.list_bottom_navigation_details)
        this.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_home_button -> {
                    findNavController().navigate(R.id.action_fragmentDetails_to_fragmentHome, null)
                }
                R.id.list_profile_button -> {
                    findNavController().navigate(
                        R.id.action_fragmentDetails_to_fragmentProfile,
                        null
                    )
                }
                else -> {
                }
            }
            true
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_DETAILS, "Destroy")
    }

    private fun updateUI(movie: MoviesAndGenres) {
        movieImage.load(movie.imageUrl ?: "https://i.ibb.co/Bf42WH6/900-600.jpg")
        movieAge.text = movie.ageRestriction ?: "99+"
        movieTitle.text = movie.title ?: "Фильму быть!"
        movieDescription.text = movie.description
            ?: "Здесь должно быть описание фильма. Возможно, когда-нибудь оно будет. Оставайтесь с нами"
        movieGenre.text = movie.genreTitle ?: "Жанр"
        movieDate.text = movie.date ?: "00.00.1900"

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

    override fun onCellClickListener(position: String?, moviesId: Int?) {
        Log.d(TAG_DETAILS, "Empty")
    }
}