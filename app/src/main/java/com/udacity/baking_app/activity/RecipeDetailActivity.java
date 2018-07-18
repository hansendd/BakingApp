package com.udacity.baking_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.udacity.baking_app.R;
import com.udacity.baking_app.adapter.RecipeDetailStepAdapter;
import com.udacity.baking_app.model.Step;
import com.udacity.baking_app.ui.RecipeDetailFragment;
import com.udacity.baking_app.ui.RecipeDetailStepFragment;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailStepAdapter.RecipeDetailStepOnClickHandler {

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        LinearLayout linearLayoutRecipeDetail = findViewById(R.id.linear_layout_recipe_detail);

        Bundle selectedRecipeBundle = getIntent().getExtras();
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(selectedRecipeBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.recipe_detail_container, recipeDetailFragment).commit();

        if (linearLayoutRecipeDetail != null) {
            twoPane = true;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(Step step) {
        if (!twoPane) {
            Intent recipeDetailIntent = new Intent(this,
                                                   RecipeDetailStepActivity.class);
            recipeDetailIntent.putExtra("step", step);
            startActivity(recipeDetailIntent);
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("step", step);
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
}
