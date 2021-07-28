package com.example.food4u.fragments;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.FileUtils;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.food4u.Ball;
import com.example.food4u.DetailsActivity;
import com.example.food4u.HomeAdapter;

import com.example.food4u.PersonalInfo;
import com.example.food4u.Recipe;

import com.example.food4u.databinding.FragmentMealBinding;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.food4u.DetailsActivity.mealAdded;


public class MealFragment extends Fragment implements Serializable {

    //recycler view variables
    protected HomeAdapter adapter;
    protected List<Recipe> allMeals;
    public static final String TAG = "MealFragment";
    public static final String ADD_PROGRESS = TAG + "ADD_PROGRESS";

    //shared variables
    public static double currentCalories;
    public static int currentProtein;
    public static int currentCarbs;
    public static int currentFat;
    public static FragmentMealBinding binding;
    float xPositionProtein;
    float xPositionFat;
    float xPositionCarbs;
    boolean bounceProteinLeft = false;
    boolean bounceCarbLeft = false;
    boolean bounceFatLeft = false;


    //local variables
    Recipe meal;
    Double totalCalories;
    // Screen Size
    int screenWidth;
    int screenHeight;

    //Initialize
    Timer timer = new Timer();
    Handler handler = new Handler();

    //Images
    ImageView addFat;
    ImageView addCarb;
    ImageView addProtein;

    //Position
    private float proteinX;
    private float proteinY;
    private float carbX;
    private float carbY;
    private float fatX;
    private float fatY;

    public static int protein;
    public static int fat;
    public static int carbs;

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
        binding.rvMeals.setHasFixedSize(true);

        // Define 1 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 1);

        meal = DetailsActivity.recipe;
        allMeals = new ArrayList<>();
        getCalories();

        addCarb = binding.addCarb;
        addFat = binding.addFat;
        addProtein = binding.addProtein;

        //set screen size coordinates
        Display currentWindow = getActivity().getWindowManager().getDefaultDisplay();
        Point coordinate = new Point();
        currentWindow.getSize(coordinate);
        screenWidth = coordinate.x;
        screenHeight = coordinate.y;

        // start from bottom of screen
        addCarb.setX(-80.0f);
        addCarb.setY(-80.0f);
        addProtein.setX(-80.0f);
        addProtein.setY(-80.0f);
        addFat.setX(-80.0f);
        addFat.setY(-80.0f);

        // Create and bind an adapter & set layout manager
        adapter = new HomeAdapter(getContext(), allMeals);
        binding.rvMeals.setAdapter(adapter);
        binding.rvMeals.setLayoutManager(layout);

        binding.proteinProgress.setProgress(protein);
        binding.fatProgress.setProgress(fat);
        binding.carbProgress.setProgress(carbs);

