package com.udacity.baking_app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.baking_app.R;
import com.udacity.baking_app.model.Ingredient;

public class DbContentProvider extends ContentProvider {
    public static final int RECIPES = 100;
    public static final int RECIPES_WITH_ID = 101;
    public static final int INGREDIENTS = 200;
    public static final int INGREDIENTS_WITH_ID = 201;
    public static final int STEPS = 300;
    public static final int STEPS_WITH_ID = 301;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private RecipeDbHelper recipeDbHelper;
    private IngredientDbHelper ingredientDbHelper;
    private StepDbHelper stepDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES + "/#", RECIPES_WITH_ID);
        uriMatcher.addURI(IngredientContract.AUTHORITY, IngredientContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(IngredientContract.AUTHORITY, IngredientContract.PATH_INGREDIENTS + "/#", INGREDIENTS_WITH_ID);
        uriMatcher.addURI(StepContract.AUTHORITY, StepContract.PATH_STEPS, STEPS);
        uriMatcher.addURI(StepContract.AUTHORITY, StepContract.PATH_STEPS + "/#", STEPS_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        recipeDbHelper = new RecipeDbHelper(context);
        ingredientDbHelper = new IngredientDbHelper(context);
        stepDbHelper = new StepDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase sqLiteDatabase;
        int match = uriMatcher.match(uri);
        String tableName = "";

        switch(match) {
            case RECIPES:
                sqLiteDatabase = recipeDbHelper.getWritableDatabase();
                tableName = RecipeContract.RecipeEntry.TABLE_NAME;
                break;
            case INGREDIENTS:
                sqLiteDatabase = ingredientDbHelper.getWritableDatabase();
                tableName = IngredientContract.IngredientEntry.TABLE_NAME;
                break;
            case STEPS:
                sqLiteDatabase = stepDbHelper.getWritableDatabase();
                tableName = StepContract.StepEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException(getContext().getResources().getString(R.string.error_unknown_uri) + uri);
        }

        Cursor retCursor = null;
        switch (match) {
            case RECIPES:
                retCursor =  sqLiteDatabase.query(tableName,
                                                  projection,
                                                  selection,
                                                  selectionArgs,
                                                  null,
                                                  null,
                                                  sortOrder);
                break;
            case RECIPES_WITH_ID:
                retCursor =  sqLiteDatabase.query(tableName,
                                                  projection,
                                                  selection,
                                                  selectionArgs,
                                                  null,
                                                  null,
                                                  sortOrder);
                break;
            case INGREDIENTS:
                retCursor =  sqLiteDatabase.query(tableName,
                                                 projection,
                                                 selection,
                                                 selectionArgs,
                                                 null,
                                                 null,
                                                 sortOrder);
                break;
            case INGREDIENTS_WITH_ID:
                retCursor =  sqLiteDatabase.query(tableName,
                                                  projection,
                                                  selection,
                                                  selectionArgs,
                                                  null,
                                                  null,
                                                  sortOrder);
                break;
            case STEPS:
                retCursor =  sqLiteDatabase.query(tableName,
                                                  projection,
                                                  selection,
                                                  selectionArgs,
                                                  null,
                                                  null,
                                                  sortOrder);
                break;
            case STEPS_WITH_ID:
                retCursor =  sqLiteDatabase.query(tableName,
                                                  projection,
                                                  selection,
                                                  selectionArgs,
                                                  null,
                                                  null,
                                                  sortOrder);
                break;
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException(getContext().getResources().getString(R.string.error_not_yet_implemented));
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase sqLiteDatabase;
        int match = uriMatcher.match(uri);

        switch(match) {
            case RECIPES:
                sqLiteDatabase = recipeDbHelper.getWritableDatabase();
                break;
            case INGREDIENTS:
                sqLiteDatabase = ingredientDbHelper.getWritableDatabase();
                break;
            case STEPS:
                sqLiteDatabase = stepDbHelper.getWritableDatabase();
                break;
            default:
                throw new UnsupportedOperationException(getContext().getResources().getString(R.string.error_unknown_uri) + uri);
        }

        Uri returnUri = null;
        switch(match) {
            case RECIPES:
                long id = sqLiteDatabase.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, id);
                }
                else {
                    throw new SQLException(getContext().getResources().getString(R.string.error_insert_row_failed) + uri);
                }
                break;
            case INGREDIENTS:
                id = sqLiteDatabase.insert(IngredientContract.IngredientEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, id);
                }
                else {
                    throw new SQLException(getContext().getResources().getString(R.string.error_insert_row_failed) + uri);
                }
                break;
            case STEPS:
                id = sqLiteDatabase.insert(StepContract.StepEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(StepContract.StepEntry.CONTENT_URI, id);
                }
                else {
                    throw new SQLException(getContext().getResources().getString(R.string.error_insert_row_failed) + uri);
                }
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException(getContext().getResources().getString(R.string.error_not_yet_implemented));
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException(getContext().getResources().getString(R.string.error_not_yet_implemented));
    }
}
