package com.example.swedishnounpractice.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.adapter.ModuleAdapter;
import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.object.Module;
import com.example.swedishnounpractice.utility.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
    }

    private void initialise ()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Module> modules = new ArrayList<>();

        ModuleAdapter adapter = new ModuleAdapter(this, modules);
        recyclerView.setAdapter(adapter);

        DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());

        Module placeholder = new Module (0, null, null, null);
        for (DatabaseObject object : helper.getList(placeholder))
            modules.add(((Module) object));

        adapter.notifyDataSetChanged();
    }
}