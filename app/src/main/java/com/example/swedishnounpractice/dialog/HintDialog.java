/* Finalised on 14/06/2020 */

package com.example.swedishnounpractice.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.object.Question;

public class HintDialog extends Dialog implements View.OnClickListener
{
    public HintDialog (@NonNull Context context)
    {
        super(context);

        setOwnerActivity((Activity) context);

        Window window = super.getWindow();

        if (window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.setContentView(R.layout.activity_hint_dialog);
        super.setCancelable(true);

        Button buttonDismiss = findViewById(R.id.buttonDismiss);
        buttonDismiss.setOnClickListener(this);
    }

    public void setAttributes (Question question)
    {
        Context context = getOwnerActivity();

        String english = question.getQuestion();
        String swedish = question.getAnswer();
        if (!question.isToSwedish())
        {
            english = question.getAnswer();
            swedish = question.getQuestion();
        }

        if (context != null)
        {
            TextView englishHint = findViewById(R.id.textEnglish);
            TextView swedishHint = findViewById(R.id.textSwedish);

            englishHint.setText(context.getString(R.string.app_hint_english, english));
            swedishHint.setText(context.getString(R.string.app_hint_swedish, swedish));
        }
    }

    @Override
    public void onClick (View v)
    {
        if (v.getId() == R.id.buttonDismiss)
            dismiss();
    }
}