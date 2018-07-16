package com.udacity.baking_app.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.baking_app.R;
import com.udacity.baking_app.adapter.RecipeListAdapter;
import com.udacity.baking_app.model.Recipe;
import com.udacity.baking_app.ui.RecipeListFragment;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeListFragment recipeListFragment = new RecipeListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.recipe_list_container, recipeListFragment).commit();
    }
}
