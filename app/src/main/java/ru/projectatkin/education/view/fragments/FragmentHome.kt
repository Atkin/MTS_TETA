package ru.projectatkin.education.view.fragments

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import ru.projectatkin.education.*
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.Movies
import ru.projectatkin.education.ViewModels.ActorsViewModel
import ru.projectatkin.education.ViewModels.GenresViewModel
import ru.projectatkin.education.ViewModels.MoviesViewModel
import ru.projectatkin.education.view.Adapters.MovieGenreRecyclerAdapter
import ru.projectatkin.education.view.Adapters.MovieRecyclerAdapter
import java.util.*

const val TAG_HOME = "HomeFragment"

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

        addMoviesList(moviesViewModel, downloadStatus)

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
                    delay(5000)
                    addMoviesList(moviesViewModel, this@FragmentHome.downloadStatus)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        downloadStatus = sharedPreference.getValueBoolien("downloaded", false)
        if (downloadStatus) {
            addMoviesList(moviesViewModel, downloadStatus)
        }
    }

    private suspend fun download() = coroutineScope {
        val scope = CoroutineScope(Dispatchers.Default)
        val handler = CoroutineExceptionHandler { _, _ ->
            Log.d(TAG_HOME, "Error")
            this@FragmentHome.downloadStatus = true
        }
        val job = scope.launch(handler) {
            delay(5000)
            throw ArithmeticException()
        }
        job.join()
    }

    private fun addMoviesList(moviesViewModel: MoviesViewModel, downloads: Boolean) {
        if (downloads) {
            moviesViewModel.deleteAllMovies()
            moviesViewModel.addMovie(
                Movies(
                    "Побег из Шоушенка",
                    "Выдающаяся драма о силе таланта, важности дружбы, стремлении к свободе и Рите Хэйворт",
                    4,
                    "16+",
                    "https://i.ibb.co/QMgj3d5/1.jpg",
                    5,
                    "10.09.1994", 0,
                    0
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Зеленая миля",
                    "В тюрьме для смертников появляется заключенный с божественным даром. Мистическая драма по роману Стивена Кинга",
                    4,
                    "16+",
                    "https://i.ibb.co/L1kdndM/2.jpg",
                    5,
                    "06.12.1999", 0,
                    1
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Властелин колец: Братство Кольца",
                    "Фродо Бэггинс отправляется спасать Средиземье. Первая часть культовой фэнтези-трилогии Питера Джексона",
                    4,
                    "12+",
                    "https://i.ibb.co/tZDXdTy/5.jpg",
                    7,
                    "10.12.2001", 0,
                    2
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Властелин колец: Две крепости",
                    "Голлум ведет хоббитов в Мордор, а великие армии готовятся к битве. Вторая часть трилогии, два «Оскара»",
                    4,
                    "12+",
                    "https://i.ibb.co/C7F7JSm/4.jpg",
                    7,
                    "05.12.2002", 0,
                    3
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Властелин колец: Возвращение короля",
                    "Арагорн штурмует Мордор, а Фродо устал бороться с чарами кольца. Эффектный финал саги, собравший 11 «Оскаров»",
                    4,
                    "12+",
                    "https://i.ibb.co/qBXqfkx/e-HHH8t3-C1k-ZEUy8t-YQYo-Sn-Uy-Cfrkb-Xwhrayv9cb4b-XZd-V1moys-P795sco-HITll-bk-KVFKmo-Tn8-ZN6-xk-TKPj.jpg",
                    7,
                    "01.12.2003", 0,
                    4
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Интерстеллар",
                    "Фантастический эпос про задыхающуюся Землю, космические полеты и парадоксы времени.",
                    4,
                    "16+",
                    "https://i.ibb.co/JrqXwxX/6.jpg",
                    16,
                    "26.10.2014", 0,
                    5
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Форрест Гамп",
                    "Полувековая история США глазами чудака из Алабамы. Абсолютная классика Роберта Земекиса с Томом Хэнксом",
                    4,
                    "12+",
                    "https://i.ibb.co/7WmKX6D/7.jpg",
                    5,
                    "23.06.1994", 0,
                    6
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Иван Васильевич меняет профессию",
                    "Иван Грозный отвечает на телефон, пока его тезка-пенсионер сидит на троне. Советский хит о липовом государе",
                    4,
                    "6+",
                    "https://i.ibb.co/pnggLdb/8.jpg",
                    3,
                    "17.09.1973", 0,
                    7
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Список Шиндлера",
                    "История немецкого промышленника, спасшего тысячи жизней во время Холокоста. Драма Стивена Спилберга",
                    4,
                    "16+",
                    "https://i.ibb.co/dL6Rd9G/9.jpg",
                    5,
                    "30.11.1993", 0,
                    8
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Король Лев",
                    "Львенок Симба бросает вызов дяде-убийце. Величественный саундтрек, рисованная анимация и шекспировский размах",
                    4,
                    "0+",
                    "https://i.ibb.co/nz1ZgKV/10.jpg",
                    5,
                    "12.06.1994", 0,
                    9
                )
            )
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

    override fun onCellClickListener(title: String?) {
        val bundle = Bundle()
        bundle.putInt("position", title!!.toInt())
        bundle.putBoolean("downloaded", downloadStatus)
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentDetails, bundle)
    }

    override fun onCellClickListenerGenre(title: String?) {
        Log.d(TAG_HOME, "Empty")
    }
}
