package com.example.swedishnounpractice.layout;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.swedishnounpractice.R;

public class AnimatedSnackbar implements Animation.AnimationListener, View.OnClickListener
{
    public static final int WAIT_CORRECT = 1500;
    public static final int WAIT_INCORRECT = 2000;
    public static final int DIALOG_INTERIM = 500;

    private View view;

    private Animation moveUp;
    private Animation moveDown;

    private boolean isUp;
    private boolean hasAction;
    private boolean isDialogShowing;

    private String text;
    private int colour;
    private int length;

    private AnimatedSnackbarEventListener listener;

    public AnimatedSnackbar(View view, String text, int colour, int length)
    {
        this.view = view;
        this.text = text;
        this.colour = ContextCompat.getColor(view.getContext(), colour);
        this.length = length;

        moveUp = AnimationUtils.loadAnimation(view.getContext(), R.anim.move_up);
        moveDown = AnimationUtils.loadAnimation(view.getContext(), R.anim.move_down);

        moveUp.setAnimationListener(this);
        moveDown.setAnimationListener(this);

        isUp = false;
        hasAction = false;
    }

    public void setAction(boolean hasAction)
    {
        this.hasAction = hasAction;
    }

    public void setEventListener(AnimatedSnackbarEventListener listener)
    {
        this.listener = listener;
    }

    public void setDialogShowing (boolean isDialogShowing)
    {
        this.isDialogShowing = isDialogShowing;
    }

    public boolean isDialogShowing ()
    {
        return isDialogShowing;
    }

    public void show ()
    {
        if (!isUp)
        {
            isUp = true;

            ((CardView) view).setCardBackgroundColor (colour);

            TextView textView = view.findViewById(R.id.textLabel);
            textView.setText (text);

            if (hasAction)
            {
                Button button = view.findViewById(R.id.buttonShow);
                button.setOnClickListener (this);
                setActionVisibility (View.VISIBLE);
            }

            view.setVisibility (View.VISIBLE);
            view.startAnimation(moveUp);
        }
    }

    private void setActionVisibility (int visible)
    {
        Button button = view.findViewById(R.id.buttonShow);
        button.setVisibility (visible);
    }

    public void moveDown (int length)
    {
        isUp = false;

        view.postDelayed(new Runnable ()
        {
            @Override
            public void run ()
            {
                if (!isDialogShowing)
                    view.startAnimation (moveDown);
            }
        }, length);
    }

    @Override
    public void onAnimationStart (Animation animation) { }

    @Override
    public void onAnimationEnd (Animation animation)
    {
        if (isUp)
        {
            moveDown (length);
        } else
        {
            setActionVisibility (View.INVISIBLE);
            view.setVisibility (View.GONE);

            listener.onDismiss (this);
        }
    }

    @Override
    public void onAnimationRepeat (Animation animation) { }

    @Override
    public void onClick(View v)
    {
        listener.onActionItemClick (this);
    }

    public interface AnimatedSnackbarEventListener
    {
        void onDismiss (AnimatedSnackbar animatedSnackbar);

        void onActionItemClick (AnimatedSnackbar animatedSnackbar);
    }
}