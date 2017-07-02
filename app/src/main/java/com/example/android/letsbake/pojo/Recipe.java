package com.example.android.letsbake.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dominic Idagu on 01/07/2017.
 */

public class Recipe implements Parcelable {

    int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Steps> steps;
    private String servings;
    private String image;

    public Recipe(){}

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getServings() {
        return servings;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        servings = in.readString();
        name = in.readString();
        image = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(servings);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
    }

    public Recipe(JSONObject jsonObject){
        try{
            this.name=jsonObject.getString("name");
            this.servings=jsonObject.getString("servings");
            this.image=jsonObject.getString("image");
            this.ingredients=new ArrayList<>();
            JSONArray ingredientsJsonArray=jsonObject.getJSONArray("ingredients");
            for(int i=0;i < ingredientsJsonArray.length();i++){
                ingredients.add(new Ingredient(ingredientsJsonArray.getJSONObject(i)));
            }
            this.steps= new ArrayList<>();
            JSONArray stepsJsonArray=jsonObject.getJSONArray("steps");
            for(int i=0; i < stepsJsonArray.length();i++){
                steps.add(new Steps(stepsJsonArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
