package com.example.swedishnounpractice.helper;

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
        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return -1;
    }
}