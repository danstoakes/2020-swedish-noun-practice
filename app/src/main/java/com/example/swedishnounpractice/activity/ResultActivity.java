package com.example.swedishnounpractice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.helper.FlagHelper;

public class ResultActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        FlagHelper.setFlags(this);
    }
}