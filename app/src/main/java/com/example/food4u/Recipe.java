package com.example.food4u;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {

    private static final String TAG = "Recipe";
    String recipeName;
    String image;
    String recipeURL;
    String calories;
    String servings;
    ArrayList<String> ingredients;
    Map<String, String> nutrientMap;

    public Recipe(String recipeName, String image, String recipeURL){
        this.recipeName = recipeName;
        this.image = image;
        this.recipeURL = recipeURL;
    }

    public Recipe(String recipeName, String image, String recipeURL, ArrayList<String> ingredients, String calories, String servings, Map<String, String> nutrientMap) {
        this.recipeName = recipeName;
        this.image = image;
        this.recipeURL = recipeURL;
        this.calories = calories;
        this.servings = servings;
        this.ingredients = ingredients;
        this.nutrientMap = nutrientMap;
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

    public static void retrieveFromAPI(String url, Context context, List<Recipe> allRecipes, HomeAdapter adapter) {
        //call api with current url
        RequestQueue recipeQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("hits");
                    Log.i(TAG, "Results" + results.toString());
                    Log.i(TAG, "OnSuccess");
                    processResults(results, allRecipes);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "OnFailure");
                error.printStackTrace();

            }
        });
        recipeQueue.add(jsonObjectRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void processResults(JSONArray response, List<Recipe> allRecipes) {
        try {
            for (int i = 0; i < response.length(); i++) {
                //gets specific hit
                JSONObject recipeJSON = response.getJSONObject(i);
                //goes into the recipe portion of hit
                JSONObject currentRecipe = recipeJSON.getJSONObject("recipe");
                String recipeName = currentRecipe.getString("label");
                String image = currentRecipe.getString("image");
                String recipeURL = currentRecipe.getString("url");
                String calories = Integer.toString(currentRecipe.getInt("calories"));
                String servings = Integer.toString(currentRecipe.getInt("yield"));

                //retrieves an array of ingredients and add them to the recipe
                ArrayList<String> ingredients = new ArrayList<>();
                JSONArray ingredientList = currentRecipe.getJSONArray("ingredientLines");

                for (int j = 0; j < ingredientList.length(); j++) {
                    ingredients.add(ingredientList.get(j).toString());
                }
                //create hash map of key-nutrient labels and value-quantity
                JSONObject totalNutrients = currentRecipe.getJSONObject("totalNutrients");

                Map<String, String> nutrients = parseNutrition(totalNutrients);

                Recipe recipe = new Recipe(recipeName, image, recipeURL, ingredients, calories, servings, nutrients);
                allRecipes.add(recipe);
                //randomize order
                Collections.shuffle(allRecipes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static Map<String,String> parseNutrition(JSONObject nutrition) throws JSONException {
        Map<String, String> nutrientMap = new HashMap<>();
        //set the keys to the JSON labels
        nutrientMap.put("ENERC_KCAL", "0");
        nutrientMap.put("FAT", "0");
        nutrientMap.put("FASAT", "0");
        nutrientMap.put("FAMS", "0");
        nutrientMap.put("FAPU", "0");
        nutrientMap.put("CHOCDF", "0");
        nutrientMap.put("FIBTG", "0");
        nutrientMap.put("SUGAR", "0");
        nutrientMap.put("PROCNT", "0");

        for (Map.Entry<String, String> entry : nutrientMap.entrySet()) {
            String currentKey = entry.getKey();
            //get the json object of nutrient label
            JSONObject nutrientObject = nutrition.getJSONObject(currentKey);
            //retrieve the nutrient quantity and put in hashmap
            nutrientMap.put(currentKey, Integer.toString(nutrientObject.getInt("quantity")));
        }
        return nutrientMap;
    }

    public Map<String, String> getNutrientMap() {
        return nutrientMap;
    }

    public String getFat() {
        return nutrientMap.get("FAT");
    }

    public String getProtein() {
        return nutrientMap.get("PROCNT");
    }

    public String getCarb() {
        return nutrientMap.get("CHOCDF");
    }







}