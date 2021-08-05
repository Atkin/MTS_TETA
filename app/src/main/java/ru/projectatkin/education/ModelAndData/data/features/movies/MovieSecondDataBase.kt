package ru.projectatkin.education.ModelAndData.data.features.movies

import ru.projectatkin.education.ModelAndData.data.dto.MovieDto

class MovieSecondDataBase : MoviesDataSource {
    override fun getMovies() = mutableListOf(
        MovieDto(
            title = "Побег из Шоушенка",
            description = "Выдающаяся драма о силе таланта, важности дружбы, стремлении к свободе и Рите Хэйворт",
            rateScore = 4,
            ageRestriction = "16+",
            imageUrl = "https://i.ibb.co/QMgj3d5/1.jpg",
            genre = "Драма",
            date = "10.09.1994"
        ),
        MovieDto(
            title = "Зеленая миля",
            description = "В тюрьме для смертников появляется заключенный с божественным даром. Мистическая драма по роману Стивена Кинга",
            rateScore = 4,
            ageRestriction = "16+",
            imageUrl = "https://i.ibb.co/L1kdndM/2.jpg",
            genre = "Драма",
            date = "06.12.1999"
        ),
        MovieDto(
            title = "Властелин колец: Братство Кольца",
            description = "Фродо Бэггинс отправляется спасать Средиземье. Первая часть культовой фэнтези-трилогии Питера Джексона",
            rateScore = 4,
            ageRestriction = "12+",
            imageUrl = "https://i.ibb.co/tZDXdTy/5.jpg",
            genre = "Фэнтези",
            date = "10.12.2001"
        ),
        MovieDto(
            title = "Властелин колец: Две крепости",
            description = "Голлум ведет хоббитов в Мордор, а великие армии готовятся к битве. Вторая часть трилогии, два «Оскара»",
            rateScore = 4,
            ageRestriction = "12+",
            imageUrl = "https://i.ibb.co/C7F7JSm/4.jpg",
            genre = "Фэнтези",
            date = "05.12.2002"
        ),
        MovieDto(
            title = "Властелин колец: Возвращение короля",
            description = "Арагорн штурмует Мордор, а Фродо устал бороться с чарами кольца. Эффектный финал саги, собравший 11 «Оскаров»",
            rateScore = 4,
            ageRestriction = "12+",
            imageUrl = "https://i.ibb.co/qBXqfkx/e-HHH8t3-C1k-ZEUy8t-YQYo-Sn-Uy-Cfrkb-Xwhrayv9cb4b-XZd-V1moys-P795sco-HITll-bk-KVFKmo-Tn8-ZN6-xk-TKPj.jpg",
            genre = "Фэнтези",
            date = "01.12.2003"
        ),
        MovieDto(
            title = "Интерстеллар",
            description = "Фантастический эпос про задыхающуюся Землю, космические полеты и парадоксы времени.",
            rateScore = 4,
            ageRestriction = "16+",
            imageUrl = "https://i.ibb.co/JrqXwxX/6.jpg",
            genre = "Фантастика",
            date = "26.10.2014"
        ),
        MovieDto(
            title = "Форрест Гамп",
            description = "Полувековая история США глазами чудака из Алабамы. Абсолютная классика Роберта Земекиса с Томом Хэнксом",
            rateScore = 4,
            ageRestriction = "12+",
            imageUrl = "https://i.ibb.co/7WmKX6D/7.jpg",
            genre = "Драма",
            date = "23.06.1994"
        ),
        MovieDto(
            title = "Иван Васильевич меняет профессию",
            description = "Иван Грозный отвечает на телефон, пока его тезка-пенсионер сидит на троне. Советский хит о липовом государе",
            rateScore = 4,
            ageRestriction = "6+",
            imageUrl = "https://i.ibb.co/pnggLdb/8.jpg",
            genre = "Комедия",
            date = "17.09.1973"
        ),
        MovieDto(
            title = "Список Шиндлера",
            description = "История немецкого промышленника, спасшего тысячи жизней во время Холокоста. Драма Стивена Спилберга",
            rateScore = 4,
            ageRestriction = "16+",
            imageUrl = "https://i.ibb.co/dL6Rd9G/9.jpg",
            genre = "Драма",
            date = "30.11.1993"
        ),
        MovieDto(
            title = "Король Лев",
            description = "Львенок Симба бросает вызов дяде-убийце. Величественный саундтрек, рисованная анимация и шекспировский размах",
            rateScore = 4,
            ageRestriction = "0+",
            imageUrl = "https://i.ibb.co/nz1ZgKV/10.jpg",
            genre = "Драма",
            date = "12.06.1994"
        )
    )
}