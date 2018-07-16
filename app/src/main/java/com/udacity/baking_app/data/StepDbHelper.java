package com.udacity.baking_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StepDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "steps.db";
    private static final int VERSION = 1;

    public StepDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =
                "CREATE TABLE " + StepContract.StepEntry.TABLE_NAME + " (" +
                StepContract.StepEntry.COLUMN_NAME_RECIPE_ID +         " INTEGER NOT NULL, " +
                StepContract.StepEntry.COLUMN_NAME_ID +                " INTEGER NOT NULL, " +
                StepContract.StepEntry.COLUMN_NAME_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                StepContract.StepEntry.COLUMN_NAME_DESCRIPTION +       " TEXT NOT NULL," +
                StepContract.StepEntry.COLUMN_NAME_VIDEO_URL +         " TEXT, " +
                StepContract.StepEntry.COLUMN_NAME_THUMBNAIL_URL +     " TEXT" +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
