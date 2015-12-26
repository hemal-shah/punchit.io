package io.punch_it.punchit;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hemal on 12/25/15.
 */

public class SpacesHomeFeed extends RecyclerView.ItemDecoration {
    int mSpace;
    public SpacesHomeFeed(int mSpace){
        this.mSpace = mSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = mSpace;

        outRect.bottom = mSpace;
    }
}
