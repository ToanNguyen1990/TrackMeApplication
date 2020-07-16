package net.toannt.hacore.utils.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable divider;
    private boolean isShowDividerLastItem = false;
    private boolean isShowDividerFirstItem = false;
    private int layoutOrientation = -1;
    private int margin = 0;

    public MyDividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    public MyDividerItemDecoration(Context context, int resId) {
        divider = ContextCompat.getDrawable(context, resId);
    }

    public void setDrawable(Drawable drawable) {
        divider = drawable;
    }

    public void setShowDividerLastItem(boolean isShowDividerLastItem) {
        this.isShowDividerLastItem = isShowDividerLastItem;
    }

    public void setShowDividerFirstItem(boolean isShowDividerFirstItem) {
        this.isShowDividerFirstItem = isShowDividerFirstItem;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getLayoutManager() instanceof LinearLayoutManager && layoutOrientation == -1) {
            int layoutOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
            this.layoutOrientation = layoutOrientation;
        }

        if (layoutOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, divider.getIntrinsicHeight(), 0);
        } else {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        if (layoutOrientation == LinearLayoutManager.HORIZONTAL) {
            horizontalDivider(canvas, parent);
        } else {
            verticalDivider(canvas, parent);
        }
    }

    private void horizontalDivider(Canvas canvas, RecyclerView parent) {
        final int dividerTop = parent.getPaddingTop();
        final int dividerBottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        int itemCount = isShowDividerLastItem ? 1 : 2;

        for (int i = 0; i <= childCount - itemCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            divider.setBounds(left, dividerTop + margin, left + divider.getIntrinsicHeight(), dividerBottom - margin);
            divider.draw(canvas);
        }
    }

    private void verticalDivider(Canvas canvas, RecyclerView parent) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        int itemCount = isShowDividerLastItem ? 1 : 2;

        for (int i = 0; i <= childCount - itemCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();

            if (isShowDividerFirstItem && i == 0) {
                int firstItemDividerTop = child.getTop() + params.topMargin;
                int firstItemDividerBottom = firstItemDividerTop + 2;

                divider.setBounds(dividerLeft, firstItemDividerTop, dividerRight, firstItemDividerBottom);
                divider.draw(canvas);
            }

            divider.setBounds(dividerLeft + margin, dividerTop, dividerRight, dividerBottom - margin);
            divider.draw(canvas);
        }
    }
}