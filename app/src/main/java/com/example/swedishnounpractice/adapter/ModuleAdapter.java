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

import java.util.Arrays;
import java.util.List;

/**
 * Adapter class which allows for the representation and interaction of the various module
 * items within an AdapterView.
 */
public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleHolder>
{
    private OnAdapterEventListener listener;

    private final List<Module> modules;

    /**
     * Constructor for the ModuleAdapter class.
     * @param modules - The list of modules to be loaded.
     */
    public ModuleAdapter (List<Module> modules)
    {
        this.modules = modules;
    }

    /**
     * Loads the UI element for the module to be displayed in the activity.
     * @param parent - The parent ViewGroup of the UI element.
     * @param viewType - The item view type.
     * @return ModuleHolder - The ModuleHolder which holds all the information for a module.
     */
    @NonNull
    @Override
    public ModuleHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from (
                parent.getContext()).inflate(R.layout.item_module, parent, false);
        // create a new UI view for the module and return it
        return new ModuleHolder(view);
    }

    /**
     *
     * @param holder - The ModuleHolder which holds all the information for a module.
     * @param position - The position of the ModuleHolder.
     */
    @Override
    public void onBindViewHolder (@NonNull ModuleHolder holder, int position)
    {
        // retrieve the module and set its attributes
        Module module = modules.get (position);
        holder.setAttributes (module);
    }

    /**
     * Returns the number of modules.
     * @return int - the size of the module.
     */
    @Override
    public int getItemCount ()
    {
        return modules.size();
    }

    /**
     * Sets the EventListener for the adapter.
     * @param listener - The adapter event listener.
     */
    public void setAdapterEventListener (OnAdapterEventListener listener)
    {
        this.listener = listener;
    }

    /**
     * Interface for the Adapter which handles events.
     */
    public interface OnAdapterEventListener
    {
        void onAdapterItemClick (View view, int moduleID, @Nullable String text);
    }

    /**
     * Holder class which holds all of the information required for displaying and interacting
     * with the various modules.
     */
    class ModuleHolder extends RecyclerView.ViewHolder
    {
        private final ProgressBar bar;
        private final TextView name;
        private final TextView difficulty;
        private final ImageView image;
        private final Button button;

        /**
         * Constructor for the ModuleHolder class, whereby the UI elements are assigned.
         * @param itemView - The current view within the RecyclerView.
         */
        public ModuleHolder (@NonNull View itemView)
        {
            super(itemView);

            bar = itemView.findViewById (R.id.progressBar);
            name = itemView.findViewById (R.id.textName);
            difficulty = itemView.findViewById (R.id.textDescription);
            image = itemView.findViewById (R.id.imageIcon);
            button = itemView.findViewById (R.id.buttonStart);
        }

        /**
         * Set the attributes for the module.
         * @param module - The module to set the attributes for.
         */
        private void setAttributes (final Module module)
        {
            // the modules which currently have no display image
            List<String> noImage = Arrays.asList("module_11", "module_12", "module_13", "module_14");
            // display the progress bar, module name, and module difficulty
            bar.setProgress (module.getPercentageComplete ());
            name.setText (module.getName ());
            difficulty.setText (module.getDifficulty ());
            // default to an image placeholder
            int imageID = DrawableHelper.getResource ("ic_module_no_image", true);
            // if the module is not in the image-less list, retrieve its image
            if (!noImage.contains(module.getReferenceID()))
                imageID = DrawableHelper.getResource (
                        "ic_" + module.getReferenceID (), true);
            // set the image if it exists
            if (imageID != -1)
                image.setImageResource (imageID);
            // set the click listener for the Adapter
            button.setOnClickListener (new View.OnClickListener ()
            {
                /**
                 * Click handler method for the view.
                 * @param v - The view.
                 */
                @Override
                public void onClick(View v)
                {
                    // trigger the Adapter listener and pass in the view and moduleID
                    listener.onAdapterItemClick (v, module.getModuleID (), null);
                }
            });
        }
    }
}