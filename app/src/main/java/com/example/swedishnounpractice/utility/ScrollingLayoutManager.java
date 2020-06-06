package com.example.swedishnounpractice.utility;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ScrollingLayoutManager extends LinearLayoutManager
{
    private boolean horizontalScroll;

    public ScrollingLayoutManager(Context context, int orientation, boolean reverseLayout, boolean horizontalScroll)
    {
        super(context, orientation, reverseLayout);

        this.horizontalScroll = horizontalScroll;
    }

    public void setHorizontalScrollEnabled (boolean horizontalScroll)
    {
        this.horizontalScroll = horizontalScroll;
    }

    @Override
    public boolean canScrollHorizontally()
    {
        return horizontalScroll;
    }
}
