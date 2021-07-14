package com.example.food4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.food4u.databinding.ActivityMainBinding;
import com.example.food4u.fragments.HomeFragment;
import com.example.food4u.fragments.QuestionOne;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static String REQUEST_URL = "https://api.edamam.com/api/recipes/v2?type=public&app_id=f19437bb&app_key=655d39c01f4f38804731f9996ab01ee8";
    public static final String TAG = "MainActivity";

    ActivityMainBinding binding;
    RequestQueue recipeQueue;
    public String healthTags;
    final FragmentManager fragmentManager  = getSupportFragmentManager();;
    public static ArrayList<Recipe> allRecipes = new ArrayList<>();

//    public MainActivity(){}
//
////    public MainActivity(FragmentManager fragmentManager) {
////        this.fragmentManager = fragmentManager;
////    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);


        //retrieve api
        recipeQueue = Volley.newRequestQueue(this);

        //add the health tags to the URL if not null

        healthTags = QuestionOne.healthStringTags;
        System.out.println(healthTags);
        if(healthTags != null){
            REQUEST_URL+=healthTags;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,REQUEST_URL,null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray results = response.getJSONArray("hits");
                    Log.i(TAG, "Results" + results.toString());
                    Log.i(TAG, "OnSuccess");
                    processResults(results);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse (VolleyError error){
                    Log.i(TAG, "OnFailure");
                    error.printStackTrace();

                }
            });
        recipeQueue.add(jsonObjectRequest);
        //Recipe.processResults(jsonObjectRequest);


        binding.bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new HomeFragment();
                        break;
                }

               fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });

        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    public ArrayList<Recipe> processResults(JSONArray response){
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

                //looks into an array of ingredients and add them to the recipe
                ArrayList<String> ingredients = new ArrayList<>();
                JSONArray ingredientList = currentRecipe.getJSONArray("ingredientLines");
                for (int j = 0; j < ingredientList.length(); j++) {
                    ingredients.add(ingredientList.get(j).toString());
                }

                Recipe recipe = new Recipe(recipeName, image, recipeURL, ingredients, calories, servings);
                allRecipes.add(recipe);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
       Log.e(TAG, "recipe size in main activity" + allRecipes.size());
        return allRecipes;
    }




}

