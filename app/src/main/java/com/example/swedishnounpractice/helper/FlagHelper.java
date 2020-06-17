/* Finalised on 14/06/2020 */

package com.example.swedishnounpractice.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
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
            activity.getWindow().clearFlags(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else
        {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    public static void setRotationLocked (Context context)
    {
        Activity activity = (Activity) context;

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    public static void setRotationUnlocked (Context context)
    {
        Activity activity = (Activity) context;

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }
}