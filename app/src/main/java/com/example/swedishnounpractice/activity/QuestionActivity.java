package com.example.swedishnounpractice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.swedishnounpractice.R;

public class QuestionActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // need to write the QuestionActivity class. Get from My Application 2.
        // need to add some nouns to the database file under assets (on left)
        // that can be loaded in using the adapter

        initialise();
    }

    public void initialise ()
    {

    }
}