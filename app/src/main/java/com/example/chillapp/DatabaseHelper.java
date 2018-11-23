package com.example.chillapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_SECOND_TEXT = "secondText";
    public static final String COL_ID = "_id";
    public static final String COL_FIRST_TEXT = "firstText";
    public static final String COL_FIRST_TEXT_SIZE = "firstTextSize";
    public static final String COL_SECOND_TEXT_SIZE = "secondTextSize";
    public static final String THEME_NAME = "themeName";
    public static final String MIN_TIME = "min";
    public static final String MAX_TIME = "max";
    private static final String DATABASE_NAME = "chill.db";
    public static final String TABLE_NAME = "chillapp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( _id integer primary key autoincrement, " + COL_FIRST_TEXT + " text, " + COL_SECOND_TEXT + " text, "
                + COL_FIRST_TEXT_SIZE + " integer, " + COL_SECOND_TEXT_SIZE + " integer, " + THEME_NAME + " text, " + MIN_TIME + " integer, "+ MAX_TIME +" integer);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<CustomItem> getAll() {
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);

        List<CustomItem> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++){
                list.add(new CustomItem(cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_FIRST_TEXT)),
                        cursor.getString(cursor.getColumnIndex(COL_SECOND_TEXT)),
                        cursor.getInt(cursor.getColumnIndex(COL_FIRST_TEXT_SIZE)),
                        cursor.getInt(cursor.getColumnIndex(COL_SECOND_TEXT_SIZE)),
                        cursor.getString(cursor.getColumnIndex(THEME_NAME)),
                        cursor.getLong(cursor.getColumnIndex(MIN_TIME)),
                        cursor.getLong(cursor.getColumnIndex(MAX_TIME))));
                if (i<cursor.getCount()-1)
                    cursor.moveToNext();
            }
        }
        cursor.close();

        return list;
    }

    public void save(CustomItem item) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, item.id);
        values.put(COL_FIRST_TEXT, item.primaryText);
        values.put(COL_SECOND_TEXT, item.secondaryText);
        values.put(COL_FIRST_TEXT_SIZE,item.firstTextSize);
        values.put(COL_SECOND_TEXT_SIZE,item.secondTextSize);
        values.put(THEME_NAME, item.theme);
        values.put(MIN_TIME, item.minShowTime);
        values.put(MAX_TIME, item.maxShowTime);
        getWritableDatabase().insertOrThrow(TABLE_NAME, null, values);
    }

    public void update(int id, CustomItem item) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, item.id);
        values.put(COL_FIRST_TEXT, item.primaryText);
        values.put(COL_SECOND_TEXT, item.secondaryText);
        values.put(COL_FIRST_TEXT_SIZE,item.firstTextSize);
        values.put(COL_SECOND_TEXT_SIZE,item.secondTextSize);
        values.put(THEME_NAME, item.theme);
        values.put(MIN_TIME, item.minShowTime);
        values.put(MAX_TIME, item.maxShowTime);
        getWritableDatabase().update(TABLE_NAME, values, "_id=" + id, null);
    }

    public boolean delete(int id) {
        return getWritableDatabase().delete(TABLE_NAME, new StringBuilder().append("_id=").append(id).toString(), null) > 0;
    }

}
