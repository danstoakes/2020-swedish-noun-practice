package com.example.swedishnounpractice.object;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.activity.QuestionActivity;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarFactory implements View.OnClickListener
{
    private Activity activity;

    private Snackbar snackbar;

    private String header;

    private boolean halted;

    public SnackbarFactory (Context context)
    {
        setActivity((Activity) context);

        header = "";
    }

    public void setActivity (Activity activity)
    {
        this.activity = activity;
    }

    public void setHalted (boolean halted)
    {
        this.halted = halted;

        next();
    }

    public void setHeader (String header)
    {
        this.header = header;
    }

    public Activity getActivity ()
    {
        return activity;
    }

    public void make (String text, int length, int background, boolean menu)
    {
        snackbar = Snackbar.make(getActivity().findViewById(R.id.linearLayout), text, length);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(
                ContextCompat.getColor(activity.getApplicationContext(), background));

        if (menu)
        {
            snackbar.setAction("MORE", this);
            snackbar.setActionTextColor(Color.WHITE);
        }

        snackbar.addCallback(new Snackbar.Callback()
        {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event)
            {
                super.onDismissed(transientBottomBar, event);

                if (!halted)
                    next ();
            }
        });
    }

    public void next ()
    {
        QuestionActivity activity = (QuestionActivity) getActivity();
        activity.loadNextQuestion(activity.getQuestionManager().getCurrentQuestion());
    }

    public void show ()
    {
        snackbar.show();
    }

    @Override
    public void onClick (View v)
    {
        halted = true;

        FeedbackDialog dialog = new FeedbackDialog(activity, this);
        dialog.setHeader(header);
        dialog.show();
    }
}
