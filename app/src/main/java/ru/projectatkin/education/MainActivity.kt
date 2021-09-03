package ru.projectatkin.education

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import ru.projectatkin.education.ViewModels.GenresViewModel
import ru.projectatkin.education.ViewModels.MoviesViewModel
import ru.projectatkin.education.view.Adapters.MovieGenreRecyclerAdapter
import ru.projectatkin.education.view.Adapters.MovieRecyclerAdapter

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreference:SharedPreference

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var genresViewModel: GenresViewModel

    private lateinit var genreAdapter: MovieGenreRecyclerAdapter
    private lateinit var genreRecyclerView: RecyclerView

    private lateinit var movieAdapter: MovieRecyclerAdapter
    private lateinit var movieRecyclerView: RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = SharedPreference(this)
        sharedPreference.save("downloaded", false)


    }


    override fun onDestroy() {
        super.onDestroy()
        WorkManager.getInstance(this)
            .cancelAllWorkByTag("MoviesDownloads")
    }

}

interface CellClickListener {
    fun onCellClickListener(index: Int?, movieId: Int?, genresId: Int?)
}

interface CellClickListenerGenre {
    fun onCellClickListenerGenre(title: String?)
}