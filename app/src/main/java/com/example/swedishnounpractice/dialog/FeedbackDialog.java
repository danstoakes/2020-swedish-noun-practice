/* Finalised on 17/06/2020 */

package com.example.swedishnounpractice.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.swedishnounpractice.R;
import com.example.swedishnounpractice.helper.ConstantHelper;

public class FeedbackDialog extends Dialog implements View.OnClickListener, Dialog.OnDismissListener
{
    private DialogOptionSelectedListener listener;

    private double weight;

    public FeedbackDialog (@NonNull Context context)
    {
        super (context);

        setOwnerActivity ((Activity) context);

        Window window = super.getWindow ();

        if (window != null)
            window.setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));

        super.setContentView (R.layout.activity_feedback_dialog);
        super.setCancelable (false);

        ImageButton buttonSad = findViewById (R.id.buttonSad);
        ImageButton buttonNeutral = findViewById (R.id.buttonNeutral);
        ImageButton buttonHappy = findViewById (R.id.buttonHappy);

        buttonSad.setOnClickListener (this);
        buttonNeutral.setOnClickListener (this);
        buttonHappy.setOnClickListener (this);

        setOnDismissListener (this);
    }

    public Activity getActivity ()
    {
        return getOwnerActivity();
    }

    public void setHeader (String header)
    {
        TextView textView = findViewById (R.id.textAnswerHeader);
        textView.setText (getActivity().getString(R.string.app_feedback_header, header));
    }

    public void setOnDialogOptionSelectedListener (DialogOptionSelectedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick (View v)
    {
        switch (v.getId ())
        {
            case R.id.buttonSad:
                weight = ConstantHelper.UNHAPPY;
                dismiss ();
                break;
            case R.id.buttonNeutral:
                weight = ConstantHelper.MIXED;
                dismiss ();
                break;
            case R.id.buttonHappy:
                weight = ConstantHelper.HAPPY;
                dismiss ();
                break;
        }
    }

    @Override
    public void onDismiss (DialogInterface dialog)
    {
        listener.onDialogOptionSelected (weight);
    }

    public interface DialogOptionSelectedListener
    {
        void onDialogOptionSelected (double weight);
    }
}