package com.example.chillapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_DATE = "date";
    public static final String COL_ID = "_id";
    public static final String COL_QUANT = "quant";
    private static final String DATABASE_NAME = "quant.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "quantum";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table quantum ( _id integer primary key autoincrement, quant text, date text not null);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor getAll() {
        return getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void save(CustomItem item) {
        ContentValues values = new ContentValues();
        values.put(COL_QUANT, item.primaryText);
        values.put(COL_DATE, item.secondaryText);
        getWritableDatabase().insertOrThrow(TABLE_NAME, null, values);
    }

    public void update(int id, CustomItem item) {
        ContentValues values = new ContentValues();
        values.put(COL_QUANT, item.primaryText);
        values.put(COL_DATE, item.secondaryText);
        getWritableDatabase().update(TABLE_NAME, values, "_id=" + id, null);
    }

    public boolean delete(int id) {
        return getWritableDatabase().delete(TABLE_NAME, new StringBuilder().append("_id=").append(id).toString(), null) > 0;
    }
}
