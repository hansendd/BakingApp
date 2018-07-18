package com.udacity.baking_app.ui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.baking_app.R;
import com.udacity.baking_app.activity.RecipeDetailActivity;
import com.udacity.baking_app.adapter.RecipeDetailStepAdapter;
import com.udacity.baking_app.data.IngredientContract;
import com.udacity.baking_app.data.StepContract;
import com.udacity.baking_app.model.Ingredient;
import com.udacity.baking_app.model.Recipe;
import com.udacity.baking_app.model.Step;
import com.udacity.baking_app.service.IngredientService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment  {
    private static final String className = RecipeListFragment.class.toString();

    @BindView(R.id.textview_ingredients) TextView ingredientsTextView;
    @BindView(R.id.recycler_view_step_list) RecyclerView stepListRecyclerView;

    private RecipeDetailStepAdapter recipeDetailStepAdapter;
//    private List<Step> stepList = new ArrayList<Step>();
    private Recipe recipe;

    public RecipeDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);


        recipeDetailStepAdapter = new RecipeDetailStepAdapter(getContext(),
                                                              (RecipeDetailActivity)getActivity(),
                                                              new ArrayList<Step>());
        stepListRecyclerView.setAdapter(recipeDetailStepAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        stepListRecyclerView.setLayoutManager(gridLayoutManager);

        stepListRecyclerView.setHasFixedSize(true);

        if (savedInstanceState == null) {
            recipe = (Recipe) getArguments().getParcelable(getString(R.string.extra_recipe));

            try {
                new IngredientRetrieval().execute(recipe.getId());
            } catch (Exception e) {
                displayErrorWithData();
            }

            try {
                new StepRetrieval().execute(recipe.getId());
            } catch (Exception e) {
                displayErrorWithData();
            }
        }
        else {
            recipe = savedInstanceState.getParcelable(getString(R.string.extra_recipe));
            recipeDetailStepAdapter.loadSteps(recipe.getStepList());
            formatIngredients();
        }

//        ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(recipe.getName());

        return view;
    }

    // Reference: https://developer.android.com/guide/components/activities/activity-lifecycle#java
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        GridLayoutManager gridLayoutManager = (GridLayoutManager) stepListRecyclerView.getLayoutManager();
//        int scrollLocation = gridLayoutManager.findFirstVisibleItemPosition();
//
//        outState.putInt("SCROLL_LOCATION", scrollLocation);
//        outState.putParcelable("RECIPE", recipe);
    }

    public class IngredientRetrieval extends AsyncTask {
        private Exception exception;
        private int recipeId = 0;

        @Override
        protected List<Ingredient> doInBackground(Object[] objects) {
            List<Ingredient> ingredientList = new ArrayList<Ingredient>();
            recipeId = (int) objects[0];
            try {
                Cursor ingredientDataCursor = getIngredients();
                ingredientList = createIngredientList(ingredientDataCursor);
            }
            catch (Exception exception) {
                this.exception = exception;
            }
            return ingredientList;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (exception != null) {
                Log.e(className, "Exception retrieving data", exception);
                displayErrorWithData();
            }
            setIngredientList((List<Ingredient>) result);
            formatIngredients();
        }

        private Cursor getIngredients() {
            return getContext().getContentResolver().query(IngredientContract.IngredientEntry.CONTENT_URI,
                    null,
                    IngredientContract.IngredientEntry.COLUMN_NAME_RECIPE_ID + " = ?",
                    new String[] {Integer.toString(recipeId)},
                    null);
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

    public class StepRetrieval extends AsyncTask {
        private Exception exception;
        private int recipeId = 0;

        @Override
        protected List<Step> doInBackground(Object[] objects) {
            List<Step> stepList = new ArrayList<Step>();
            recipeId = (int) objects[0];
            try {
                Cursor stepDataCursor = getSteps();
                stepList = createStepList(stepDataCursor);
            }
            catch (Exception exception) {
                this.exception = exception;
            }
            return stepList;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (exception != null) {
                Log.e(className, "Exception retrieving data", exception);
                displayErrorWithData();
            }
            setStepList((ArrayList<Step>) result);
        }

        private Cursor getSteps() {
            return getContext().getContentResolver().query(StepContract.StepEntry.CONTENT_URI,
                    null,
                    StepContract.StepEntry.COLUMN_NAME_RECIPE_ID + " = ?",
                    new String[] {Integer.toString(recipeId)},
                    StepContract.StepEntry.COLUMN_NAME_ID);
        }

        private List<Step> createStepList(Cursor stepDataCursor) {
            List<Step> recipeList = new ArrayList<Step>();
            try {
                while (stepDataCursor.moveToNext()) {
                    recipeList.add(new Step(stepDataCursor));
                }
            }
            finally {
                stepDataCursor.close();
            }
            return recipeList;
        }
    }

    private void formatIngredients() {
        List<Ingredient> ingredentList = recipe.getIngredientList();
        String lineFeed = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ingredients:");
        stringBuilder.append(lineFeed);
        for (Ingredient i : ingredentList) {
            String plural = "";
            if (i.getQuantity().compareTo(new BigDecimal(1)) > 0) {
                plural = "s";
            }

            stringBuilder.append(String.format("%s %s%s %s%s",
                                               i.getQuantity(),
                                               i.getMeasure(),
                                               plural,
                                               i.getIngredient(),
                                               lineFeed));
        }
        ingredientsTextView.setText(stringBuilder.toString());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("RECIPE_ID", recipe.getId());
        editor.putString("RECIPE_NAME", recipe.getName());
        editor.apply();
        IngredientService.startActionUpdateIngredientWidget(getContext());
    }

    private void setStepList(List<Step> stepList) {
        recipe.getStepList().clear();
        recipe.getStepList().addAll(stepList);
        recipeDetailStepAdapter = new RecipeDetailStepAdapter(getContext(),
                                                              (RecipeDetailActivity)getActivity(),
                                                              recipe.getStepList());
        stepListRecyclerView.setAdapter(recipeDetailStepAdapter);
    }

    private void setIngredientList(List<Ingredient> ingredientList) {
        recipe.getIngredientList().clear();
        recipe.getIngredientList().addAll(ingredientList);
    }

    private void displayErrorWithData() {
        Toast.makeText(getContext(), R.string.check_recipe_error, Toast.LENGTH_SHORT).show();
    }
}
