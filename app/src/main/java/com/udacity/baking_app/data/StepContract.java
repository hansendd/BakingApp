package com.udacity.baking_app.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class StepContract {
    public static final String AUTHORITY = "com.udacity.baking_app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_STEPS = "steps";

    public static final class StepEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_STEPS).build();
        public static final String TABLE_NAME = "steps";
        public static final String COLUMN_NAME_RECIPE_ID = "recipe_id";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_VIDEO_URL = "video_url";
        public static final String COLUMN_NAME_THUMBNAIL_URL = "thumbnail_url";
    }
}
