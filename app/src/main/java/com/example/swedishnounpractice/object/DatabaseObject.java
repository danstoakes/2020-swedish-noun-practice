package com.example.swedishnounpractice.object;

import android.content.ContentValues;

public interface DatabaseObject
{
    public String getTableName ();

    public String getSelectString ();
    public String getUpdateString ();

    public ContentValues getUpdateValues ();
    public String[] getUpdateParameters ();
}