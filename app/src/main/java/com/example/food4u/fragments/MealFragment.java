package com.example.food4u.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;


import com.example.food4u.DetailsActivity;
import com.example.food4u.HomeAdapter;

import com.example.food4u.PersonalInfo;
import com.example.food4u.Recipe;

import com.example.food4u.databinding.FragmentMealBinding;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;


public class MealFragment extends Fragment implements Serializable {

    public static FragmentMealBinding binding;
    protected HomeAdapter adapter;
    protected List<Recipe> allMeals;
    public static final String TAG = "MealFragment";
    public static final String CURRENT_MEAL = TAG + "currentMeal";
    Recipe meal;
    Double totalCalories;
    public static double currentCalories;


    public MealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMealBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Find RecyclerView and bind to adapter
        // allows for optimizations
        binding.rvMeals.setHasFixedSize(true);

        // Define 1 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 1);

        meal = DetailsActivity.recipe;
        allMeals = new ArrayList<>();

        // Create an adapter
        adapter = new HomeAdapter(getContext(), allMeals);
        // Bind adapter to list
        binding.rvMeals.setAdapter(adapter);
        //set layout manager
        binding.rvMeals.setLayoutManager(layout);

        //add the current recipe to the meal tab
        if (DetailsActivity.mealPlan != null) {
            allMeals.addAll(DetailsActivity.mealPlan);
        }

        //gets user's total calories
        ParseQuery<ParseObject> query = ParseQuery.getQuery("personalInfo");
        query.include(PersonalInfo.KEY_USER);
        query.include(PersonalInfo.KEY_CALORIE);
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        //store calories into local variable
        try {

            totalCalories = Double.parseDouble(query.getFirst().get("calories").toString());
            Log.e(TAG, "total calories " + totalCalories);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "did not retrieve calories");
        }

        //display progress for calories
        binding.progressBar.setProgressPercentage(getPercentage(), true);

        if(DetailsActivity.mealAdded){
            startCarbAnimation();
        }



    }

    //calculate the percentage of calories eaten
    public Double getPercentage() {
        return (currentCalories / totalCalories) * 100;
    }

    public void startCarbAnimation(){
        binding.ivCarbs.clearAnimation();
        TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0, getDisplayHeight()/2);
        transAnim.setStartOffset(500);
        transAnim.setDuration(3000);
        transAnim.setFillAfter(true);
        transAnim.setInterpolator(new BounceInterpolator());
        transAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "Starting button dropdown animation");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG,
                        "Ending button dropdown animation. Clearing animation and setting layout");
                binding.ivCarbs.clearAnimation();
                final int left = binding.ivCarbs.getLeft();
                final int top = binding.ivCarbs.getTop();
                final int right = binding.ivCarbs.getRight();
                final int bottom = binding.ivCarbs.getBottom();
                binding.ivCarbs.layout(left, top, right, bottom);

            }
        });
        binding.ivCarbs.startAnimation(transAnim);

    }

    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }




}