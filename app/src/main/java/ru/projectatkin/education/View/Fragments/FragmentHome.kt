package ru.projectatkin.education.View.Fragments

import MovieItemOffsetDecoration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import ru.projectatkin.education.*
import ru.projectatkin.education.View.Adapters.MovieGenreRecyclerAdapter
import ru.projectatkin.education.View.Adapters.MovieRecyclerAdapter
import ru.projectatkin.education.ViewModels.GenreViewModel
import ru.projectatkin.education.ViewModels.MovieViewModel
import java.util.*

const val TAG_HOME = "HomeFragment"

class FragmentHome : Fragment(), CellClickListener, CellClickListenerGenre, CoroutineScope {
    private lateinit var bottomNavigationBar: BottomNavigationView

    protected val job = SupervisorJob() // экземпляр Job для данного фрагмента
    override val coroutineContext = Dispatchers.Main.immediate + job

    protected var downloadStatus: Boolean = false
    lateinit var sharedPreference: SharedPreference

    private val genreViewModel: GenreViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var genreAdapter: MovieGenreRecyclerAdapter
    private lateinit var genreRecyclerView: RecyclerView

    private lateinit var movieAdapter: MovieRecyclerAdapter
    private lateinit var movieRecyclerView: RecyclerView

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreference = SharedPreference(requireContext())
        downloadStatus = sharedPreference.getValueBoolien("downloaded", false)
        if (downloadStatus == true) {
            movieViewModel.tempUpdate()
        }

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swip)
        swipeRefreshLayout.setOnRefreshListener {
            downloadFilms(movieRecyclerView)
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
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        val genreDecorator = GenreItemOffsetDecoration(1, width / 80, true, 0)
        this.genreRecyclerView.addItemDecoration(genreDecorator)
        val movieDecorator = MovieItemOffsetDecoration(2, width / 20, true, 0)
        movieRecyclerView.addItemDecoration(movieDecorator)

        genreViewModel.dataListGenre.observe(viewLifecycleOwner, Observer(genreAdapter::updateList))
        movieViewModel.dataListMovie.observe(viewLifecycleOwner, Observer(movieAdapter::updateList))

        genreViewModel.loadGenre()
        movieViewModel.loadMovies()

        bottomNavigationBar = view.findViewById(R.id.list_bottom_navigation)
        this.bottomNavigationBar.setOnNavigationItemSelectedListener {
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

    fun downloadFilms(view: View) {
        runBlocking {
            launch {
                if (this@FragmentHome.downloadStatus == false) {
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
                    delay(5000)
                    this@FragmentHome.movieViewModel.tempUpdate()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        downloadStatus = sharedPreference.getValueBoolien("downloaded", false)
        if (downloadStatus == true) {
            movieViewModel.tempUpdate()
        }
    }

    suspend fun download() = coroutineScope {
        val scope = CoroutineScope(Dispatchers.Default)
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d(TAG_HOME, "Error")
            this@FragmentHome.downloadStatus = true
        }
        val job = scope.launch(handler) {
            Thread.sleep(5000)
            throw ArithmeticException()
        }
        job.join()
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

    override fun onCellClickListener(position: String?) {
        val bundle = Bundle()
        bundle.putInt("position", position!!.toInt())
        bundle.putBoolean("downloaded", downloadStatus)
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentDetails, bundle)
    }

    override fun onCellClickListenerGenre(title: String?) {
        Log.d(TAG_HOME, "Empty")
    }
}
