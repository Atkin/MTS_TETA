package ru.projectatkin.education

import MovieItemOffsetDecoration
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.projectatkin.education.data.features.movies.MoviesDataSourceImpl
import ru.projectatkin.education.data.features.movies.MovieGenreDataBase

class MainActivity : AppCompatActivity(), CellClickListener {
	private lateinit var moviesModel: MoviesModel
	private lateinit var moviesGenreModel: MoviesGenreModel

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
		recyclerGenreView.adapter = MovieGenreRecyclerAdapter(moviesGenreModel.getMoviesGenre(), this)

		val displayMetrics = DisplayMetrics()
		windowManager.defaultDisplay.getMetrics(displayMetrics)

		var width = displayMetrics.widthPixels

		val movieDecorator = MovieItemOffsetDecoration(2, width/20, true, 0)
		recyclerView.addItemDecoration(movieDecorator);

		val genreDecorator = GenreItemOffsetDecoration(1, width/80, true, 0)
		recyclerGenreView.addItemDecoration(genreDecorator);
	}

	override fun onCellClickListener(title: String?) {
		Toast.makeText(this,title, Toast.LENGTH_SHORT).show()
	}
}

interface CellClickListener {
	fun onCellClickListener(title: String?)
}