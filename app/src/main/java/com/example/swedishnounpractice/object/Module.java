package com.example.swedishnounpractice.object;

import android.content.ContentValues;

public class Module implements DatabaseObject
{
    private static final String TABLE_NAME = "Module";

    private int moduleID;

    private String referenceID;
    private String name;
    private String difficulty;

    public Module (int moduleID, String referenceID, String name, String difficulty)
    {
        this.moduleID = moduleID;
        this.referenceID = referenceID;
        this.name = name;
        this.difficulty = difficulty;
    }

    public int getModuleID ()
    {
        return moduleID;
    }

    public String getReferenceID ()
    {
        return referenceID;
    }

    public String getName ()
    {
        return name;
    }

    public String getDifficulty ()
    {
        return difficulty;
    }

    @Override
    public String getTableName ()
    {
        return TABLE_NAME;
    }

    @Override
    public String getSelectString ()
    {
        return "SELECT * FROM Module;";
    }

    @Override
    public String getUpdateString ()
    {
        return "ModuleID = ?";
    }

    @Override
    public ContentValues getUpdateValues ()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Difficulty", difficulty);

        return contentValues;
    }

    @Override
    public String[] getUpdateParameters ()
    {
        return new String[] {String.valueOf(moduleID)};
    }
}