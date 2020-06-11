package com.example.swedishnounpractice.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.swedishnounpractice.R;

public class PermissionHelper
{
    private boolean hint;
    private boolean vibrate;

    private boolean sounds;
    private boolean wordSound;
    private boolean correctSound;
    private boolean errorSound;

    public PermissionHelper (Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences != null)
        {
            hint = preferences.getBoolean(context.getString(R.string.hint_key), false);
            vibrate = preferences.getBoolean(context.getString(R.string.vibration_key), false);

            sounds = preferences.getBoolean(context.getString(R.string.sounds_key), true);
            wordSound = preferences.getBoolean(context.getString(R.string.word_sounds_key), true);
            correctSound = preferences.getBoolean(context.getString(R.string.correct_sounds_key), true);
            errorSound = preferences.getBoolean(context.getString(R.string.error_sounds_key), true);
        }
    }

    public boolean getHintsOn ()
    {
        return hint;
    }

    public boolean getVibrationsOn ()
    {
        return vibrate;
    }

    public boolean getSoundsOn ()
    {
        return sounds;
    }

    public boolean getWordSoundsOn ()
    {
        return getSoundsOn() && wordSound;
    }

    public boolean getCorrectSoundsOn ()
    {
        return getSoundsOn() && correctSound;
    }

    public boolean getErrorSoundsOn ()
    {
        return getSoundsOn() && errorSound;
    }
}