package com.example.swedishnounpractice.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.helper.ConstantHelper;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarFactory implements View.OnClickListener
{
    private Activity activity;

    private boolean dialogShowing;

    private Snackbar snackbar;

    private SnackbarOptionSelectedListener listener;

    private String header;

    private static final int CORRECT_COLOUR = R.color.correctAnswer;
    private static final int INCORRECT_COLOUR = R.color.incorrectAnswer;
    private static final int TEXT_COLOUR = Color.WHITE;

    public SnackbarFactory (Context context)
    {
        activity = (Activity) context;
    }

    public boolean isDialogShowing ()
    {
        return dialogShowing;
    }

    public void setDialogShowing (boolean dialogShowing)
    {
        this.dialogShowing = dialogShowing;
    }

    public Snackbar make (int code, String answerHeader)
    {
        snackbar = Snackbar.make(activity.findViewById(R.id.linearLayout), "", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        switch (code)
        {
            case ConstantHelper.CORRECT_TYPO :
                snackbar.setText(activity.getString(R.string.app_answer_correct_spelling, answerHeader));
                snackbarView.setBackgroundColor(
                        ContextCompat.getColor(activity.getApplicationContext(), CORRECT_COLOUR));
                break;
            case ConstantHelper.CORRECT :
                snackbar.setText(activity.getString(R.string.app_answer_correct));
                snackbarView.setBackgroundColor(
                        ContextCompat.getColor(activity.getApplicationContext(), CORRECT_COLOUR));
                break;
            default :
                snackbar.setText(activity.getString(R.string.app_answer_incorrect));
                snackbarView.setBackgroundColor(
                        ContextCompat.getColor(activity.getApplicationContext(), INCORRECT_COLOUR));
                header = answerHeader;
                snackbar.setAction("MORE", this);
                snackbar.setActionTextColor(TEXT_COLOUR);
                break;
        }
        return snackbar;
    }

    public void setSnackbarOptionSelectedListener (SnackbarOptionSelectedListener listener)
    {
        this.listener = listener;
    }

    public void show ()
    {
        snackbar.show();
    }

    @Override
    public void onClick (View v)
    {
        setDialogShowing (true);

        listener.onSnackbarOptionSelected (header);
    }

    public interface SnackbarOptionSelectedListener
    {
        void onSnackbarOptionSelected (String header);
    }

    /* private Activity activity;

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

    public interface SnackbarOptionSelectedListener
    {
        void onSnackbarOptionSelected (String header);
    }

    private static int getColourID (String description)
    {
        switch (description.toLowerCase())
        {
            case "correct" :
                return R.color.correctAnswer;
            case "incorrect" :
                return R.color.incorrectAnswer;
        }
        return -1;
    } */
}