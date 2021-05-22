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

/**
 * The main activity class for the application. Handles the creation of the RecyclerView which
 * houses the different modules, and the adapter which loads them in. It also handles the UI
 * interactions, i.e., starting a module lesson.
 */
public class MainActivity extends AppCompatActivity implements ModuleAdapter.OnAdapterEventListener
{
    /**
     * Calls and creates the activity when the application is run.
     * @param savedInstanceState - Bundle object which represents a potential previous save state.
     */
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        // load any previous saved state and set the layout
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        setTitle (R.string.main_header);
        // set the light/dark mode flags
        FlagHelper.setFlags (this);
        // start loading the activity components
        initialise ();
    }

    /**
     * Loads the options menu for the activity.
     * @param menu - The menu object.
     * @return true - is always returned after the menu is loaded.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // inflate the menu and return true
        getMenuInflater ().inflate(R.menu.action_settings, menu);
        return true;
    }

    /**
     * Handles the selection of the available menu options.
     * @param item - The selected menu item.
     * @return boolean - True if the settings menu item is selected, otherwise returns false.
     */
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item)
    {
        if (item.getItemId () == R.id.action_settings)
        {
            // start the Settings activity and return true
            Intent intent = new Intent (MainActivity.this, SettingsActivity.class);
            startActivity (intent);

            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    /**
     * Sets up the RecyclerView which houses all of the Swedish modules and the adapter which loads
     * the respective modules into the RecyclerView to be interacted with.
     */
    private void initialise ()
    {
        // create a new RecyclerView and set it up with a LinearLayoutManager
        RecyclerView recyclerView = findViewById (R.id.recyclerView);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));
        // create a ModuleAdapter for loading modules into the RecyclerView
        ModuleAdapter adapter = new ModuleAdapter (setModules ());
        adapter.setAdapterEventListener (this);
        recyclerView.setAdapter (adapter);
    }

    /**
     * Retrieves the modules from the database and returns a list of them.
     * @return modules - A list of the modules which can be interacted with in the activity.
     */
    public List<Module> setModules ()
    {
        List<Module> modules = new ArrayList<> ();
        // create a new DatabaseHelper object
        DatabaseHelper helper = new DatabaseHelper (this.getApplicationContext ());
        // create an empty Module to allow for the module list to be retrieved
        Module placeholder = new Module (0, null, null, null);
        // loop through the list of modules
        for (DatabaseObject object : helper.getList (placeholder))
        {
            // create a new module using the DatabaseObject retrieved from the list
            Module module = (Module) object;
            // calculate the percentage of the module which has been completed by the user
            double percentage = (
                    (double) helper.getNounCount(module, 1)) / helper.getNounCount(module, 0);
            module.setPercentageComplete((int) Math.round(percentage * 100));
            // add the module to the list to be returned
            modules.add (module);
        }
        return modules;
    }

    /**
     * Handles the selection of the available Adapter options.
     * @param view - The view which is being interacted with.
     * @param moduleID - The ID of the module which is being interacted with/used.
     * @param text - TBR (serves no purpose)
     */
    @Override
    public void onAdapterItemClick(View view, int moduleID, @Nullable String text)
    {
        // if the element being pressed is the start button
        if (view.getId () == R.id.buttonStart)
        {
            // create a new QuestionIntent using the current module ID
            Intent questionIntent = new Intent (MainActivity.this, QuestionActivity.class)
                    .putExtra ("moduleID", moduleID);
            // start the activity
            startActivity (questionIntent);
        }
    }
}