/* Finalised on 14/06/2020 */

package com.example.swedishnounpractice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.adapter.ModuleAdapter;
import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.object.Module;
import com.example.swedishnounpractice.utility.DatabaseHelper;
import com.example.swedishnounpractice.helper.FlagHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ModuleAdapter.OnAdapterEventListener
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        setTitle (R.string.main_header);

        FlagHelper.setFlags (this);

        initialise ();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater ().inflate(R.menu.action_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item)
    {
        if (item.getItemId () == R.id.action_settings)
        {
            Intent intent = new Intent (MainActivity.this, SettingsActivity.class);
            startActivity (intent);

            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    private void initialise ()
    {
        RecyclerView recyclerView = findViewById (R.id.recyclerView);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));

        ModuleAdapter adapter = new ModuleAdapter (setModules ());
        adapter.setAdapterEventListener (this);
        recyclerView.setAdapter (adapter);
    }

    public List<Module> setModules ()
    {
        List<Module> modules = new ArrayList<> ();

        DatabaseHelper helper = new DatabaseHelper (this.getApplicationContext ());

        Module placeholder = new Module (0, null, null, null);

        for (DatabaseObject object : helper.getList (placeholder))
        {
            Module module = (Module) object;

            double percentage = ((double) helper.getNounCount(module, 1)) / helper.getNounCount(module, 0);
            module.setPercentageComplete((int) Math.round(percentage * 100));

            modules.add (module);
        }
        return modules;
    }

    @Override
    public void onAdapterItemClick(View view, int moduleID, @Nullable String text)
    {
        if (view.getId () == R.id.buttonStart)
        {
            Intent questionIntent = new Intent (MainActivity.this, QuestionActivity.class)
                    .putExtra ("moduleID", moduleID);

            startActivity (questionIntent);
        }
    }
}