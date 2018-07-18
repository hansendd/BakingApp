package com.udacity.baking_app.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.baking_app.R;
import com.udacity.baking_app.adapter.RecipeListAdapter;
import com.udacity.baking_app.model.Recipe;
import com.udacity.baking_app.ui.RecipeListFragment;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.RecipeListOnClickHandler {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent recipeDetailIntent = new Intent(this,
                                               RecipeDetailActivity.class);
        recipeDetailIntent.putExtra(getString(R.string.extra_recipe), recipe);
        startActivity(recipeDetailIntent);
    }
}
