package com.example.food4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.food4u.databinding.ActivityCuisinePageBinding;
import com.example.food4u.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class CuisinePage extends AppCompatActivity {

    ActivityCuisinePageBinding binding;
    protected List<Recipe> allRecipes;
    protected HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCuisinePageBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        String cuisine = getIntent().getStringExtra("CUISINE_NAME");
        binding.tvCuisineTitle.setText(cuisine + " Recipes");

        allRecipes = new ArrayList<>();
        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(CuisinePage.this, 2);

        // Create an adapter
        adapter = new HomeAdapter(CuisinePage.this, allRecipes);
        binding.rvCuisineRecipes.setAdapter(adapter);
        binding.rvCuisineRecipes.setLayoutManager(layout);
        MainActivity main = new MainActivity();
        String cuisine_url = main.REQUEST_URL + "&cuisineType=" + cuisine;
        //retrieve recipes
        Recipe.retrieveFromAPI(cuisine_url, CuisinePage.this, allRecipes, adapter);

    }
}