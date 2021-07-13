package com.example.food4u;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Recipe {


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

//    public ArrayList<Recipe> processResults(JSONObject response){
//        ArrayList<Recipe> allRecipes = new ArrayList<>();
//        try {
//
//                JSONObject recipeCatalogJSON = new JSONObject(jsonData);
//                JSONArray recipesJSON = recipeCatalogJSON.getJSONArray("hits");
//                for (int i = 0; i < recipesJSON.length(); i++) {
//                    //gets specific hit
//                    JSONObject recipeJSON = recipesJSON.getJSONObject(i);
//                    //goes into the recipe portion of hit
//                    JSONObject currentRecipe = recipeJSON.getJSONObject("recipe");
//                    recipeName = currentRecipe.getString("label");
//                    image = currentRecipe.getString("image");
//                    recipeURL = currentRecipe.getString("url");
//                    calories = Integer.toString(currentRecipe.getInt("calories"));
//                    servings = Integer.toString(currentRecipe.getInt("yield"));
//
//                    //looks into an array of ingredients and add them to the recipe
//                    ingredients = new ArrayList<>();
//                    JSONArray ingredientList = currentRecipe.getJSONArray("ingredientLines");
//                    for (int j = 0; j < ingredientList.length(); j++) {
//                        ingredients.add(ingredientList.get(j).toString());
//                    }
//
//                    Recipe recipe = new Recipe(recipeName, image, recipeURL, ingredients, calories, servings);
//                    allRecipes.add(recipe);
//                }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return allRecipes;
//    }




}
