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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import ru.projectatkin.education.*
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies
import ru.projectatkin.education.ModelAndData.data.lowercase.MoviesViewModel
import ru.projectatkin.education.View.Adapters.MovieGenreRecyclerAdapter
import ru.projectatkin.education.View.Adapters.MovieRecyclerAdapter
import ru.projectatkin.education.ViewModels.GenreViewModel
import java.util.*

const val TAG_HOME = "HomeFragment"

class FragmentHome : Fragment(), CellClickListener, CellClickListenerGenre, CoroutineScope {
    private lateinit var bottomNavigationBar: BottomNavigationView

    protected val job = SupervisorJob() // экземпляр Job для данного фрагмента
    override val coroutineContext = Dispatchers.Main.immediate + job

    protected var downloadStatus: Boolean = false
    lateinit var sharedPreference: SharedPreference

    private val genreViewModel: GenreViewModel by viewModels()

    //private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var moviesViewModel: MoviesViewModel

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
        //if (downloadStatus == true) {
         //   // movieViewModel.tempUpdate()
           // addMoviesList(moviesViewModel, downloadStatus)
        //}

        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        addMoviesList(moviesViewModel, downloadStatus)

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

        //moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        moviesViewModel.readAllData.observe(viewLifecycleOwner, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {
                movieAdapter.updateMoviesList(it)
            }
        })
        //movieViewModel.dataListMovie.observe(viewLifecycleOwner, Observer(movieAdapter::updateList))

        genreViewModel.loadGenre()
        //movieViewModel.loadMovies()

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
                    // this@FragmentHome.movieViewModel.tempUpdate()
                    addMoviesList(moviesViewModel, this@FragmentHome.downloadStatus)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        downloadStatus = sharedPreference.getValueBoolien("downloaded", false)
        if (downloadStatus == true) {
            // movieViewModel.tempUpdate()
            addMoviesList(moviesViewModel, downloadStatus)
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

    fun addMoviesList(moviesViewModel: MoviesViewModel, downloads: Boolean) {
        moviesViewModel.deleteAllMovies()
        if (downloads == false) {
            moviesViewModel.addMovie(
                Movies(
                    "Гнев человеческий",
                    "Эйч — загадочный и холодный на вид джентльмен, но внутри него пылает жажда справедливости. Преследуя...",
                    3,
                    "18+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5JP9X5tCZ6qz7DYMabLmrQirlWh.jpg",
                    "Боевик",
                    "04.03.2021", 0
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Мортал Комбат",
                    "Боец смешанных единоборств Коул Янг не раз соглашался проиграть за деньги. Он не знает о своем наследии...",
                    5,
                    "18+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/pMIixvHwsD5RZxbvgsDSNkpKy0R.jpg",
                    "Боевик",
                    "08.04.2021",
                    1
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Упс... Приплыли!",
                    "От Великого потопа зверей спас ковчег. Но спустя полгода скитаний они готовы сбежать с него куда угодно...",
                    5,
                    "6+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/546RNYy9Wi5wgboQ7EtD6i0DY5D.jpg",
                    "Детский",
                    "29.04.202",
                    2
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "The Box",
                    "Уличный музыкант знакомится с музыкальным продюсером, и они вдвоём отправляются в путешествие...",
                    4,
                    "12+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/fq3DSw74fAodrbLiSv0BW1Ya4Ae.jpg",
                    "Драма",
                    "13.05.2021",
                    3
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Сага о Дэнни Эрнандесе",
                    "Tekashi69 или Сикснайн — знаменитый бруклинский рэпер с радужными волосами — прогремел...",
                    2,
                    "18+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5xXGQLVtTAExHY92DHD9ewGmKxf.jpg",
                    "Докум.",
                    "29.04.2021",
                    4
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Пчелка Майя",
                    "Когда упрямая пчелка Майя и ее лучший друг Вилли спасают принцессу-муравьишку, начинается сказочное...",
                    4,
                    "0+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/xltjMeLlxywym14NEizl0metO10.jpg",
                    "Мульфильм",
                    "04.05.2021",
                    5
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Круэлла",
                    "Невероятно одаренная мошенница по имени Эстелла решает сделать себе имя в мире моды.",
                    4,
                    "12+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hUfyYGP9Xf6cHF9y44JXJV3NxZM.jpg",
                    "Комедия",
                    "03.06.2021",
                    6
                )
            )

            moviesViewModel.addMovie(
                Movies(
                    "Чёрная вдова",
                    "Чёрной Вдове придется вспомнить о том, что было в её жизни задолго до присоединения к команде Мстителей",
                    3,
                    "16+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/mbtN6V6y5kdawvAkzqN4ohi576a.jpg",
                    "Боевик",
                    "06.2021", 7
                )
            )
        } else {
            moviesViewModel.addMovie(
                Movies(
                    "Побег из Шоушенка",
                    "Выдающаяся драма о силе таланта, важности дружбы, стремлении к свободе и Рите Хэйворт",
                    4,
                    "16+",
                    "https://i.ibb.co/QMgj3d5/1.jpg",
                    "Драма",
                    "10.09.1994",
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
                    "Драма",
                    "06.12.1999",
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
                    "Фэнтези",
                    "10.12.2001",
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
                    "Фэнтези",
                    "05.12.2002",
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
                    "Фэнтези",
                    "01.12.2003",
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
                    "Фантастика",
                    "26.10.2014",
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
                    "Драма",
                    "23.06.1994",
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
                    "Комедия",
                    "17.09.1973",
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
                    "Драма",
                    "30.11.1993",
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
                    "Драма",
                    "12.06.1994",
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
