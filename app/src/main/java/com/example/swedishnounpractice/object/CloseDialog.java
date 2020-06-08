package com.example.swedishnounpractice.object;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.swedishnounpractice.R;

public class CloseDialog extends Dialog implements View.OnClickListener
{
    public CloseDialog(@NonNull Context context)
    {
        super(context);

        setOwnerActivity((Activity) context);

        Window window = super.getWindow();

        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.setContentView(R.layout.activity_close_dialog);
        super.setCancelable(true);

        Button buttonNo = findViewById(R.id.buttonNo);
        Button buttonYes = findViewById(R.id.buttonYes);

        buttonNo.setOnClickListener(this);
        buttonYes.setOnClickListener(this);
    }

    public Activity getActivity ()
    {
        return getOwnerActivity();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonNo:
                dismiss();
                break;
            case R.id.buttonYes:
                dismiss();
                getActivity().finish();
                break;
        }
    }
}