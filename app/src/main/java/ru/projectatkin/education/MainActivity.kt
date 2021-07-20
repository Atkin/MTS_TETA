package ru.projectatkin.education

import MovieItemOffsetDecoration
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.projectatkin.education.data.features.movies.MoviesDataSourceImpl
import ru.projectatkin.education.data.features.movies.MovieGenreDataBase

class MainActivity : AppCompatActivity(), CellClickListener, CellClickListenerGenre {
    private lateinit var moviesModel: MoviesModel
    private lateinit var moviesGenreModel: MoviesGenreModel
    private lateinit var bottomNavigationBar: BottomNavigationView
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewMovie)
        moviesModel = MoviesModel(MoviesDataSourceImpl())
        val adapter = MovieRecyclerAdapter(moviesModel.getMovies(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter.updateList(moviesModel.getMovies())

        val recyclerGenreView: RecyclerView = findViewById(R.id.recyclerViewMovieGenre)
        moviesGenreModel = MoviesGenreModel(MovieGenreDataBase())
        recyclerGenreView.adapter =
            MovieGenreRecyclerAdapter(moviesGenreModel.getMoviesGenre(), this)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels

        val movieDecorator = MovieItemOffsetDecoration(2, width / 20, true, 0)
        recyclerView.addItemDecoration(movieDecorator)

        val genreDecorator = GenreItemOffsetDecoration(1, width / 80, true, 0)
        recyclerGenreView.addItemDecoration(genreDecorator)

        val profileFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        bottomNavigationBar = findViewById(R.id.list_bottom_navigation)
        this.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_home_button -> {
                    if (currentFragment is MovieProfileFragment) {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.popBackStack()
                    } else if (currentFragment is MovieCardFragment) {
                        supportFragmentManager.popBackStack()
                    }
                }
                R.id.list_profile_button -> {
                    if (profileFragment == null) {
                        switchToFragment(MovieProfileFragment())
                    }
                }
                else -> {
                }
            }
            true
        }
    }

    fun switchToFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null) // что бы не делать стек фрагментов
            .commitAllowingStateLoss()
        currentFragment = fragment
    }

    override fun onCellClickListener(position: String?) {
        val movieFragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if (movieFragment == null) {
            switchToFragment(MovieCardFragment.newInstance(position!!.toInt()))
        }
    }

    override fun onCellClickListenerGenre(title: String?) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
    }
}

interface CellClickListener {
    fun onCellClickListener(title: String?)
}

interface CellClickListenerGenre {
    fun onCellClickListenerGenre(title: String?)
}