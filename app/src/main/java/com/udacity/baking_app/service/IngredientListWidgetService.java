package com.udacity.baking_app.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.baking_app.R;
import com.udacity.baking_app.data.IngredientContract;
import com.udacity.baking_app.model.Ingredient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IngredientListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewsFactory(this.getApplicationContext());
    }

    class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        private List<Ingredient> ingredientList = new ArrayList<Ingredient>();

        public IngredientRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {}

        @Override
        public void onDataSetChanged() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            int recipeId = sharedPreferences.getInt(getString(R.string.extra_recipe_id), -1);

            if (recipeId != -1) {
                Cursor cursor = getContentResolver().query(IngredientContract.IngredientEntry.CONTENT_URI,
                        null,
                        IngredientContract.IngredientEntry.COLUMN_NAME_RECIPE_ID + " = ?",
                        new String[] {Integer.toString(recipeId)},
                        null);
                this.ingredientList = createIngredientList(cursor);
            }

            if (ingredientList.size() == 0) {
                ingredientList.add(new Ingredient(-1, new BigDecimal(0), "", "No Recipe Selected"));
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredientList == null) return 0;
            return ingredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_list_item);

            Ingredient ingredient = ingredientList.get(position);

            String plural = "";
            if (ingredient.getQuantity().compareTo(new BigDecimal(1)) > 0) {
                plural = "s";
            }

            String ingredientText = String.format("%s %s%s %s",
                                                  ingredient.getQuantity() != BigDecimal.ZERO ? ingredient.getQuantity() : "",
                                                  ingredient.getMeasure(),
                                                  plural,
                                                  ingredient.getIngredient());


            views.setTextViewText(R.id.text_view_widget_ingredient, ingredientText);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private List<Ingredient> createIngredientList(Cursor ingredientDataCursor) {
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
        try {
            while (ingredientDataCursor.moveToNext()) {
                ingredientList.add(new Ingredient(ingredientDataCursor));
            }
        }
        finally {
            ingredientDataCursor.close();
        }
        return ingredientList;
    }
}
