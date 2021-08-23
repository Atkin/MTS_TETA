package ru.projectatkin.education.ModelAndData.data.lowercase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.Actors
import ru.projectatkin.education.ModelAndData.data.lowercase.Actors.ActorsDao
import ru.projectatkin.education.ModelAndData.data.lowercase.Genre.GenreDao
import ru.projectatkin.education.ModelAndData.data.lowercase.Genre.Genres
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.Movies
import ru.projectatkin.education.ModelAndData.data.lowercase.Movies.MoviesDao
import ru.projectatkin.education.ModelAndData.data.lowercase.Profile.Profile
import ru.projectatkin.education.ModelAndData.data.lowercase.Profile.ProfileDao
import ru.projectatkin.education.ModelAndData.data.lowercase.Retrofit.Interface.ApiRequests
import ru.projectatkin.education.SharedPreference
import ru.projectatkin.education.view.fragments.BASE_URL
import ru.projectatkin.education.view.fragments.TAG_PROFILE

@Database(
    entities = arrayOf(Movies::class, Profile::class, Genres::class, Actors::class),
    version = 2,
    exportSchema = false
)
@TypeConverters(MoviesTypeConverters::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun profileDao(): ProfileDao
    abstract fun genresDao(): GenreDao
    abstract fun actorsDao(): ActorsDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        private fun createInstance(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "MoviesDatabase"
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Thread(Runnable { prepopulateDb(context, getInstance(context)) }).start()
                    }
                }).addMigrations(MIGRATION_1_2)
                .build()

        fun getInstance(context: Context): MoviesDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = createInstance(context)
                }
                return INSTANCE!!
            }
        }

        private fun prepopulateDb(context: Context, db: MoviesDatabase) {
            //add default film genres
            val api: ApiRequests = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getGenresDBList().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d(TAG_PROFILE, data.genres[0].name)
                    var sharedPreference: SharedPreference
                    sharedPreference = SharedPreference(context)
                    for (genre in data.genres) {
                        sharedPreference.saveGenre(genre.id.toString(), genre.name)
                        db.genresDao().insertDefault(
                            Genres(
                                genre.name,
                                genre.id
                            )
                        )
                    }
                }
            }


            //Add default actors
            db.actorsDao().insertDefault(
                Actors(
                    "Евгений Моргунов",
                    "https://i.ibb.co/K92rzch/1.jpg",
                    1
                )
            )

            db.actorsDao().insertDefault(
                Actors(
                    "Юрий Никулин",
                    "https://i.ibb.co/8D2gJn6/2.jpg",
                    2
                )
            )

            db.actorsDao().insertDefault(
                Actors(
                    "Георгий Вицин",
                    "https://i.ibb.co/nwN5xw5/3.jpg",
                    3
                )
            )

            val defaultGenres = listOf(1)
            //Add default Movies
            db.moviesDao().insertDefault(
                Movies(
                    "Гнев человеческий",
                    "Эйч — загадочный и холодный на вид джентльмен, но внутри него пылает жажда справедливости. Преследуя...",
                    3,
                    "18+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5JP9X5tCZ6qz7DYMabLmrQirlWh.jpg",
                    1,
                    "04.03.2021",

                    0
                )
            )

            db.moviesDao().insertDefault(
                Movies(
                    "Мортал Комбат",
                    "Боец смешанных единоборств Коул Янг не раз соглашался проиграть за деньги. Он не знает о своем наследии...",
                    5,
                    "18+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/pMIixvHwsD5RZxbvgsDSNkpKy0R.jpg",
                    1,
                    "08.04.2021",
                    1
                )
            )

            db.moviesDao().insertDefault(
                Movies(
                    "Упс... Приплыли!",
                    "От Великого потопа зверей спас ковчег. Но спустя полгода скитаний они готовы сбежать с него куда угодно...",
                    5,
                    "6+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/546RNYy9Wi5wgboQ7EtD6i0DY5D.jpg",
                    13,
                    "29.04.202",
                    2
                )
            )

            db.moviesDao().insertDefault(
                Movies(
                    "The Box",
                    "Уличный музыкант знакомится с музыкальным продюсером, и они вдвоём отправляются в путешествие...",
                    4,
                    "12+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/fq3DSw74fAodrbLiSv0BW1Ya4Ae.jpg",
                    5,
                    "13.05.2021",
                    3
                )
            )

            db.moviesDao().insertDefault(
                Movies(
                    "Сага о Дэнни Эрнандесе",
                    "Tekashi69 или Сикснайн — знаменитый бруклинский рэпер с радужными волосами — прогремел...",
                    2,
                    "18+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5xXGQLVtTAExHY92DHD9ewGmKxf.jpg",
                    14,
                    "29.04.2021",
                    4
                )
            )

            db.moviesDao().insertDefault(
                Movies(
                    "Пчелка Майя",
                    "Когда упрямая пчелка Майя и ее лучший друг Вилли спасают принцессу-муравьишку, начинается сказочное...",
                    4,
                    "0+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/xltjMeLlxywym14NEizl0metO10.jpg",
                    15,
                    "04.05.2021",
                    5
                )
            )

            db.moviesDao().insertDefault(
                Movies(
                    "Круэлла",
                    "Невероятно одаренная мошенница по имени Эстелла решает сделать себе имя в мире моды.",
                    4,
                    "12+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hUfyYGP9Xf6cHF9y44JXJV3NxZM.jpg",
                    3,
                    "03.06.2021",
                    6
                )
            )

            db.moviesDao().insertDefault(
                Movies(
                    "Чёрная вдова",
                    "Чёрной Вдове придется вспомнить о том, что было в её жизни задолго до присоединения к команде Мстителей",
                    3,
                    "16+",
                    "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/mbtN6V6y5kdawvAkzqN4ohi576a.jpg",
                    1,
                    "06.2021",
                    7
                )
            )
        }
    }
}