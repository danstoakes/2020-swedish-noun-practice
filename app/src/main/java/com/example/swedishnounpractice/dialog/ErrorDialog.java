/* Finalised on 11/06/2020 */

package com.example.swedishnounpractice.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.activity.MainActivity;

public class ErrorDialog extends Dialog implements View.OnClickListener
{
    public ErrorDialog(@NonNull Context context)
    {
        super(context);

        setOwnerActivity((Activity) context);

        Window window = super.getWindow();

        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.setContentView(R.layout.activity_error_dialog);
        super.setCancelable(false);

        Button button = findViewById(R.id.buttonRetry);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        dismiss();

        Intent intent = new Intent(getOwnerActivity(), MainActivity.class);

        if (getOwnerActivity() != null)
            getOwnerActivity().startActivity(intent);
    }
}