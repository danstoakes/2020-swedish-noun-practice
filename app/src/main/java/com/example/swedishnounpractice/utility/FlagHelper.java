package com.example.swedishnounpractice.utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

public class FlagHelper
{
    public static void setFlags (Context context)
    {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        Activity activity = (Activity) context;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
        {
            activity.getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else
        {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}