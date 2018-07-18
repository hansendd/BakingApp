package com.udacity.baking_app.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.baking_app.R;
import com.udacity.baking_app.activity.MainActivity;
import com.udacity.baking_app.adapter.RecipeListAdapter;
import com.udacity.baking_app.data.IngredientContract;
import com.udacity.baking_app.data.RecipeContract;
import com.udacity.baking_app.data.StepContract;
import com.udacity.baking_app.deserializer.RecipeDeserializer;
import com.udacity.baking_app.model.Ingredient;
import com.udacity.baking_app.model.Recipe;
import com.udacity.baking_app.model.Step;
import com.udacity.baking_app.utility.NetworkConnectionUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment {
    private static final String className = RecipeListFragment.class.toString();

    @BindView(R.id.recycler_view_recipe_list) RecyclerView recipeListRecyclerView;
//    @BindView(R.id.progress_bar_recipe_list) ProgressBar recipeListProgressBar;
//    @BindView(R.id.textview_recipe_list_check) TextView recipeListCheckTextView;

    private RecipeListAdapter recipeListAdapter;
    private List<Recipe> recipeList = new ArrayList<Recipe>();


    public RecipeListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);

        if (NetworkConnectionUtility.haveActiveNetworkConnection(getConnectivityManager())) {
//            recipeListProgressBar.setVisibility(View.VISIBLE);
//            recipeListCheckTextView.setVisibility(View.VISIBLE);
            try {
                new RecipeCheck().execute().get();
            } catch (Exception e) {
                displayErrorWithData();
            }
//            recipeListProgressBar.setVisibility(View.INVISIBLE);
//            recipeListCheckTextView.setVisibility(View.INVISIBLE);
        }
        else {
            NetworkConnectionUtility.displayNoNetworkConnection(getContext());
        }

        recipeListAdapter = new RecipeListAdapter(getContext(),
                                                  (MainActivity)getActivity(),
                                                  recipeList);
        recipeListRecyclerView.setAdapter(recipeListAdapter);


        GridLayoutManager gridLayoutManager;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(getContext(), 4);
        }
        else {
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
        }
        recipeListRecyclerView.setLayoutManager(gridLayoutManager);

        recipeListRecyclerView.setHasFixedSize(true);

//        recipeListProgressBar.setVisibility(View.VISIBLE);
//        recipeListCheckTextView.setVisibility(View.VISIBLE);
        try {
            new RecipeRetrieval().execute();
        } catch (Exception e) {
            displayErrorWithData();
        }
