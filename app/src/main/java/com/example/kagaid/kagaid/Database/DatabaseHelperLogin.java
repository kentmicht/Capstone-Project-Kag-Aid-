package com.example.kagaid.kagaid.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class DatabaseHelperLogin extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "users_table";

    //table columns
    public static final String COL_1 = "users_id";
    public static final String COL_2 = "users_username";
    public static final String COL_3 = "users_password";

    public DatabaseHelperLogin(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //create database and table
        SQLiteDatabase db = this.getWritableDatabase();
    }

    //creates the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        //executes whatever query you pass an argument
        //(ID INTEGER PRIMARYKEY AI, USERNAME TEXT, PASSWORD TEXT)
        db.execSQL("create table " + TABLE_NAME+ " (users_id INTEGER PRIMARY KEY AUTOINCREMENT, users_username TEXT, users_password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
