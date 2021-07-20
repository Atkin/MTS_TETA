import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MovieItemOffsetDecoration(
    var spanCount: Int,
    var spacing: Int,
    var includeEdge: Boolean,
    var headerNum: Int
) : RecyclerView.ItemDecoration() {

    fun GridSpacingItemDecoration(
        spanCount: Int,
        spacing: Int,
        includeEdge: Boolean,
        headerNum: Int
    ) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.headerNum = headerNum;
    }

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

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing
            } else {
                outRect.left =
                    column * spacing / spanCount
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}
