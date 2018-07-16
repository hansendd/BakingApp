package com.udacity.baking_app.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {
    public static final String AUTHORITY = "com.udacity.baking_app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPES = "recipes";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_RECIPES).build();
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SERVINGS = "servings";
        public static final String COLUMN_NAME_IMAGE = "image";
    }
}
