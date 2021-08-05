package com.example.food4u;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food4u.databinding.ActivityDetailsBinding;
import com.example.food4u.fragments.DirectionFragment;
import com.example.food4u.fragments.IngredientFragment;
import com.example.food4u.fragments.NutritionFragment;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements Serializable {

    private ActivityDetailsBinding binding;
    private Recipe recipe;
    private static final String TAG = "DetailsActivity";
    public static final String CURRENT_RECIPE = TAG + ".CurrentRecipe";
    private TabLayout tabLayout;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private boolean mealAdded;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        recipe = (Recipe) getIntent().getExtras().getSerializable(CURRENT_RECIPE);

        // tab layout to navigate btwn: ingredients and directions
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

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
                // get the current selected tab's position
                Fragment fragment = new IngredientFragment(recipe);
                switch (tab.getPosition()) {
                    case 2:
                        fragment = new NutritionFragment(recipe);
                        break;
                    case 1:
                        fragment = new DirectionFragment(recipe);
                        break;
                }
                //replace the fragment accordingly
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //add recipe to meal tab
        binding.btnMeal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //send recipe information to mealFragment via MainActivity
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.TO_MEAL, "MealFragment");
                intent.putExtra("RECIPE", recipe);
                mealAdded = true;
                intent.putExtra("MEAL_ADDED", mealAdded);
                startActivity(intent);
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //add diet labels
        ArrayList<String> dietLabels = recipe.getDietLabels();
        for (int i = 0; i < dietLabels.size(); i++) {
            // Create labels dynamically
            Button diet = new Button(this);
            //edit appearance
            String label = dietLabels.get(i).toLowerCase();
            diet.setAllCaps(false);
            diet.setText(label);
            diet.setTextColor(getResources().getColor(R.color.green));
            diet.setBackgroundResource(R.drawable.background_green);
            diet.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            //add to linear list
            if (i <= 1) {
                binding.lldietLabels.addView(diet);
            } else {
                binding.lldietTwo.addView(diet);
            }
        }


        //save rating in parse server
        Favorite favoriteRecipe = new Favorite();
        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = binding.foodRating.getRating();
                favoriteRecipe.createObject(recipe.getRecipeName(), recipe.getRecipeURL(), rating);
                Toast.makeText(DetailsActivity.this, "Rating saved",Toast.LENGTH_SHORT).show();
            }
        });

        //change color of star
        LayerDrawable stars = (LayerDrawable) binding.foodRating.getProgressDrawable();
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        //set details view image and recipe name
        binding.tvRecipe.setText(recipe.getRecipeName());
        Glide.with(this).load(recipe.getImage()).circleCrop().fitCenter().into(binding.ivFood);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //select ingredients tab
        tabLayout.getTabAt(0).select();
        Fragment fragment = new IngredientFragment(recipe);
        //set up fragments
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        //go to ingredients fragment
        ft.replace(R.id.simpleFrameLayout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

    }


}
