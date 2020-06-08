package com.example.swedishnounpractice.utility;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ScrollingLayoutManager extends LinearLayoutManager
{
    private boolean horizontalScroll;

    private int horizontalScrollPosition;

    public ScrollingLayoutManager(Context context, int orientation, boolean reverseLayout, boolean horizontalScroll)
    {
        super(context, orientation, reverseLayout);

        this.horizontalScroll = horizontalScroll;
    }

    @Override
    public boolean canScrollHorizontally()
    {
        return horizontalScroll;
    }

    public void setHorizontalScrollEnabled (boolean horizontalScroll)
    {
        this.horizontalScroll = horizontalScroll;
    }

    public void setHorizontalScrollPosition (int scrollPosition)
    {
        this.horizontalScrollPosition = scrollPosition;
    }

    public int getHorizontalScrollPosition ()
    {
        return horizontalScrollPosition;
    }
}