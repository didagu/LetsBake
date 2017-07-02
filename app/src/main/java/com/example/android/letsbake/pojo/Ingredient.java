package com.example.android.letsbake.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dominic Idagu on 01/07/2017.
 */

public class Ingredient implements Parcelable{

    private double quantity;
    private String measure;
    private String ingredient;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {

        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public double getQuantity() {

        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Ingredient(JSONObject jsonObject){
        try{
            this.quantity=jsonObject.getDouble("quantity");
            this.measure=jsonObject.optString("measure");
            this.ingredient=jsonObject.optString("ingredient");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
