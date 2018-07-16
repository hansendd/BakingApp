package com.udacity.baking_app.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.baking_app.data.IngredientContract;

import java.math.BigDecimal;

public class Ingredient implements Parcelable {
    private int recipeId;
    private BigDecimal quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {}

    public Ingredient(Cursor ingredientDataCursor) {
        int recipeIdIndex = ingredientDataCursor.getColumnIndexOrThrow(IngredientContract.IngredientEntry.COLUMN_NAME_RECIPE_ID);
        int quantityIndex = ingredientDataCursor.getColumnIndexOrThrow(IngredientContract.IngredientEntry.COLUMN_NAME_QUANTITY);
        int measureIndex = ingredientDataCursor.getColumnIndexOrThrow(IngredientContract.IngredientEntry.COLUMN_NAME_MEASUREMENT);
        int ingredientIndex = ingredientDataCursor.getColumnIndexOrThrow(IngredientContract.IngredientEntry.COLUMN_NAME_INGREDIENT);
        this.recipeId = ingredientDataCursor.getInt(recipeIdIndex);
        this.quantity = new BigDecimal(ingredientDataCursor.getDouble(quantityIndex));
        this.measure = ingredientDataCursor.getString(measureIndex);
        this.ingredient = ingredientDataCursor.getString(ingredientIndex);
    }

    public Ingredient(int recipeId,
                      BigDecimal quantity,
                      String measure,
                      String ingredient) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(recipeId);
        parcel.writeDouble(quantity.doubleValue());
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public Ingredient(Parcel in) {
        this.recipeId = in.readInt();
        this.quantity = new BigDecimal(in.readDouble());
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    static final Parcelable.Creator<Ingredient> CREATOR
            = new Parcelable.Creator<Ingredient>() {

        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
