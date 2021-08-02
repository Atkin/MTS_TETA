package ru.projectatkin.education

import androidx.recyclerview.widget.DiffUtil
import ru.projectatkin.education.ModelAndData.data.dto.MovieGenre

class GenresCallback(private val oldList: List<MovieGenre>, private val newList: List<MovieGenre>): DiffUtil.Callback() {

    override fun getOldListSize(): Int =oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}