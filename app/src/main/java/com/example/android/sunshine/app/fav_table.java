package com.example.android.sunshine.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MIDO on 28/01/2016.
 */
public class fav_table extends SQLiteOpenHelper {

    public fav_table(Context applicationcontext)
    { super(applicationcontext, "androidsqlite.db", null, 1);}

    @Override public void onCreate(SQLiteDatabase database)
    { String query;
        query = "CREATE TABLE Favourits ( movie_id INTEGER PRIMARY KEY  )";
        database.execSQL(query);  }


    @Override public void onUpgrade(SQLiteDatabase database, int version_old, int current_version)
    { String query; query = "DROP TABLE IF EXISTS Favourits";
        database.execSQL(query);
        onCreate(database); }

    public void insert_fav(int movie_id)

    {

        ContentValues values=new ContentValues(1);

        values.put("movie_id", movie_id);


        getWritableDatabase().insert("Favourits", "movie_id", values);

    }


    public int[]  getData(){

        String Table_Name="Favourits";

        String selectQuery = "SELECT  * FROM " + Table_Name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int[] data = new int[cursor.getCount()];
        int i =0;
        if (cursor.moveToFirst()) {
            do {

                int id=cursor.getInt(0);
                data[i]=id;
                i++;
            } while (cursor.moveToNext());
        }
        db.close();
        return data;
    }


    public void delete(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("delete from  Favourits  where movie_id='" + id + "'");
    }

}
