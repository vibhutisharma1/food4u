package com.example.food4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.food4u.databinding.ActivityMainBinding;
import com.example.food4u.databinding.ActivitySearchBinding;
import com.example.food4u.databinding.FragmentHomeBinding;
import com.example.food4u.fragments.HomeFragment;
import com.example.food4u.fragments.QuestionOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    public static final String TAG = "SearchActivity";
    protected HomeAdapter adapter;
    protected List<Recipe> searchRecipes;
    RequestQueue recipeQueue;

    public SearchActivity(){
        //empty constructor required
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // allows for optimizations
        binding.rvPosts.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(this, 2);

        searchRecipes = new ArrayList<>();

        Log.i(TAG, "sets search activity ");

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String new_url = MainActivity.REQUEST_URL;
            if(query != null){
                new_url+="&q=" + query;
            }
            Log.i(TAG, "query is " + query);
            retrieveFromAPI(new_url);
        }

        // Create an adapter
        adapter = new HomeAdapter(this, searchRecipes);
        // Bind adapter to list
        binding.rvPosts.setAdapter(adapter);
        //set layout manager
        binding.rvPosts.setLayoutManager(layout);
    }


    public void retrieveFromAPI(String url){
        //retrieve api
        recipeQueue = Volley.newRequestQueue(this);
        Toast.makeText(this,"retrieve api search activity opened", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "retrieve api method");
        //filter different pages
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("hits");
                    Log.i(TAG, "Results" + results.toString());
                    Log.i(TAG, "OnSuccess");
                    processResults(results);
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

    public void processResults(JSONArray response){
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
                searchRecipes.add(recipe);
                //Collections.shuffle(searchRecipes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}