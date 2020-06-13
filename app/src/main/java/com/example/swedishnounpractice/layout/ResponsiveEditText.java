/* Finalised on 13/06/2020 */

package com.example.swedishnounpractice.layout;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

public class ResponsiveEditText extends androidx.appcompat.widget.AppCompatEditText implements View.OnFocusChangeListener
{
    private ResponsiveEditText responsiveEditText;

    private ValidEntryListener listener;

    public ResponsiveEditText (Context context, AttributeSet attrs)
    {
        super (context, attrs);

        responsiveEditText = this;
    }

    public interface ValidEntryListener
    {
        void onValidEntryAdded (boolean enabled);
    }

    public void setValidEntryListener (ValidEntryListener listener)
    {
        this.listener = listener;
    }

    public void setProperties ()
    {
        setText("");
        setEnabled(true);

        setImeOptions(EditorInfo.IME_ACTION_DONE);
        setRawInputType(InputType.TYPE_CLASS_TEXT);
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        setOnFocusChangeListener(this);
        requestFocus();

        addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s)
            {
                listener.onValidEntryAdded (!s.toString().trim().equals(""));
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        setKeyboardVisibility (hasFocus);
    }

    private Runnable showKeyboardRunnable = new Runnable ()
    {
        @Override
        public void run()
        {
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);

            if (manager != null)
                manager.showSoftInput(responsiveEditText, 0);
        }
    };

    private void setKeyboardVisibility (boolean visible)
    {
        if (visible)
        {
            post (showKeyboardRunnable);
        } else
        {
            removeCallbacks(showKeyboardRunnable);

            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);

            if (manager != null)
                manager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }
}