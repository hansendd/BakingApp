package com.udacity.baking_app.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.baking_app.data.RecipeContract;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private int servings;
    private String image;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;

    public Recipe() {}

    public Recipe(Cursor recipeDataCursor) {
        int idIndex = recipeDataCursor.getColumnIndexOrThrow(RecipeContract.RecipeEntry.COLUMN_NAME_ID);
        int nameIndex = recipeDataCursor.getColumnIndexOrThrow(RecipeContract.RecipeEntry.COLUMN_NAME_NAME);
        int servingsIndex = recipeDataCursor.getColumnIndexOrThrow(RecipeContract.RecipeEntry.COLUMN_NAME_SERVINGS);
        int imageIndex = recipeDataCursor.getColumnIndexOrThrow(RecipeContract.RecipeEntry.COLUMN_NAME_IMAGE);
        this.id = recipeDataCursor.getInt(idIndex);
        this.name = recipeDataCursor.getString(nameIndex);
        this.servings = recipeDataCursor.getInt(servingsIndex);
        this.image = recipeDataCursor.getString(imageIndex);
    }

    public Recipe(int id,
                  String name,
                  int servings,
                  String image,
                  List<Ingredient> ingredientList,
                  List<Step> stepList) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredientList = ingredientList;
        this.stepList = stepList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeTypedList(ingredientList);
        parcel.writeTypedList(stepList);
    }

    public Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.servings = in.readInt();
        this.image = in.readString();
        this.ingredientList = new ArrayList<Ingredient>();
        in.readTypedList(ingredientList, Ingredient.CREATOR);
        this.stepList = new ArrayList<Step>();
        in.readTypedList(stepList, Step.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {

        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
