package com.example.swedishnounpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.adapter.ModuleAdapter;
import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.object.Module;
import com.example.swedishnounpractice.utility.DatabaseHelper;
import com.example.swedishnounpractice.utility.FlagHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlagHelper.setFlags(this);

        initialise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.action_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.action_settings)
        {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialise ()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ModuleAdapter adapter = new ModuleAdapter(this, setModules ());
        recyclerView.setAdapter(adapter);
    }

    public List<Module> setModules ()
    {
        List<Module> modules = new ArrayList<>();

        DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());

        Module placeholder = new Module (0, null, null, null);
        for (DatabaseObject object : helper.getList(placeholder))
            modules.add(((Module) object));

        return modules;
    }
}