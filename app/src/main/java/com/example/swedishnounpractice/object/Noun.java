package com.example.swedishnounpractice.object;

import android.content.ContentValues;

public class Noun implements DatabaseObject
{
    private static final String TABLE_NAME = "Noun";

    private int nounID;
    private int moduleID;

    private String referenceID;
    private String english;
    private String swedish;

    private Double weight;

    public Noun (int nounID, int moduleID, String referenceID, String english, String swedish, Double weight)
    {
        this.nounID = nounID;
        this.moduleID = moduleID;
        this.referenceID = referenceID;
        this.english = english;
        this.swedish = swedish;
        this.weight = weight;
    }

    public int getNounID ()
    {
        return nounID;
    }

    public int getModuleID ()
    {
        return moduleID;
    }

    public String getReferenceID()
    {
        return referenceID;
    }

    public String getEnglish()
    {
        return english;
    }

    public String getSwedish()
    {
        return swedish;
    }

    public Double getWeight ()
    {
        return weight;
    }

    @Override
    public String getTableName ()
    {
        return TABLE_NAME;
    }

    @Override
    public String getSelectString ()
    {
        return "SELECT * FROM Noun WHERE ModuleID = ? ORDER BY Weight DESC LIMIT 10;";
    }

    @Override
    public String getUpdateString ()
    {
        return "NounID = ?";
    }

    @Override
    public ContentValues getUpdateValues ()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Weight", weight);

        return contentValues;
    }

    @Override
    public String[] getUpdateParameters ()
    {
        return new String[] {String.valueOf(nounID)};
    }
}