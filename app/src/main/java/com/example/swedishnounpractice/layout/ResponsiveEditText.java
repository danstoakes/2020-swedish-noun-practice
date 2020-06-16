/* Finalised on 15/06/2020 */

package com.example.swedishnounpractice.layout;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

public class ResponsiveEditText extends androidx.appcompat.widget.AppCompatEditText implements View.OnFocusChangeListener
{
    private ValidEntryListener listener;
    private KeyListener keyListener;

    public ResponsiveEditText (Context context, AttributeSet attrs)
    {
        super (context, attrs);

        keyListener = getKeyListener ();
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
        setEnabled (true);
        setKeyListener (keyListener);
        setText ("");
        setImeOptions (EditorInfo.IME_ACTION_DONE);
        setRawInputType (InputType.TYPE_CLASS_TEXT);
        setInputType (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        setOnFocusChangeListener (this);

        requestFocus ();

        addTextChangedListener (new TextWatcher ()
        {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged (Editable s)
            {
                listener.onValidEntryAdded (!s.toString().trim().equals(""));
            }
        });
    }

    @Override
    public void setEnabled (boolean enabled)
    {
        setCursorVisible (enabled);
        setAlpha (enabled ? 1 : (float) 0.75);

        if (!enabled)
            setKeyListener (null);
        // this method normally causes the keyboard to disappear on disable (line 228)
        // https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/widget/TextView.java
    }

    @Override
    public void onFocusChange (View v, boolean hasFocus)
    {
        setKeyboardVisible ();
    }

    private Runnable showKeyboardRunnable = new Runnable ()
    {
        @Override
        public void run()
        {
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);

            if (manager != null)
                manager.showSoftInput(ResponsiveEditText.this, 0);
        }
    };

    private void setKeyboardVisible ()
    {
        post (showKeyboardRunnable);
    }
}