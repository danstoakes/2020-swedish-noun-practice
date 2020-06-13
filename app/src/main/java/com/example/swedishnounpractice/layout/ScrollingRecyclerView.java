package com.example.swedishnounpractice.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollingRecyclerView extends RecyclerView
{
    private int last;

    private DisplayMetrics metrics;

    private ScrollingLayoutManager manager;

    private ScrollCompletedListener listener;

    public ScrollingRecyclerView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        metrics = context.getResources().getDisplayMetrics();
    }

    public void setScrollCompletedListener (ScrollCompletedListener listener)
    {
        this.listener = listener;
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
    public void onScrollStateChanged(int state)
    {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE)
        {
            if ((metrics.widthPixels - last) == 0)
            {
                last = 0;
                manager.setHorizontalScrollEnabled(false);

                listener.onScrollCompleted();
            } else
            {
                smoothScrollBy(metrics.widthPixels - last, 0);
            }
        }
    }
}