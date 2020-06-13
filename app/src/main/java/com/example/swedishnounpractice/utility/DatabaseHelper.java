/* Finalised on 05/06/2020 */

package com.example.swedishnounpractice.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.swedishnounpractice.object.DatabaseObject;
import com.example.swedishnounpractice.object.Module;
import com.example.swedishnounpractice.object.Noun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private Context context;

    private static final String DATABASE_NAME = "learning_swedish";
    private static final int DATABASE_VERSION = 1;

    private String databasePath;

    public DatabaseHelper(Context context)
    {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;

        databasePath = context.getDatabasePath (DATABASE_NAME).toString ();

        initialise ();
    }

    private void initialise ()
    {
        if (!databaseExists ())
            copyDatabase ();
    }

    @Override
    public void onCreate (SQLiteDatabase db)
    {
        db.execSQL ("DROP TABLE IF EXISTS android_metadata");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) { }

    public boolean databaseExists ()
    {
        File databaseFile = new File (databasePath);

        return databaseFile.exists ();
    }

    public void copyDatabase ()
    {
        try
        {
            InputStream inputStream = context.getAssets().open (DATABASE_NAME + ".db");
            OutputStream outputStream = new FileOutputStream (databasePath);

            byte [] buffer = new byte [1024];

            int length;
            while ((length = inputStream.read(buffer)) > 0)
                outputStream.write(buffer, 0, length);

            outputStream.flush ();
            outputStream.close ();
            inputStream.close ();
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    public List<DatabaseObject> getList (DatabaseObject object)
    {
        List<DatabaseObject> toReturn = new ArrayList<> ();

        String query = object.getSelectString ();

        SQLiteDatabase database = this.getReadableDatabase ();

        String [] parameters = new String [1];
        if (object instanceof Noun)
            parameters [0] = String.valueOf(((Noun) object).getModuleID ());

        Cursor cursor = database.rawQuery (query, parameters [0] != null ? parameters : null);

        if (cursor.moveToFirst ())
        {
            do
            {
                if (object instanceof Module)
                {
                    toReturn.add(new Module (
                            cursor.getInt (0), cursor.getString (1),
                            cursor.getString (2), cursor.getString (3)));
                } else
                {
                    toReturn.add(new Noun (
                            cursor.getInt (0), cursor.getInt (1),
                            cursor.getString (2), cursor.getString (3),
                            cursor.getString (4), cursor.getDouble (5)));
                }
            } while (cursor.moveToNext ());
        }
        cursor.close ();
        database.close ();

        return toReturn;
    }

    public double getModuleWeight (Module module)
    {
        String query = "SELECT AVG(Weight) AS Average FROM Noun WHERE ModuleID = ?";

        SQLiteDatabase database = this.getReadableDatabase ();

        Cursor cursor = database.rawQuery(query, new String [] {String.valueOf(module.getModuleID ())});

        if (cursor.moveToFirst ())
            return cursor.getDouble (0);

        cursor.close ();

        return 0;
    }

    public int getNounCount (Module module, int range)
    {
        String query = "SELECT COUNT(NounID) FROM Noun WHERE ModuleID = ? AND Seen >= ?";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                query, new String [] {String.valueOf(module.getModuleID()), String.valueOf(range)});

        if (cursor.moveToFirst())
            return cursor.getInt (0);

        cursor.close();

        return 0;
    }

    public boolean update (DatabaseObject object)
    {
        SQLiteDatabase database = this.getWritableDatabase ();
        int success = database.update(
                object.getTableName (), object.getUpdateValues (), object.getUpdateString (),
                object.getUpdateParameters ());

        database.close ();

        return success == 1;
    }
}