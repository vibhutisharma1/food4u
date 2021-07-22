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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    public static final String TAG = "SearchActivity";
    protected HomeAdapter adapter;
    protected List<Recipe> searchRecipes;

    public SearchActivity() {
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

        // Create an adapter
        adapter = new HomeAdapter(this, searchRecipes);

        // Bind adapter to list
        binding.rvPosts.setAdapter(adapter);
        //set layout manager
        binding.rvPosts.setLayoutManager(layout);

        // Get the intent, verify the action and get the search query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String new_url = MainActivity.REQUEST_URL;
            if (query != null) {
                new_url += "&q=" + query;
            }
            Log.i(TAG, "query is " + query);
            //retrieve the new recipes with the search query
            Recipe.retrieveFromAPI(new_url, this, searchRecipes, adapter);
        }
    }

}