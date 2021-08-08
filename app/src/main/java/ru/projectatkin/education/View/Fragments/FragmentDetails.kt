package ru.projectatkin.education.View.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.projectatkin.education.CellClickListener
import ru.projectatkin.education.ModelAndData.data.ActorsModel
import ru.projectatkin.education.ModelAndData.data.features.movies.MovieActorDataBase
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies
import ru.projectatkin.education.ModelAndData.data.lowercase.MoviesViewModel
import ru.projectatkin.education.R
import ru.projectatkin.education.View.Adapters.MovieActorAdapter
import ru.projectatkin.education.View.Adapters.MovieRecyclerAdapter
import ru.projectatkin.education.ViewModels.MovieViewModel

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
    private lateinit var actorsModel: ActorsModel
    private lateinit var bottomNavigationBar: BottomNavigationView

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieRecyclerAdapter

    private lateinit var moviesViewModel: MoviesViewModel

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
        val downloadStatus = arguments?.getBoolean("downloaded")

        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.actor_recyclerview)
        actorsModel = ActorsModel(MovieActorDataBase())
        val adapter = MovieActorAdapter(actorsModel.getActors())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 1, RecyclerView.HORIZONTAL, false)

        movieAdapter = MovieRecyclerAdapter(this)

        moviesViewModel.readAllData.observe(viewLifecycleOwner, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {
                movieAdapter.updateMoviesList(it)
            }
            updateUI(words[position!!])
        })

       // movieViewModel.dataListMovie.observe(viewLifecycleOwner, Observer {
       //     if (downloadStatus == true) {
       //         movieViewModel.tempUpdate()
       //     }
       //     updateUI(it[position!!])
       // })
       // movieViewModel.loadMovies()

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

    private fun updateUI(movie: Movies) {
        movieImage.load(movie.imageUrl ?: "https://i.ibb.co/Bf42WH6/900-600.jpg")
        movieAge.text = movie.ageRestriction ?: "99+"
        movieTitle.text = movie.title ?: "Фильму быть!"
        movieDescription.text = movie.description
            ?: "Здесь должно быть описание фильма. Возможно, когда-нибудь оно будет. Оставайтесь с нами"
        movieGenre.text = movie.genre ?: "Жанр"
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

    override fun onCellClickListener(position: String?) {
        Log.d(TAG_DETAILS, "Empty")
    }

    companion object {
        fun newInstance(position: Int): FragmentDetails {
            val args = Bundle()
            args.apply {
                putInt("MOVIE_POSITION", position)
            }
            val fragment = FragmentDetails()
            fragment.arguments = args
            return fragment
        }
    }
}