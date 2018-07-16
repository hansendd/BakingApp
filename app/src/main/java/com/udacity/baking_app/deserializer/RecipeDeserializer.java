package com.udacity.baking_app.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.udacity.baking_app.model.Ingredient;
import com.udacity.baking_app.model.Recipe;
import com.udacity.baking_app.model.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// Using example from http://www.javacreed.com/gson-deserialiser-example/
public class RecipeDeserializer implements JsonDeserializer<Recipe> {
    @Override
    public Recipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final int id = jsonObject.get("id").getAsInt();
        final String name = jsonObject.get("name").getAsString();
        final int servings = jsonObject.get("servings").getAsInt();
        final String image = jsonObject.get("image").getAsString();

        JsonArray jsonArray = jsonObject.get("ingredients").getAsJsonArray();
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();;
        for (int i = 0; i < jsonArray.size(); i++) {
            final JsonElement jsonElement = jsonArray.get(i);
            Ingredient ingredient = new Gson().fromJson(jsonElement, Ingredient.class);
            ingredientList.add(ingredient);
        }

        jsonArray = jsonObject.get("steps").getAsJsonArray();
        List<Step> stepList = new ArrayList<Step>();;
        for (int i = 0; i < jsonArray.size(); i++) {
            final JsonElement jsonElement = jsonArray.get(i);
            Step step = new Gson().fromJson(jsonElement, Step.class);
            stepList.add(step);
        }

        return new Recipe(id,
                          name,
                          servings,
                          image,
                          ingredientList,
                          stepList);
    }
}
