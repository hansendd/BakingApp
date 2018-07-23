package com.udacity.baking_app.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.udacity.baking_app.R;
import com.udacity.baking_app.data.IngredientContract;
import com.udacity.baking_app.model.Ingredient;
import com.udacity.baking_app.widget.IngredientWidgetProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IngredientService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENT_WIDGET = "com.udacity.baking_app.action.update_ingredient_widget";

    public IngredientService() {
        super("IngredientService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENT_WIDGET.equals(action)) {
                handleActionUpdateIngredientWidget();
            }
        }
    }

    private void handleActionUpdateIngredientWidget() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String recipeName = sharedPreferences.getString(getString(R.string.extra_recipe_name), "");
        int recipeId = sharedPreferences.getInt(getString(R.string.extra_recipe_id), -1);

        Cursor cursor = getContentResolver().query(IngredientContract.IngredientEntry.CONTENT_URI,
                                                   null,
                                                   IngredientContract.IngredientEntry.COLUMN_NAME_RECIPE_ID + " = ?",
                                                   new String[] {Integer.toString(recipeId)},
                                                   null);
        List<Ingredient> ingredientList = createIngredientList(cursor);
//        String ingredients = formatIngredients(recipeName, ingredientList);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_ingredients);
        IngredientWidgetProvider.updateIngredientWidget(this, appWidgetManager, appWidgetIds, ingredientList);
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

//    private String formatIngredients(String recipeName,
//                                     List<Ingredient> ingredientList) {
//        String lineFeed = System.getProperty("line.separator");
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(String.format("Ingredients for %s:", recipeName));
//        stringBuilder.append(lineFeed);
//        for (Ingredient i : ingredientList) {
//            String plural = "";
//            if (i.getQuantity().compareTo(new BigDecimal(1)) > 0) {
//                plural = "s";
//            }
//
//            stringBuilder.append(String.format("%s %s%s %s%s",
//                    i.getQuantity(),
//                    i.getMeasure(),
//                    plural,
//                    i.getIngredient(),
//                    lineFeed));
//        }
//        return stringBuilder.toString();
//    }

    public static void startActionUpdateIngredientWidget(Context context) {
        Intent intent = new Intent(context, IngredientService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_WIDGET);
        context.startService(intent);
    }


}