        //add the current recipe to the meal tab
        if (DetailsActivity.mealPlan != null) {
            allMeals.addAll(DetailsActivity.mealPlan);
            if (mealAdded) {
                //set x positions
                xPositionProtein = addProtein.getX();
                xPositionFat = addFat.getX();
                xPositionCarbs = addCarb.getX();
                // start timer
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                bounce();
                            }
                        });
                    }
                }, 0, 20);

                mealAdded = false;
            }else{
                binding.proteinProgress.setProgress(currentProtein);
                binding.fatProgress.setProgress(currentFat);
                binding.carbProgress.setProgress(currentCarbs);
            }
        }


        //display progress for calories
        binding.progressBar.setProgressPercentage(getPercentage(), true);

        //set max nutrient
        binding.proteinProgress.setMax((int) (.25 * totalCalories) / 4);
        binding.carbProgress.setMax((int) (.50 * totalCalories) / 4);
        binding.fatProgress.setMax((int) (.25 * totalCalories) / 4);

    }


    //retrieve total calories from parse server
    public void getCalories() {
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
    }

    //calculate the percentage of calories eaten
    public Double getPercentage() {
        return (currentCalories / totalCalories) * 100;
    }


    public void bounce() {
        Random random = new Random();

        //calculate a random x position between the start and end of image (within boundaries)
        int max = (Math.round(binding.proteinProgress.getX()) + (binding.proteinProgress.getWidth() / 2));
        int min = Math.round(binding.proteinProgress.getX());


        int initialProteinX = random.nextInt((max - min + 1) + min);
        xPositionProtein = getMotionProteinX(xPositionProtein, binding.proteinProgress, bounceProteinLeft);


        //protein movement
        proteinY -= 10;
        if (addProtein.getY() + addProtein.getHeight() < 0) {
            proteinX = initialProteinX;
            proteinY = screenHeight + 100.0f;
        }
        addProtein.setX(proteinX);
        addProtein.setY(proteinY);

        proteinX = (float) Math.ceil(xPositionProtein);
        addProtein.setX(proteinX);

        int initialCarbX = random.nextInt((Math.round(binding.carbProgress.getX())
                + (binding.carbProgress.getWidth() / 2)) - Math.round(binding.carbProgress.getX()) + 1)
                + Math.round(binding.carbProgress.getX());

        //carb movement
        carbY -= 10;
        if (addCarb.getY() + addCarb.getHeight() < 0) {
            carbX = initialCarbX;
            carbY = screenHeight + 100.0f;
        }
       ;
        addCarb.setX(carbX);
        addCarb.setY(carbY);

        xPositionCarbs = getMotionCarbX(xPositionCarbs, binding.carbProgress, bounceCarbLeft);

        carbX = (float) Math.ceil(xPositionCarbs);
        addCarb.setX(carbX);

        //fat movement
        int initialFatX = random.nextInt((Math.round(binding.fatProgress.getX())
                + (binding.fatProgress.getWidth() / 2)) - Math.round(binding.fatProgress.getX()) + 1)
                + Math.round(binding.fatProgress.getX());

        Log.e(TAG,  xPositionFat== xPositionCarbs? "true and" + xPositionFat : "false");


        fatY -= 10;

        if (addFat.getY() + addFat.getHeight() < 0) {
            fatX = initialFatX;
            Log.e(TAG, "intiial fat " + initialFatX);
            fatY = screenHeight + 100.0f;
        }
        addFat.setX(fatX);
        addFat.setY(fatY);

        if (bounceFatLeft) {
            xPositionFat -= 15;
        } else {
            xPositionFat += 15;
        }

        if (xPositionFat > (binding.fatProgress.getX() + binding.fatProgress.getWidth()) - 15) {
            bounceFatLeft = true;
        } else if (xPositionFat < binding.fatProgress.getX() + 15) {
            bounceFatLeft = false;
        }
        fatX = (float) Math.ceil(xPositionFat);
        addFat.setX(fatX);



        //checks if they collided
        if (collide(binding.proteinProgress, addProtein) || collide(binding.carbProgress, addCarb) || collide(binding.fatProgress, addFat)) {
            //update progress percent
            binding.proteinProgress.setProgress(currentProtein);
            binding.fatProgress.setProgress(currentFat);
            binding.carbProgress.setProgress(currentCarbs);

            //stop movement
            timer.cancel();
            timer = null;

            //hide images
            addCarb.setVisibility(View.INVISIBLE);
            addProtein.setVisibility(View.INVISIBLE);
            addFat.setVisibility(View.INVISIBLE);
        }

    }
    public float getMotionProteinX(float xPosition, CircleProgressBar currentProgressBar, boolean bounceLeft){

        if (bounceLeft) {
            xPosition -= 10;
        } else {
            xPosition += 10;
        }
        if (xPosition > (currentProgressBar.getX() + currentProgressBar.getWidth()) - 10) {
            bounceProteinLeft = true;
        } else if (xPosition < currentProgressBar.getX() + 10) {
            bounceProteinLeft = false;
        }
        return xPosition;
    }
    public float getMotionCarbX(float xPosition, CircleProgressBar currentProgressBar, boolean bounceLeft){

        if (bounceLeft) {
            xPosition -= 10;
        } else {
            xPosition += 10;
        }
        if (xPosition > (currentProgressBar.getX() + currentProgressBar.getWidth()) - 10) {
            bounceCarbLeft = true;
        } else if (xPosition < currentProgressBar.getX() + 10) {
            bounceCarbLeft = false;
        }

        return xPosition;
    }


    //calculates distance between two circles then checks if it is less than or equal to
    // half the circle to qualify it as being collided
    public boolean collide(CircleProgressBar circle, ImageView smallCircle) {
        return distance(circle.getX(), smallCircle.getX(), circle.getY(), smallCircle.getY())
                <= (circle.getWidth() / 2);
    }

    //distance formula
    public double distance(float x1, float x2, float y1, float y2) {
        return Math.sqrt(Math.abs(Math.pow((x1 - x2), 2)) + (Math.pow((y1 - y2), 2)));
    }

}