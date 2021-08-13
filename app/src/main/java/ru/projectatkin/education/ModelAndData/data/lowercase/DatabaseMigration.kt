package ru.projectatkin.education.ModelAndData.data.lowercase

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Profile ADD COLUMN Profile_surname TEXT NOT NULL DEFAULT 'tmp'")
        database.execSQL("ALTER TABLE Profile RENAME COLUMN movie_age_restriction TO Profile_name")
        database.execSQL("ALTER TABLE Profile RENAME COLUMN movie_image_url TO Profile_password")
        database.execSQL("ALTER TABLE Profile RENAME COLUMN movie_genre TO Profile_email")
        database.execSQL("ALTER TABLE Profile RENAME COLUMN movie_date TO Profile_telephone")
    }
}