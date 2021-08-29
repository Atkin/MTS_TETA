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
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.item_movie.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import ru.projectatkin.education.*
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.Actors
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.Movies
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.Interface.ApiRequests
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Genres.GenresDB
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.model.Movies.MovieDB
import ru.projectatkin.education.ModelAndData.workmanager.MovieDownloadWorker
import ru.projectatkin.education.R
import ru.projectatkin.education.ViewModels.ActorsViewModel
import ru.projectatkin.education.ViewModels.GenresViewModel
import ru.projectatkin.education.ViewModels.MoviesViewModel
import ru.projectatkin.education.view.Adapters.MovieGenreRecyclerAdapter
import ru.projectatkin.education.view.Adapters.MovieRecyclerAdapter
import java.util.concurrent.TimeUnit

const val TAG_HOME = "HomeFragment"
const val BASE_URL = "https://api.themoviedb.org"
const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/original"
const val BASE_ACTOR_URL = "https://image.tmdb.org/t/p/w500"

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

        //Starting workmanager
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<MovieDownloadWorker>(
            1, TimeUnit.DAYS,
            5, TimeUnit.MINUTES
        )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("MoviesDownloads")
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueue(workRequest)

        return view
    }

    private fun downloadFilms() {
        runBlocking {
            launch {
                val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_star_black)
                    .setContentTitle("Downloads!")
                    .setContentText("Please, wait!")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(requireContext())) {
                    notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
                }
                addMoviesBDList(moviesViewModel)
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

    private fun addMoviesBDList(
        moviesViewModel: MoviesViewModel
    ) {
        moviesViewModel.deleteAllMovies()
        for (result in moviesDB.results) {
            val api: ApiRequests = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getAgeRequest(result.id).awaitResponse()
                if (response.isSuccessful) {
                    var age = response.body()!!
                    var ageUs = ""
                    var ageRu = ""
                    var resultAge = "12+"
                    for (ages in age.results) {
                        if (ages.iso_3166_1 == "US") {
                            ageUs = ages.release_dates[0].certification
                        } else if (ages.iso_3166_1 == "RU") {
                            ageRu = ages.release_dates[0].certification
                        }
                    }
                    if (ageRu != "") {
                        resultAge = ageRu
                    } else {
                        resultAge = ageUs
                    }

                    moviesViewModel.addMovie(
                        Movies(
                            result.original_title,
                            result.overview,
                            ((result.vote_average) / 2).toInt(),
                            if (resultAge != "")
                                resultAge
                            else "12+",
                            if (isValidUrl(BASE_POSTER_URL + result.poster_path)) {
                                BASE_POSTER_URL + result.poster_path
                            } else {
                                "https://i.ibb.co/Bf42WH6/900-600.jpg"
                            },
                            result.genre_ids[0],
                            result.release_date,
                            result.id
                        )
                    )
                }

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
                            if (isValidUrl(BASE_ACTOR_URL + actor.profile_path)) {
                                BASE_ACTOR_URL + actor.profile_path
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

    override fun onCellClickListener(title: Int?, moviesId: Int?, genresId: Int?) {
        getActorsDB(moviesId!!)
        val bundle = Bundle()
        bundle.putInt("position", title!!)
        bundle.putInt("moviesId", moviesId!!)
        bundle.putBoolean("downloaded", downloadStatus)
        bundle.putString("genre", sharedPreference.getGenre(genresId.toString(), "GENRE"))
        //root_movie.transitionToEnd()

        findNavController().navigate(R.id.action_fragmentHome_to_fragmentDetails, bundle)
    }

    override fun onCellClickListenerGenre(title: String?) {
        Log.d(TAG_HOME, "Empty")
    }

    fun isValidUrl(url: String?): Boolean {
        if (url != null) {
            val p = Patterns.WEB_URL
            val m = p.matcher(url)
            return m.matches()
        } else return false
    }
}
