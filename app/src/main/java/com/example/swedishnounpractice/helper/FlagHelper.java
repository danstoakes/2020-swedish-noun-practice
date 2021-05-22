/* Finalised on 14/06/2020 */

package com.example.swedishnounpractice.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;

/**
 * Handles any actions which require flags to be set, such as light/dark mode and orientation.
 */
public class FlagHelper
{
    /**
     * Sets the correct flags based on the system preferences for light/dark mode.
     * @param context - The application context.
     */
    public static void setFlags (Context context)
    {
        // get the current UI mode being used by the mobile device
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        Activity activity = (Activity) context;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
        {
            // clear the light-coloured status bar for dark mode users
            activity.getWindow().clearFlags (
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else
        {
            // set the light-coloured status bar for light mode users
            activity.getWindow().getDecorView().setSystemUiVisibility (
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    /**
     * Sets the orientation flags to be locked, thus preventing rotation.
     * @param context - The application context.
     */
    public static void setRotationLocked (Context context)
    {
        Activity activity = (Activity) context;
        // set the locked flag
        activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    /**
     * Sets the orientation flags to be unlocked, thus allowing for free rotation.
     * @param context - The application context.
     */
    public static void setRotationUnlocked (Context context)
    {
        Activity activity = (Activity) context;
        // set the free orientation flag
        activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER);
    }
}