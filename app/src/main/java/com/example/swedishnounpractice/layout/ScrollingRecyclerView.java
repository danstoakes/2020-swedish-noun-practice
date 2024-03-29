/* Finalised on 11/06/2020 */

package com.example.swedishnounpractice.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollingRecyclerView extends RecyclerView
{
    private int last;

    private ScrollingLayoutManager manager;

    private ScrollCompletedListener listener;

    public ScrollingRecyclerView (Context context, AttributeSet attributeSet)
    {
        super (context, attributeSet);
    }

    public void setScrollCompletedListener (ScrollCompletedListener listener)
    {
        this.listener = listener;
    }

    public interface ScrollCompletedListener
    {
        void onScrollCompleted ();
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout)
    {
        super.setLayoutManager(layout);

        manager = (ScrollingLayoutManager) layout;
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter)
    {
        super.setAdapter(adapter);
    }

    @Override
    public void onScrolled(int dx, int dy)
    {
        super.onScrolled(dx, dy);

        last += dx;
    }

    @Override
    public void onScrollStateChanged (int state)
    {
        super.onScrollStateChanged (state);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        if (state == RecyclerView.SCROLL_STATE_IDLE)
        {
            if ((metrics.widthPixels - last) == 0)
            {
                last = 0;
                manager.setHorizontalScrollEnabled (false);

                listener.onScrollCompleted ();
            } else
            {
                smoothScrollBy (metrics.widthPixels - last, 0);
            }
        }
    }
}