package ru.projectatkin.education

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GenreItemOffsetDecoration(
    var spanCount: Int,
    var spacing: Int,
    var includeEdge: Boolean,
    var headerNum: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var position: Int = parent.getChildAdapterPosition(view) - headerNum

        if (position >= 0) {
            var column: Int = position % spanCount

            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount
                outRect.right =
                    (column + 1) * spacing / spanCount
            } else {
                outRect.left =
                    column * spacing / spanCount
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount
            }
        } else {
            outRect.left = 0;
            outRect.right = 0;
        }
    }
}
