package ru.projectatkin.education.view.fragments

import MovieItemOffsetDecoration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import ru.projectatkin.education.*
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.Actors
import ru.projectatkin.education.ModelAndData.data.lowercase.Genre.Genres
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.Movies
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.Interface.ApiRequests
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Genres.GenresDB
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Movies.MovieDB
import ru.projectatkin.education.ViewModels.ActorsViewModel
import ru.projectatkin.education.ViewModels.GenresViewModel
import ru.projectatkin.education.ViewModels.MoviesViewModel
import ru.projectatkin.education.view.Adapters.MovieGenreRecyclerAdapter
import ru.projectatkin.education.view.Adapters.MovieRecyclerAdapter
import java.util.*

const val TAG_HOME = "HomeFragment"
const val BASE_URL = "https://api.themoviedb.org"


class FragmentHome : Fragment(), CellClickListener, CellClickListenerGenre, CoroutineScope {
    private lateinit var bottomNavigationBar: BottomNavigationView

    private val job = SupervisorJob() // экземпляр Job для данного фрагмента
    override val coroutineContext = Dispatchers.Main.immediate + job

    private var downloadStatus: Boolean = false
    private lateinit var sharedPreference: SharedPreference

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var genresViewModel: GenresViewModel
    private lateinit var actorsViewModel: ActorsViewModel

    private lateinit var genreAdapter: MovieGenreRecyclerAdapter
    private lateinit var genreRecyclerView: RecyclerView

    private lateinit var movieAdapter: MovieRecyclerAdapter
    private lateinit var movieRecyclerView: RecyclerView

    private lateinit var moviesDB: MovieDB
    private lateinit var genresDB: GenresDB

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreference = SharedPreference(requireContext())
        downloadStatus = sharedPreference.getValueBoolien("downloaded", false)

        actorsViewModel = ViewModelProvider(this).get(ActorsViewModel::class.java)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        genresViewModel = ViewModelProvider(this).get(GenresViewModel::class.java)


