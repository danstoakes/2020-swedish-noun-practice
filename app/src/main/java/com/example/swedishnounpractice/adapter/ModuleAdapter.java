/* Finalised on 14/06/2020 */

package com.example.swedishnounpractice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.helper.DrawableHelper;
import com.example.swedishnounpractice.object.Module;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleHolder>
{
    private OnAdapterEventListener listener;

    private final List<Module> modules;

    public ModuleAdapter (List<Module> modules)
    {
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from (
                parent.getContext()).inflate(R.layout.item_module, parent, false);

        return new ModuleHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull ModuleHolder holder, int position)
    {
        Module module = modules.get (position);
        holder.setAttributes (module);
    }

    @Override
    public int getItemCount ()
    {
        return modules.size();
    }

    public void setAdapterEventListener (OnAdapterEventListener listener)
    {
        this.listener = listener;
    }

    public interface OnAdapterEventListener
    {
        void onAdapterItemClick (View view, int moduleID, @Nullable String text);
    }

    class ModuleHolder extends RecyclerView.ViewHolder
    {
        private final ProgressBar bar;
        private final TextView name;
        private final TextView difficulty;
        private final ImageView image;
        private final Button button;

        public ModuleHolder (@NonNull View itemView)
        {
            super(itemView);

            bar = itemView.findViewById (R.id.progressBar);
            name = itemView.findViewById (R.id.textName);
            difficulty = itemView.findViewById (R.id.textDescription);
            image = itemView.findViewById (R.id.imageIcon);
            button = itemView.findViewById (R.id.buttonStart);
        }

        private void setAttributes (final Module module)
        {
            bar.setProgress (module.getPercentageComplete ());
            name.setText (module.getName ());
            difficulty.setText (module.getDifficulty ());

            int imageID = DrawableHelper.getResource ("ic_" + module.getReferenceID(), true);

            if (imageID != -1)
                image.setImageResource (imageID);

            button.setOnClickListener (new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onAdapterItemClick (v, module.getModuleID(), null);
                }
            });
        }
    }
}