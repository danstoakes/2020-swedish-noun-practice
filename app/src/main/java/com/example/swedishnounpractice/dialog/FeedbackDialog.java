package com.example.swedishnounpractice.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.swedishnounpractice.R;

public class FeedbackDialog extends Dialog implements View.OnClickListener, Dialog.OnDismissListener
{
    private SnackbarFactory factory;

    public FeedbackDialog (@NonNull Context context, @Nullable SnackbarFactory factory)
    {
        super(context);

        this.factory = factory;

        setOwnerActivity((Activity) context);

        Window window = super.getWindow();

        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.setContentView(R.layout.activity_feedback_dialog);
        super.setCancelable(false);

        ImageButton buttonSad = findViewById(R.id.buttonSad);
        ImageButton buttonNeutral = findViewById(R.id.buttonNeutral);
        ImageButton buttonHappy = findViewById(R.id.buttonHappy);

        buttonSad.setOnClickListener(this);
        buttonNeutral.setOnClickListener(this);
        buttonHappy.setOnClickListener(this);

        setOnDismissListener(this);
    }

    public Activity getActivity ()
    {
        return getOwnerActivity();
    }

    public void setHeader (String header)
    {
        TextView textView = findViewById(R.id.textAnswerHeader);
        textView.setText(getActivity().getString(R.string.app_feedback_header, header));
    }

    @Override
    public void onClick (View v)
    {
        switch (v.getId())
        {
            case R.id.buttonSad:
                dismiss();
                break;
            case R.id.buttonNeutral:
                dismiss();
                break;
            case R.id.buttonHappy:
                dismiss();
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        if (factory != null)
            factory.setHalted(false);
    }
}