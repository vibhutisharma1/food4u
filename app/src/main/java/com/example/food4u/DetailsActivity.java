package com.example.food4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.food4u.databinding.ActivityDetailsBinding;
import com.example.food4u.fragments.DirectionFragment;
import com.example.food4u.fragments.IngredientFragment;
import com.example.food4u.fragments.NutritionFragment;
import com.google.android.material.tabs.TabLayout;


public class DetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;
    public static Recipe recipe;
    private static final String TAG = "DetailsActivity";
    public static final String CURRENT_RECIPE = TAG + ".CurrentRecipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        recipe = (Recipe) getIntent().getExtras().getSerializable(CURRENT_RECIPE);

        // tab layout to navigate btwn: ingredients and directions
        TabLayout tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        TabLayout.Tab ingredientsTab = tabLayout.newTab();
        ingredientsTab.setText(R.string.Ingredients);

        tabLayout.addTab(ingredientsTab);

        TabLayout.Tab directionsTab = tabLayout.newTab();
        directionsTab.setText(R.string.Directions);
        tabLayout.addTab(directionsTab);

        TabLayout.Tab nutritionTab = tabLayout.newTab();
        nutritionTab.setText(R.string.Nutrition);
        tabLayout.addTab(nutritionTab);

        // perform setOnTabSelectedListener event on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = new IngredientFragment();
                switch (tab.getPosition()) {
                    case 2:
                        fragment = new NutritionFragment();
                        break;
                    case 1:
                        fragment = new DirectionFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
       //set initial fragment
        binding.tvRecipe.setText(recipe.getRecipeName());
        Glide.with(this).load(recipe.getImage()).circleCrop().fitCenter().into(binding.ivFood);

    }

}


