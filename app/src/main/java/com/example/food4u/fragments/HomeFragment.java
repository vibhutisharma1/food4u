package com.example.food4u.fragments;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.food4u.HomeAdapter;
import com.example.food4u.MainActivity;
import com.example.food4u.R;
import com.example.food4u.Recipe;
import com.example.food4u.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import okhttp3.Headers;


public class HomeFragment extends Fragment  {

    FragmentHomeBinding binding;
    public static final String TAG = "HomeFragment";
    protected HomeAdapter adapter;
    protected List<Recipe> allRecipes;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the intent, verify the action and get the query
        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String new_url = MainActivity.REQUEST_URL;
            if(query != null){
                new_url+="&q=" + query;
            }
            Log.i(TAG, "query is " + query);
            allRecipes.clear();
            retrieveFromAPI(new_url);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Find RecyclerView and bind to adapter

        // allows for optimizations
        binding.rvPosts.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 2);

        //add the new health tags to url
        String healthTags = QuestionOne.healthStringTags;

        allRecipes = new ArrayList<>();

        //maybe set this info into a new string to avoid failure if crash
        //remove duplicate tags
        if(healthTags != null){
            MainActivity.REQUEST_URL+=healthTags;
        }
        // Create an adapter
        adapter = new HomeAdapter(getContext(), allRecipes);
        // Bind adapter to list
        binding.rvPosts.setAdapter(adapter);
        //set layout manager
        binding.rvPosts.setLayoutManager(layout);

        retrieveFromAPI(MainActivity.REQUEST_URL);

    }

    public void retrieveFromAPI(String url){
        //retrieve api
        RequestQueue recipeQueue = Volley.newRequestQueue(getContext());
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
                allRecipes.add(recipe);
                Collections.shuffle(allRecipes);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}