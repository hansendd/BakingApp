package com.udacity.baking_app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.udacity.baking_app.R;
import com.udacity.baking_app.ui.RecipeDetailStepFragment;

import butterknife.BindView;

public class RecipeDetailStepActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_step);

        if (savedInstanceState == null) {
            Bundle selectedRecipeBundle = getIntent().getExtras();

            RecipeDetailStepFragment recipeDetailStepFragment = new RecipeDetailStepFragment();
            recipeDetailStepFragment.setArguments(selectedRecipeBundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.recipe_detail_step_container, recipeDetailStepFragment).commit();

            String recipeName = getIntent().getStringExtra(getString(R.string.extra_recipe_name));

            Toolbar navigationToolbar = (Toolbar) findViewById(R.id.toolbar_navigation);
            setSupportActionBar(navigationToolbar);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(recipeName);
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
