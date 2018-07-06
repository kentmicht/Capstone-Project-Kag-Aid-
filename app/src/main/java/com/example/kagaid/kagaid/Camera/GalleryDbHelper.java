package com.example.kagaid.kagaid.Camera;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GalleryDbHelper extends SQLiteOpenHelper{
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String DATABASE_NAME = "memories.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GalleryContract.MemoryEntry.TABLE_NAME + " (" +
                    GalleryContract.MemoryEntry._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    GalleryContract.MemoryEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    GalleryContract.MemoryEntry.COLUMN_IMAGE + TEXT_TYPE + " )";

    public GalleryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //This method has been intentionally left empty. There is only one version of the database.
    }

    public Cursor readAllMemories() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                GalleryContract.MemoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public boolean addMemory(GalleryImg galleryImg) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GalleryContract.MemoryEntry.COLUMN_TITLE, galleryImg.getTitle());
        values.put(GalleryContract.MemoryEntry.COLUMN_IMAGE, galleryImg.getImageAsString());

        return db.insert(GalleryContract.MemoryEntry.TABLE_NAME, null, values) != -1;
    }
}
