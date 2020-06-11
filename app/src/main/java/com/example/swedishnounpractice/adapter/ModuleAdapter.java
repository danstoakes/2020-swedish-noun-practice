package com.example.swedishnounpractice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.activity.QuestionActivity;
import com.example.swedishnounpractice.object.Module;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleHolder>
{
    private Context context;

    private List<Module> modules;

    public ModuleAdapter(Context context, List<Module> modules)
    {
        this.context = context;
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_module, parent, false);
        return new ModuleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleHolder holder, int position)
    {
        Module module = modules.get(position);
        holder.setAttributes(module);
    }

    @Override
    public int getItemCount()
    {
        return modules.size();
    }

    class ModuleHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView difficulty;

        private ImageView image;

        private Button button;

        public ModuleHolder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.textName);
            difficulty = itemView.findViewById(R.id.textDescription);

            image = itemView.findViewById(R.id.imageIcon);

            button = itemView.findViewById(R.id.buttonStart);
        }

        private void setAttributes (final Module module)
        {
            name.setText(module.getName());
            difficulty.setText(module.getDifficulty());

            try
            { // reused a bit, so maybe a new class?
                int imageID = R.drawable.class.getField(
                        "ic_" + module.getReferenceID()).getInt(null);

                image.setImageResource(imageID);
            } catch (IllegalAccessException | NoSuchFieldException e)
            {
                e.printStackTrace();
            }

            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent questionIntent = new Intent(v.getContext(), QuestionActivity.class)
                            .putExtra("moduleID", module.getModuleID());

                    context.startActivity(questionIntent);
                }
            });
        }
    }
}