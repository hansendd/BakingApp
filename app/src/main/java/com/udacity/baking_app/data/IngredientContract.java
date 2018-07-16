package com.udacity.baking_app.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientContract {
    public static final String AUTHORITY = "com.udacity.baking_app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_INGREDIENTS = "ingredients";

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_INGREDIENTS).build();
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_NAME_RECIPE_ID = "recipe_id";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_MEASUREMENT = "measurement";
        public static final String COLUMN_NAME_INGREDIENT = "ingredient";
    }
}