//        recipeListProgressBar.setVisibility(View.INVISIBLE);
//        recipeListCheckTextView.setVisibility(View.INVISIBLE);


        return view;
    }

    public class RecipeCheck extends AsyncTask {
        private Exception exception;

        @Override
        protected Object doInBackground(Object[] objects) {
            final String dataURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
            try {
                URL url = buildURL(dataURL);
                String json = getRecipes(url);
                List<Recipe> recipeList = createRecipeList(json);
                validateRecipes(recipeList);
            }
            catch (Exception exception) {
                this.exception = exception;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (exception != null) {
                Log.e(className, "Exception parsing data", exception);
                displayErrorWithData();
            }
        }

        private URL buildURL(String dataURL) throws MalformedURLException {
            return new URL(dataURL);
        }

        private String getRecipes(URL url) throws IOException {
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            try {
                httpURLConnection.connect();
                return getResponseJson(httpURLConnection.getInputStream());
            }
            finally {
                httpURLConnection.disconnect();
            }
        }

        private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }

        private String getResponseJson(InputStream inputStream) throws IOException {
            // Taken from
            // https://www.androidauthority.com/use-remote-web-api-within-android-app-617869/
            // Refactored
            InputStreamReader inputStreamReader = createInputStreamReader(inputStream);
            BufferedReader bufferedReader = createBufferedReader(inputStreamReader);
            return createResponseJson(bufferedReader);
        }

        private InputStreamReader createInputStreamReader(InputStream inputStream) {
            return new InputStreamReader(inputStream);
        }

        private BufferedReader createBufferedReader(InputStreamReader inputStreamReader) {
            return new BufferedReader(inputStreamReader);
        }

        private String createResponseJson(BufferedReader bufferedReader) throws IOException {
            // Streams is minimum JDK of 24, we have listed 16 for project
            // So streams should be avoided
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }

        private List<Recipe> createRecipeList(String json) {
            // http://www.javacreed.com/gson-deserialiser-example/
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Recipe.class, new RecipeDeserializer());
            Gson gson = gsonBuilder.create();
            List<Recipe> recipeList = Arrays.asList(gson.fromJson(json, Recipe[].class));
            return recipeList;
        }

        private void validateRecipes(List<Recipe> recipeList) {
            // Avoid using Streams due to API level
            for (Recipe recipe : recipeList) {
                Cursor recipeCursor = null;
                try {
                    recipeCursor = getActivity().getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                            null,
                            RecipeContract.RecipeEntry.COLUMN_NAME_ID + " = ?",
                            new String[] {Integer.toString(recipe.getId())},
                            null);
                    if (recipeCursor.getCount() == 0) {
                        insertRecipe(recipe);
                    }
                }
                finally {
                    if (recipeCursor != null) {
                        recipeCursor.close();
                    }
                }
            }
        }

        private void insertRecipe(Recipe recipe) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_NAME_ID, recipe.getId());
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_NAME_NAME, recipe.getName());
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_NAME_SERVINGS, recipe.getServings());
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_NAME_IMAGE, recipe.getImage());
            getActivity().getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, contentValues);

            insertIngredients(recipe.getId(), recipe.getIngredientList());
            insertSteps(recipe.getId(), recipe.getStepList());
        }

        private void insertIngredients(int recipeId,
                                       List<Ingredient> ingredientList) {
            for (Ingredient ingredient : ingredientList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(IngredientContract.IngredientEntry.COLUMN_NAME_RECIPE_ID, recipeId);
                contentValues.put(IngredientContract.IngredientEntry.COLUMN_NAME_QUANTITY, ingredient.getQuantity().doubleValue());
                contentValues.put(IngredientContract.IngredientEntry.COLUMN_NAME_MEASUREMENT, ingredient.getMeasure());
                contentValues.put(IngredientContract.IngredientEntry.COLUMN_NAME_INGREDIENT, ingredient.getIngredient());
                getActivity().getContentResolver().insert(IngredientContract.IngredientEntry.CONTENT_URI, contentValues);
            }
        }

        private void insertSteps(int recipeId,
                                 List<Step> stepList) {
            for (Step step : stepList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(StepContract.StepEntry.COLUMN_NAME_RECIPE_ID, recipeId);
                contentValues.put(StepContract.StepEntry.COLUMN_NAME_ID, step.getId());
                contentValues.put(StepContract.StepEntry.COLUMN_NAME_SHORT_DESCRIPTION, step.getShortDescription());
                contentValues.put(StepContract.StepEntry.COLUMN_NAME_DESCRIPTION, step.getDescription());
                contentValues.put(StepContract.StepEntry.COLUMN_NAME_VIDEO_URL, step.getVideoURL());
                contentValues.put(StepContract.StepEntry.COLUMN_NAME_THUMBNAIL_URL, step.getThumbnailURL());
                getActivity().getContentResolver().insert(StepContract.StepEntry.CONTENT_URI, contentValues);
            }
        }
    }

    public class RecipeRetrieval extends AsyncTask {
        private Exception exception;

        @Override
        protected List<Recipe> doInBackground(Object[] objects) {
            List<Recipe> recipeList = new ArrayList<Recipe>();
            try {
                Cursor recipeDataCursor = getRecipes();
                recipeList = createRecipeList(recipeDataCursor);
            }
            catch (Exception exception) {
                this.exception = exception;
            }
            return recipeList;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (exception != null) {
                Log.e(className, "Exception retrieving data", exception);
                displayErrorWithData();
            }
            setRecipeList((ArrayList<Recipe>) result);
        }

        private Cursor getRecipes() {
            return getContext().getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                                                           null,
                                                           null,
                                                           null,
                                                           RecipeContract.RecipeEntry.COLUMN_NAME_ID);
        }

        private List<Recipe> createRecipeList(Cursor recipeDataCursor) {
            List<Recipe> recipeList = new ArrayList<Recipe>();
            try {
                while (recipeDataCursor.moveToNext()) {
                    recipeList.add(new Recipe(recipeDataCursor));
                }
            }
            finally {
                recipeDataCursor.close();
            }
            return recipeList;
        }
    }

    private void setRecipeList(List<Recipe> recipeList) {
        this.recipeList.clear();
        this.recipeList.addAll(recipeList);
        recipeListAdapter = new RecipeListAdapter(getContext(),
                                                  (MainActivity)getActivity(),
                                                  this.recipeList);
        recipeListRecyclerView.setAdapter(recipeListAdapter);
    }

    private void displayErrorWithData() {
        Toast.makeText(getContext(), R.string.check_recipe_error, Toast.LENGTH_SHORT).show();
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
