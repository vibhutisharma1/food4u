package com.example.food4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food4u.databinding.ActivityDetailsBinding;
import com.example.food4u.fragments.DirectionFragment;
import com.example.food4u.fragments.IngredientFragment;
import com.example.food4u.fragments.MealFragment;
import com.example.food4u.fragments.NutritionFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity implements Serializable {

    ActivityDetailsBinding binding;
    private Recipe recipe;
    private static final String TAG = "DetailsActivity";
    public static final String CURRENT_RECIPE = TAG + ".CurrentRecipe";
    public static boolean mealAdded = false;
    public static ArrayList<Recipe> mealPlan = new ArrayList<>();
    GestureDetector gestureDetector;

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

        //add recipe to meal tab
        binding.btnMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.TO_MEAL, "MealFragment");
                intent.putExtra("RECIPE", recipe);
                startActivity(intent);
                mealPlan.add(recipe);
                mealAdded = true;
                Meal meal = new Meal();
                meal.createObject(recipe.getRecipeName(), recipe.getRecipeURL(), recipe.getImage(),
                        recipe.getProtein(), recipe.getFat(), recipe.getCarb(), recipe.getCalories());

            }
        });

        //like double click
        binding.likeStar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "Inside touch method");
                gestureDetector = new GestureDetector(DetailsActivity.this, new GestureListener());
                binding.likeStar.setPressed(true);
                binding.likeStar.setActivated(true);
                return gestureDetector.onTouchEvent(event);
            }
            class GestureListener extends GestureDetector.SimpleOnGestureListener {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                // event when double tap occurs
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    binding.likeStar.setPressed(true);
                    binding.likeStar.setActivated(true);
                    return true;
                }
            }

        });

        //set details view image and recipe name
        binding.tvRecipe.setText(recipe.getRecipeName());
        Glide.with(this).load(recipe.getImage()).circleCrop().fitCenter().into(binding.ivFood);

    }


}



