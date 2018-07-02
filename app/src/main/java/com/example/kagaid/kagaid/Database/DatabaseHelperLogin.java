package com.example.kagaid.kagaid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class DatabaseHelperLogin extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "users_table";

    //table columns
    public static final String COL_1 = "users_id";
    public static final String COL_2 = "users_username";
    public static final String COL_3 = "users_password";

    public String[] COLUMNS = {COL_1,COL_2,COL_3};

    public DatabaseHelperLogin(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (users_id INTEGER PRIMARY KEY AUTOINCREMENT,users_username TEXT,users_password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" +TABLE_NAME); //Drop older table if exists
        onCreate(db);
    }

    public boolean addData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);

        long result = db.insert(TABLE_NAME, null, contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean checkUser(String userName, String password) {
        boolean ret = false;
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT *FROM " + TABLE_NAME + " WHERE " + COL_2 + "=? AND " + COL_3 + "=?", new String[]{userName, password});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                ret = true;
            } else {
                ret = false;
            }
        }

        return ret;
    }

}
