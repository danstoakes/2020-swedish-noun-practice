/* Finalised on 15/06/2020 */

package com.example.swedishnounpractice.helper;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.swedishnounpractice.R;

public class VibrationHelper
{
    private static final int CORRECT_LENGTH = 150;
    private static final int INCORRECT_LENGTH = 250;

    public static void vibrate (Context context, boolean preferenceDependent, int code)
    {
        boolean canVibrate = false;

        int length = CORRECT_LENGTH;

        if (code == ConstantHelper.INCORRECT)
            length = INCORRECT_LENGTH;

        if (preferenceDependent)
            canVibrate = PreferenceHelper.getStandardPreference (
                    context, R.string.vibration_key, false);

        if (canVibrate || !preferenceDependent)
        {
            Vibrator vibrator = (Vibrator) context.getSystemService (Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                vibrator.vibrate (
                        VibrationEffect.createOneShot (length, VibrationEffect.DEFAULT_AMPLITUDE));
            } else
            {
                vibrator.vibrate (length);
            }
        }
    }
}