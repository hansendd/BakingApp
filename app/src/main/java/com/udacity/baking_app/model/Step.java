package com.udacity.baking_app.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.baking_app.data.StepContract;

import java.math.BigDecimal;

public class Step implements Parcelable {
    private int recipeId;
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step() {}

    public Step(Cursor stepDataCursor) {
        int idIndex = stepDataCursor.getColumnIndexOrThrow(StepContract.StepEntry.COLUMN_NAME_ID);
        int recipeIdIndex = stepDataCursor.getColumnIndexOrThrow(StepContract.StepEntry.COLUMN_NAME_RECIPE_ID);
        int shortDescriptionIndex = stepDataCursor.getColumnIndexOrThrow(StepContract.StepEntry.COLUMN_NAME_SHORT_DESCRIPTION);
        int descriptionIndex = stepDataCursor.getColumnIndexOrThrow(StepContract.StepEntry.COLUMN_NAME_DESCRIPTION);
        int videoURLIndex = stepDataCursor.getColumnIndexOrThrow(StepContract.StepEntry.COLUMN_NAME_VIDEO_URL);
        int thumbnailURLIndex = stepDataCursor.getColumnIndexOrThrow(StepContract.StepEntry.COLUMN_NAME_THUMBNAIL_URL);
        this.id = stepDataCursor.getInt(idIndex);
        this.recipeId = stepDataCursor.getInt(recipeIdIndex);
        this.shortDescription = stepDataCursor.getString(shortDescriptionIndex);
        this.description = stepDataCursor.getString(descriptionIndex);
        this.videoURL = stepDataCursor.getString(videoURLIndex);
        this.thumbnailURL = stepDataCursor.getString(thumbnailURLIndex);
    }

    public Step(int recipeId,
                int id,
                String shortDescription,
                String description,
                String videoURL,
                String thumbnailURL) {
        this.recipeId = recipeId;
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(recipeId);
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    public Step(Parcel in) {
        this.recipeId = in.readInt();
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR
            = new Parcelable.Creator<Step>() {

        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
