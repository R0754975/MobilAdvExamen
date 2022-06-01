package com.example.mobiladvexamen;

import org.json.JSONArray;

import java.util.ArrayList;

public class Recipe {
    private String label;
    private String image;
    private ArrayList mealIngredients;

    public Recipe(String label_, String image_, ArrayList mealIngredients_){
        this.label = label_;
        this.image = image_;
        this.mealIngredients = mealIngredients_;

    }
    public String getImage(){
        return image;
    }

    public String getLabel(){
        return label;
    }

    public ArrayList getMealIngredients() {
        return mealIngredients;
    }





}