        getMoviesDB()
        getGenresDB()

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swip)
        swipeRefreshLayout.setOnRefreshListener {
            downloadFilms()
            swipeRefreshLayout.isRefreshing = false
        }

        genreRecyclerView = view.findViewById(R.id.recyclerViewMovieGenre)
        genreAdapter = MovieGenreRecyclerAdapter(this)
        genreRecyclerView.adapter = genreAdapter

        movieRecyclerView = view.findViewById(R.id.recyclerViewMovie)
        movieAdapter = MovieRecyclerAdapter(this)
        movieRecyclerView.adapter = movieAdapter
        movieRecyclerView.layoutManager = GridLayoutManager(activity, 2)

        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val genreDecorator = GenreItemOffsetDecoration(1, width / 80, true, 0)
        this.genreRecyclerView.addItemDecoration(genreDecorator)
        val movieDecorator = MovieItemOffsetDecoration(2, width / 20, true, 0)
        movieRecyclerView.addItemDecoration(movieDecorator)

        moviesViewModel.readAllData.observe(viewLifecycleOwner, { words ->
            words?.let {
                movieAdapter.updateMoviesList(it)
            }
        })

        genresViewModel.readAllData.observe(viewLifecycleOwner, { words ->
            words?.let {
                genreAdapter.updateList(it)
            }
        })

        bottomNavigationBar = view.findViewById(R.id.list_bottom_navigation)
        this.bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.list_home_button -> {
                    Log.d(TAG_HOME, "Same")
                }
                R.id.list_profile_button -> {
                    findNavController().navigate(R.id.action_fragmentHome_to_fragmentProfile, null)
                }
                else -> {
                    Log.d(TAG_HOME, "Empty")
                }
            }
            true
        }
        return view
    }

    private fun downloadFilms() {
        runBlocking {
            launch {
                if (!this@FragmentHome.downloadStatus) {
                    download()
                    val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_star_black)
                        .setContentTitle("Downloads error!")
                        .setContentText("Please, try again!")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    with(NotificationManagerCompat.from(requireContext())) {
                        notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
                    }
                } else {
                    delay(2000)
                    addMoviesBDList(moviesViewModel, this@FragmentHome.downloadStatus, moviesDB)
                    addGenresDBList(genresViewModel, this@FragmentHome.downloadStatus, genresDB)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        downloadStatus = sharedPreference.getValueBoolien("downloaded", false)
        if (downloadStatus) {
            getMoviesDB()
        }
    }

    private suspend fun download() = coroutineScope {
        val scope = CoroutineScope(Dispatchers.Default)
        val handler = CoroutineExceptionHandler { _, _ ->
            Log.d(TAG_HOME, "Error")
            this@FragmentHome.downloadStatus = true
        }
        val job = scope.launch(handler) {
            delay(2000)
            throw ArithmeticException()
        }
        job.join()
    }


    private fun addMoviesBDList(
        moviesViewModel: MoviesViewModel,
        downloads: Boolean,
        moviesDB: MovieDB
    ) {
        if (downloads) {
            moviesViewModel.deleteAllMovies()
            for (result in moviesDB.results) {
                moviesViewModel.addMovie(
                    Movies(
                        result.original_title,
                        result.overview,
                        ((result.vote_average) / 2).toInt(),
                        "12+",
                        if (isValidUrl(result.poster_path)) {
                            result.poster_path
                        } else {
                            "https://i.ibb.co/Bf42WH6/900-600.jpg"
                        },
                        result.genre_ids[0],
                        result.release_date,
                        0,
                        result.id
                    )
                )
            }
        }
    }

    private fun addGenresDBList(
        genresViewModel: GenresViewModel,
        downloads: Boolean,
        genresDB: GenresDB
    ) {
        if (downloads) {
            genresViewModel.deleteAllGenres()
            for (genre in genresDB.genres) {
                genresViewModel.addProfile(
                    Genres(
                        genre.name,
                        genre.id
                    )
                )
            }
        }
    }

    private fun getMoviesDB() {
        val api: ApiRequests = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getMovieFacts().awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()!!
                moviesDB = response.body()!!
                Log.d(TAG_PROFILE, data.total_results.toString())

            }
        }
    }

    private fun getActorsDB(moviesId: Int) {
        val api: ApiRequests = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getActorsDBList(moviesId).awaitResponse()
            if (response.isSuccessful) {
                actorsViewModel.deleteAllActors()
                for (actor in response.body()!!.cast) {
                    actorsViewModel.addActor(
                        Actors(
                            actor.name,
                            if (isValidUrl(actor.profile_path)) {
                                actor.profile_path
                            } else {
                                "https://i.ibb.co/8D2gJn6/2.jpg"
                            },
                            actor.id
                        )
                    )

                }
            }
        }
    }

    private fun getGenresDB() {
        val api: ApiRequests = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getGenresDBList().awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()!!
                genresDB = response.body()!!
                Log.d(TAG_PROFILE, data.genres[0].name)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.job.cancel()
    }

    override fun onPause() {
        super.onPause()
        sharedPreference.save("downloaded", downloadStatus)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("downloaded", downloadStatus)
    }

    override fun onCellClickListener(title: String?, moviesId: Int?) {
        getActorsDB(moviesId!!)
        val bundle = Bundle()
        bundle.putInt("position", title!!.toInt())
        bundle.putInt("moviesId", moviesId!!)
        bundle.putBoolean("downloaded", downloadStatus)
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentDetails, bundle)
    }

    override fun onCellClickListenerGenre(title: String?) {
        Log.d(TAG_HOME, "Empty")
    }

    fun isValidUrl(url: String?): Boolean {
        if(url != null) {
            val p = Patterns.WEB_URL
            val m = p.matcher(url)
            return m.matches()
        } else return  false
    }
}