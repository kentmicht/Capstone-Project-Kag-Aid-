package com.example.kagaid.kagaid.Camera;

import android.provider.BaseColumns;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class GalleryContract {
    private GalleryContract() {}

    public static final class MemoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "memories";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
    }
}
