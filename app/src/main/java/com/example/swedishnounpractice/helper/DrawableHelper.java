/* Finalised on 11/06/2020 */

package com.example.swedishnounpractice.helper;

import android.content.res.Resources;

import com.example.swedishnounpractice.R;

public class DrawableHelper
{
    public static int getResource (String stringID, boolean image)
    {
        try
        {
            if (image)
                return R.drawable.class.getField(stringID).getInt(null);

            return R.raw.class.getField(stringID).getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException | Resources.NotFoundException e)
        {
            e.printStackTrace ();
        }

        return -1;
    }
}