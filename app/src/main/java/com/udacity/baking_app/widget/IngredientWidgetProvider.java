package com.udacity.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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
                                int appWidgetId,
                                List<Ingredient> ingredientList) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);
//        views.setTextViewText(R.id.appwidget_text, ingredient);
        RemoteViews remoteViews = getIngredientRemoteListView(context, ingredientList);

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
                                              int[] appWidgetIds,
                                              List<Ingredient> ingredientList ) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredientList);
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

    private static RemoteViews getIngredientRemoteListView(Context context,
                                                           List<Ingredient> ingredientList) {
        RemoteViews views = new RemoteViews(context.getPackageName(),  R.layout.ingredient_widget_provider);

        Intent intent = new Intent(context, IngredientListWidgetService.class);
        intent.putParcelableArrayListExtra("WIDGET_LIST", new ArrayList<Ingredient>(ingredientList));
        views.setRemoteAdapter(R.id.list_view_ingredients, intent);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setPendingIntentTemplate(R.id.list_view_ingredients, pendingIntent);

        return views;
    }
}

