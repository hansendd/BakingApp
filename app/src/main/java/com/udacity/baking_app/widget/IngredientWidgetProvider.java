package com.udacity.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.udacity.baking_app.R;
import com.udacity.baking_app.model.Ingredient;
import com.udacity.baking_app.service.IngredientListWidgetService;
import com.udacity.baking_app.service.IngredientService;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews remoteViews = getIngredientRemoteListView(context);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        IngredientService.startActionUpdateIngredientWidget(context);
    }

    public static void updateIngredientWidget(Context context,
                                              AppWidgetManager appWidgetManager,
                                              int[] appWidgetIds ) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getIngredientRemoteListView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),  R.layout.ingredient_widget_provider);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = sharedPreferences.getString("RECIPE_NAME", "");
        views.setTextViewText(R.id.textview_widget_recipe_name, recipeName);

        Intent intent = new Intent(context, IngredientListWidgetService.class);
        views.setRemoteAdapter(R.id.list_view_ingredients, intent);

        return views;
    }
}

