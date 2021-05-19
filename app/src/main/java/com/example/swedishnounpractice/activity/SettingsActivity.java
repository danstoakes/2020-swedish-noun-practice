/* Finalised on 15/06/2020 */

package com.example.swedishnounpractice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.helper.FlagHelper;
import com.example.swedishnounpractice.helper.VibrationHelper;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_settings);

        getSupportFragmentManager ()
                .beginTransaction ()
                .replace (R.id.settings, new SettingsFragment ())
                .commit ();

        ActionBar actionBar = getSupportActionBar ();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled (true);

        FlagHelper.setFlags (this);
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item)
    {
        if (item.getItemId () == android.R.id.home)
        {
            Intent intent = new Intent (SettingsActivity.this, MainActivity.class);
            startActivity (intent);

            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key)
        {
            String vibrateKey = getString(R.string.vibration_key);

            if (key.equals(vibrateKey))
            {
                if (sharedPreferences.getBoolean(key, false))
                    VibrationHelper.vibrate(getActivity(), false, -1);
            }
        }

        @Override
        public void onResume ()
        {
            super.onResume ();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause ()
        {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause ();
        }
    }
}