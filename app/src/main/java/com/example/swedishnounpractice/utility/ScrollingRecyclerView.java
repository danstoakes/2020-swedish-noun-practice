package com.example.swedishnounpractice.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.activity.QuestionActivity;
import com.example.swedishnounpractice.object.Question;

public class ScrollingRecyclerView extends RecyclerView
{
    private int last;

    private DisplayMetrics metrics;

    private ScrollingLayoutManager manager;

    private Question question;

    public ScrollingRecyclerView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        metrics = context.getResources().getDisplayMetrics();
    }

    public void setQuestion (Question question)
    {
        this.question = question;
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

                QuestionActivity activity = ((QuestionActivity) getContext());

                ((QuestionActivity) getContext()).playSound(question, null);
            } else
            {
                smoothScrollBy(metrics.widthPixels - last, 0);
            }
        }
    }
}