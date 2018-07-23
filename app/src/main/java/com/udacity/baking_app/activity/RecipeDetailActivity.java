package com.udacity.baking_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.udacity.baking_app.R;
import com.udacity.baking_app.adapter.RecipeDetailStepAdapter;
import com.udacity.baking_app.model.Recipe;
import com.udacity.baking_app.model.Step;
import com.udacity.baking_app.ui.RecipeDetailFragment;
import com.udacity.baking_app.ui.RecipeDetailStepFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailStepAdapter.RecipeDetailStepOnClickHandler {

    private boolean twoPane;
    private String recipeName;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        LinearLayout linearLayoutRecipeDetail = findViewById(R.id.linear_layout_recipe_detail);

        Bundle selectedRecipeBundle = getIntent().getExtras();

        if (recipe == null) {
            recipe = getIntent().getParcelableExtra(getString(R.string.extra_recipe));
        }

        recipeName = recipe.getName();

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(selectedRecipeBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.recipe_detail_container, recipeDetailFragment).commit();

        if (linearLayoutRecipeDetail != null) {
            twoPane = true;
        }

        Toolbar navigationToolbar = (Toolbar) findViewById(R.id.toolbar_navigation);
        setSupportActionBar(navigationToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(recipeName);
    }

    @Override
    public void onClickRecipeDetailStep(int selectedIndex,
                                        List<Step> stepList) {
        if (!twoPane) {
            Intent recipeDetailIntent = new Intent(this,
                                                   RecipeDetailStepActivity.class);
            recipeDetailIntent.putParcelableArrayListExtra(getString(R.string.extra_step_list), new ArrayList(stepList));
            recipeDetailIntent.putExtra(getString(R.string.extra_selected_index), selectedIndex);
            recipeDetailIntent.putExtra(getString(R.string.extra_recipe_name), recipeName);
            startActivity(recipeDetailIntent);
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(getString(R.string.extra_step_list), new ArrayList(stepList));
            bundle.putInt(getString(R.string.extra_selected_index), selectedIndex);
            bundle.putString(getString(R.string.extra_recipe_name), recipeName);
            RecipeDetailStepFragment recipeDetailStepFragment = new RecipeDetailStepFragment();
            recipeDetailStepFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.recipe_detail_step_container, recipeDetailStepFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
//            onBackPressed();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.extra_recipe), recipe);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable(getString(R.string.extra_recipe));
    }
}
