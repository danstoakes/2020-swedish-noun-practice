package com.example.swedishnounpractice.helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.swedishnounpractice.R;

public class PreferenceHelper
{
    public static boolean getStandardPreference (Context context, int key, boolean defValue)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String keyString = context.getString(key);

        if (preferences != null)
            return preferences.getBoolean(keyString, defValue);

        return defValue;
    }

    public static boolean getSoundPreference (Context context, int key, boolean defValue)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String keyString = context.getString(key);

        if (preferences != null)
        {
            boolean soundsPreference = preferences.getBoolean(
                    context.getString(R.string.sounds_key), true);

            return soundsPreference && preferences.getBoolean(keyString, defValue);
        }

        return defValue;
    }
}