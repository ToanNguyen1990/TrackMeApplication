package net.toannt.hacore.utils.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(var verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun  getItemOffsets(outRect : Rect, view: View,  parent: RecyclerView, state: RecyclerView.State ) {
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1) ?: false) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}