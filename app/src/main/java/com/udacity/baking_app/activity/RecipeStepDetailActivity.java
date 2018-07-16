package com.udacity.baking_app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.baking_app.R;
import com.udacity.baking_app.ui.RecipeStepDetailFragment;

public class RecipeStepDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Bundle selectedRecipeBundle = getIntent().getExtras();

        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setArguments(selectedRecipeBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.recipe_step_detail_container, recipeStepDetailFragment).commit();
    }
}
