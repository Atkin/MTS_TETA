package ru.projectatkin.education.ModelAndData.data.lowercase.Profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profile")
data class Profile(
    @ColumnInfo(name = "Profile_name")
    var name: String?,
    @ColumnInfo(name = "Profile_surname")
    var surname: String?,
    @ColumnInfo(name = "Profile_password")
    var password: String?,
    @ColumnInfo(name = "Profile_email")
    var eMail: String?,
    @ColumnInfo(name = "Profile_telephone")
    var telephone: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int
)