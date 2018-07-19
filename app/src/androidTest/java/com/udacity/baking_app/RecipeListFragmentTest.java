package com.udacity.baking_app;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.baking_app.activity.MainActivity;
import com.udacity.baking_app.activity.RecipeDetailActivity;
import com.udacity.baking_app.model.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeListFragmentTest {
    // This was done with the help from the answer in:
    // https://stackoverflow.com/questions/34628700/mocking-intent-extras-in-espresso-tests
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class)
            {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra("RECIPE", new Recipe());
                    return result;
                }
            };

    @Test
    public void checkRecipeClickTransferToRecipeDetail() {
        onView(withId(R.id.recycler_view_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_view_step_list)).check(matches(isDisplayed()));
    }

    @Test
    public void checkRecipeClickTransferToRecipeDetailToTransferToStepDetail() {
        onView(withId(R.id.recycler_view_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_view_step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_detail_step_container)).check(matches(isDisplayed()));
    }
}
