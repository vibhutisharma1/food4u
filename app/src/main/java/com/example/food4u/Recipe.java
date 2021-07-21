package com.example.food4u;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Recipe implements Serializable {

    String recipeName;
    String image;
    String recipeURL;
    String calories;
    String servings;
    ArrayList<String> ingredients;

    public Recipe(String recipeName, String image, String recipeURL, ArrayList<String> ingredients, String calories, String servings) {
        this.recipeName = recipeName;
        this.image = image;
        this.recipeURL = recipeURL;
        this.calories = calories;
        this.servings = servings;
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getCalories() {
        return calories;
    }

    public String getServings() {
        return servings;
    }

    public String getRecipeURL() {
        return recipeURL;
    }

    public String getImage() {
        return image;
    }

    public String getRecipeName() {
        return recipeName;
    }


}
