package com.example.swedishnounpractice.object;

import android.content.ContentValues;

public interface DatabaseObject
{
    String getTableName();

    String getSelectString();
    String getUpdateString();

    ContentValues getUpdateValues();
    String[] getUpdateParameters();
}