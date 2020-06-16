package com.example.swedishnounpractice.listener;

import android.view.View;

import androidx.annotation.Nullable;

public interface OnAdapterInteractionListener extends OnAdapterEventListener
{
    @Override
    void onAdapterItemClick(View view, int moduleID, @Nullable String text);

    void onSetupError ();

    void onAdapterLoaded (int position);

    int getQuestionNumber ();

    boolean isViewVisible (View view);
}