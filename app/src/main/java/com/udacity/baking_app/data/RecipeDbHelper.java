package com.udacity.baking_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =
                "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry.COLUMN_NAME_ID +       " INTEGER PRIMARY KEY, " +
                RecipeContract.RecipeEntry.COLUMN_NAME_NAME +     " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_NAME_IMAGE +    " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_NAME_SERVINGS + " INTEGER NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
