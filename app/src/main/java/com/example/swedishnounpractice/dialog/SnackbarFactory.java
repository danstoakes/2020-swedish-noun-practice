package com.example.swedishnounpractice.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.swedishnounpractice.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarFactory implements View.OnClickListener
{
    private Activity activity;

    private Snackbar snackbar;

    private String header;

    private boolean halted;

    private SnackbarOptionSelectedListener listener;

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

        // next();
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
    }

    public Snackbar getSnackbar ()
    {
        return snackbar;
    }

    public boolean getHalted ()
    {
        return halted;
    }

    public void show ()
    {
        snackbar.show();
    }

    public void setSnackbarOptionSelectedListener (SnackbarOptionSelectedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick (View v)
    {
        halted = true;

        listener.onSnackbarOptionSelected (header);
    }
}