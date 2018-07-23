package com.udacity.baking_app.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.baking_app.R;
import com.udacity.baking_app.model.Ingredient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IngredientListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        private List<Ingredient> ingredientList;

        public IngredientRemoteViewsFactory(Context context,
                                            Intent intent) {
            this.context = context;
            ingredientList = intent.getParcelableArrayListExtra("WIDGET_LIST");
        }

        @Override
        public void onCreate() {}

        @Override
        public void onDataSetChanged() {
            this.ingredientList = new ArrayList<>();
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

            String ingredientText = String.format("%s %s%s %s%s",
                                                  ingredient.getQuantity(),
                                                  ingredient.getMeasure(),
                                                  plural,
                                                  ingredient.getIngredient());


            views.setTextViewText(R.id.text_view_widget_ingredient, ingredientText);

            Intent intent = new Intent();
            views.setOnClickFillInIntent(R.id.text_view_widget_ingredient, intent);

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
}
