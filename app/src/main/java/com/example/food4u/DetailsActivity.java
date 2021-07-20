package com.example.food4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.food4u.databinding.ActivityDetailsBinding;
import com.example.food4u.databinding.ActivityMainBinding;
import com.example.food4u.fragments.DirectionFragment;
import com.example.food4u.fragments.IngredientFragment;
import com.example.food4u.fragments.NutritionFragment;
import com.google.android.material.tabs.TabLayout;

public class DetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        recipe = (Recipe)getIntent().getExtras().getSerializable("CurrentRecipe");

        // tab layout to navigate btwn: ingredients and directions
        TabLayout tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        TabLayout.Tab directionsTab = tabLayout.newTab();
        directionsTab.setText("Directions");
        tabLayout.addTab(directionsTab);

        TabLayout.Tab ingredientsTab = tabLayout.newTab();
        ingredientsTab.setText("Ingredients");
        tabLayout.addTab(ingredientsTab);

        TabLayout.Tab nutritionTab = tabLayout.newTab();
        nutritionTab.setText("Nutrition");
        tabLayout.addTab(nutritionTab);

        binding.tvRecipe.setText(recipe.getRecipeName());
        Glide.with(this).load(recipe.getImage()).circleCrop().into(binding.ivFood);

    }


    public void onTabSelected(TabLayout.Tab tab) {
    // get the current selected tab's position and replace the fragment accordingly
        Fragment fragment;
        switch (tab.getPosition()) {
            case 0:
                fragment = new IngredientFragment();
                break;
            case 1:
                fragment = new DirectionFragment();
                break;
            case 2:
                fragment = new NutritionFragment();
                break;
            default:
                fragment = new IngredientFragment();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

}