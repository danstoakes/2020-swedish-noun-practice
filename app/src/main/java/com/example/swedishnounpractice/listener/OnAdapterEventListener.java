package com.example.swedishnounpractice.listener;

import android.view.View;

import androidx.annotation.Nullable;

public interface OnAdapterEventListener
{
    void onAdapterItemClick (View view, int moduleID, @Nullable String text);
}