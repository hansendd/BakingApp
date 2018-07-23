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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_ingredients);
        IngredientWidgetProvider.updateIngredientWidget(this, appWidgetManager, appWidgetIds);
    }

    public static void startActionUpdateIngredientWidget(Context context) {
        Intent intent = new Intent(context, IngredientService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_WIDGET);
        context.startService(intent);
    }


}
