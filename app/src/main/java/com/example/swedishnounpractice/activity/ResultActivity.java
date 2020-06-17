/* Finalised on 05/06/2020 */

package com.example.swedishnounpractice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.helper.FlagHelper;

public class ResultActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoBar);
        setContentView(R.layout.activity_result);

        FlagHelper.setFlags(this);

        int score = 0;
        int total = 0;

        if (getIntent().getIntExtra("score", 0) != 0)
            score = getIntent().getIntExtra("score", 0);

        if (getIntent().getIntExtra("total", 0) != 0)
            total = getIntent().getIntExtra("total", 0);

        TextView textView = findViewById(R.id.textScore);
        textView.setText(getString(R.string.app_result_message, score, total));

        Button button = findViewById(R.id.buttonContinue);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent returnIntent = new Intent (ResultActivity.this, MainActivity.class);

                startActivity(returnIntent);
            }
        });
    }
}