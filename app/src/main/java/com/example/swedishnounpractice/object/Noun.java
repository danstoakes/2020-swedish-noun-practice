package com.example.swedishnounpractice.object;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class Noun implements DatabaseObject, Parcelable {
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

    public void setWeight (double increment)
    {
        weight += increment;
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
        return "SELECT * FROM Noun WHERE ModuleID = ? ORDER BY Weight DESC LIMIT 4;"; // 10
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

    protected Noun(Parcel in) {
        nounID = in.readInt();
        moduleID = in.readInt();
        referenceID = in.readString();
        english = in.readString();
        swedish = in.readString();
        weight = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nounID);
        dest.writeInt(moduleID);
        dest.writeString(referenceID);
        dest.writeString(english);
        dest.writeString(swedish);
        if (weight == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(weight);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Noun> CREATOR = new Parcelable.Creator<Noun>() {
        @Override
        public Noun createFromParcel(Parcel in) {
            return new Noun(in);
        }

        @Override
        public Noun[] newArray(int size) {
            return new Noun[size];
        }
    };
}