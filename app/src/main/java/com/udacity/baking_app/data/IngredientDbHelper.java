package com.udacity.baking_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IngredientDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ingredients.db";
    private static final int VERSION = 1;

    public IngredientDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =
                "CREATE TABLE " + IngredientContract.IngredientEntry.TABLE_NAME + " (" +
                IngredientContract.IngredientEntry.COLUMN_NAME_RECIPE_ID +   " INTEGER NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_NAME_QUANTITY +    " REAL NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_NAME_MEASUREMENT + " TEXT NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_NAME_INGREDIENT +  " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
