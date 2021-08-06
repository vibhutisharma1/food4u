package com.example.food4u.fragments;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.food4u.Circle;
import com.example.food4u.HomeAdapter;


import com.example.food4u.Meal;
import com.example.food4u.PersonalInfo;
import com.example.food4u.R;
import com.example.food4u.Recipe;

import com.example.food4u.databinding.FragmentMealBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MealFragment extends Fragment implements Serializable {

    //recycler view variables
    protected HomeAdapter adapter;
    protected List<Recipe> allMeals;
    public static final String TAG = "MealFragment";
    public static final String SEND_RECIPE = TAG + "SEND_RECIPE";

    private FragmentMealBinding binding;
    float xPositionProtein;
    float xPositionFat;
    float xPositionCarbs;
    boolean bounceProteinLeft = false;
    boolean bounceCarbLeft = false;
    boolean bounceFatLeft = false;
    private String currentDate;
    private boolean mealAdded;
    private List<String> oldNames;
    private boolean nextDay;

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

    //Circles
    Circle proteinCircle;
    Circle fatCircle;
    Circle carbCircle;

    //Nutrition values
    private double caloriesBefore;
    private double currentCalories;
    private int currentProtein;
    private int currentCarbs;
    private int currentFat;
    private int protein;
    private int fat;
    private int carbs;

    public MealFragment() {
        // Required empty public constructor
    }

    public MealFragment(Recipe recipe, boolean mealAdded) {
        this.meal = recipe;
        this.mealAdded = mealAdded;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.e(TAG, "gets bundle ");
            meal = (Recipe) savedInstanceState.getSerializable(SEND_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentMealBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Find RecyclerView and bind to adapter
        binding.rvMeals.setHasFixedSize(true);
        allMeals = new ArrayList<>();
        oldNames = new ArrayList<>();
        getCalories();

        //set circles aka image views
        addCarb = binding.addCarb;
        addFat = binding.addFat;
        addProtein = binding.addProtein;

        getScreenSize();
        setCirclesOutOfScreen();


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        currentDate = dtf.format(localDate);

        try {
            loadOldMeals();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //clear calories and macro info when its a new day
        if (nextDay) {
            resetValues();
        }
        //add the current recipe to the meal tab
        if (meal != null) {
            if (mealAdded) {
                allMeals.add(meal);
                String currentName = meal.getRecipeName();
                oldNames.add(currentName);
                Log.e(TAG, currentName);
                updateNewProgressBars();
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

                // send to meal to store in parse server
                Meal newMeal = new Meal();
                newMeal.createObject(meal.getRecipeName(), meal.getRecipeURL(), meal.getImage(),
                        meal.getProtein(), meal.getFat(), meal.getCarb(), meal.getCalories());
                mealAdded = false;
            } else {
                updateProgressBars();
            }
        }


        //set max nutrient
        binding.proteinProgress.setMax((int) (.25 * totalCalories) / 4);
        binding.carbProgress.setMax((int) (.50 * totalCalories) / 4);
        binding.fatProgress.setMax((int) (.25 * totalCalories) / 4);

        //remove recipe on long click
        HomeAdapter.OnLongClickListener onLongClickListener = new HomeAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                Recipe current = allMeals.get(position);
                //subtract nutrition
                currentCalories -= Integer.parseInt(current.getCalories());
                caloriesBefore -= Integer.parseInt(current.getCalories());
                protein -= Integer.parseInt(current.getProtein());
                currentProtein -= Integer.parseInt(current.getProtein());
                fat -= Integer.parseInt(current.getFat());
                currentFat -= Integer.parseInt(current.getFat());
                carbs -= Integer.parseInt(current.getCarb());
                currentCarbs -= Integer.parseInt(current.getCarb());

                //update progress
                updateProgressBars();

                allMeals.remove(current);
                adapter.notifyItemRemoved(position);
                Toast.makeText(getContext(), "Item was removed", Toast.LENGTH_SHORT).show();

            }
        };


        // Define 1 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 1);
        // Create and bind an adapter & set layout manager
        adapter = new HomeAdapter(getContext(), allMeals, onLongClickListener);
        binding.rvMeals.setAdapter(adapter);
        binding.rvMeals.setLayoutManager(layout);
    }

    public void updateNewProgressBars() {
        double percent = getPercentage(caloriesBefore);
        if (percent >= 85) {
            binding.progressBar.setTextPadding(220);
        }
        binding.progressBar.setProgressPercentage(percent, true);
        binding.proteinProgress.setProgress(protein);
        binding.fatProgress.setProgress(fat);
        binding.carbProgress.setProgress(carbs);
    }

    public void updateProgressBars() {
        double percent = getPercentage(currentCalories);
        if (percent >= 85) {
            binding.progressBar.setTextPadding(220);
        }
        binding.progressBar.setProgressPercentage(percent, true);
        binding.proteinProgress.setProgress(currentProtein);
        binding.fatProgress.setProgress(currentFat);
        binding.carbProgress.setProgress(currentCarbs);
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
    public Double getPercentage(double currentCalories) {
        double percentage = (currentCalories / totalCalories) * 100;
        return percentage;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bounce() {
        //start circle animation but dont animate if no nutrition
        proteinCircle = new Circle(addProtein.getX(), addProtein.getY());
        if (Integer.parseInt(meal.getProtein()) != 0) {
            circleMovement(proteinCircle, addProtein, binding.proteinProgress);
        }

        carbCircle = new Circle(addCarb.getX(), addCarb.getY());
        if (Integer.parseInt(meal.getCarb()) != 0) {
            circleMovement(carbCircle, addCarb, binding.carbProgress);
        }

        fatCircle = new Circle(addFat.getX(), addFat.getY());
        if (Integer.parseInt(meal.getFat()) != 0) {
            circleMovement(fatCircle, addFat, binding.fatProgress);
        }

        //checks if they collided/overlap
        if (isCollision(binding.proteinProgress, addProtein) || isCollision(binding.carbProgress, addCarb) || isCollision(binding.fatProgress, addFat)) {
            setNutritionFacts();
            updateProgressBars();

            //stop movement
            timer.cancel();
            timer = null;

            //hide images
            addCarb.setVisibility(View.INVISIBLE);
            addProtein.setVisibility(View.INVISIBLE);
            addFat.setVisibility(View.INVISIBLE);
        }

    }

    public void circleMovement(Circle current, ImageView ball, CircleProgressBar progressBar) {
        //set initial x to stay within diameter of circle
        current.setInitialX(progressBar.getX(), progressBar.getWidth() / 2);

        //move ball up 10 pixels
        current.updateY(-10);
        if (ball.getY() + ball.getHeight() < 0) {
            //when out of screen set x boundary and move y up in screen
            current.setX(current.getInitialX());
            current.setY(screenHeight + 100.0f);
        }
        ball.setX(current.getX());
        ball.setY(current.getY());

        //change x pos to left or right
        if (ball.equals(addCarb)) {
            xPositionCarbs = getMotionCarbX(xPositionCarbs, progressBar, bounceCarbLeft);
            //update x position
            current.setX((float) Math.ceil(xPositionCarbs));
        } else if (ball.equals(addProtein)) {
            xPositionProtein = getMotionProteinX(xPositionProtein, progressBar, bounceProteinLeft);
            //update x position
            current.setX((float) Math.ceil(xPositionProtein));
        } else {
            xPositionFat = getMotionFatX(xPositionFat, progressBar, bounceFatLeft);
            fatCircle.setX((float) Math.ceil(xPositionFat));
        }

        ball.setX(current.getX());

    }


    //based on bounceLeft boolean alter x position +10 or -10
    public float getMotionCarbX(float xPosition, CircleProgressBar currentProgressBar, boolean bounceLeft) {
        if (bounceLeft) {
            xPosition -= 10;
        } else {
            xPosition += 10;
        }
        if (xPosition > (currentProgressBar.getX() + currentProgressBar.getWidth()) - 10) {
            //when x coordinate is greater than the circles subtract 10 next time
            bounceCarbLeft = true;
        } else if (xPosition < currentProgressBar.getX() + 10) {
            //add 10 next time because to make ball stay to the right
            bounceCarbLeft = false;
        }

        return xPosition;
    }

    public float getMotionProteinX(float xPosition, CircleProgressBar currentProgressBar, boolean bounceLeft) {
        if (bounceLeft) {
            xPosition -= 10;
        } else {
            xPosition += 10;
        }
        if (xPosition > (currentProgressBar.getX() + currentProgressBar.getWidth()) - 10) {
            //when x coordinate is greater than the circles subtract 10 next time
            bounceProteinLeft = true;
        } else if (xPosition < currentProgressBar.getX() + 10) {
            //add 10 next time because to make ball stay to the right
            bounceProteinLeft = false;
        }

        return xPosition;
    }

    public float getMotionFatX(float xPosition, CircleProgressBar currentProgressBar, boolean bounceLeft) {
        //bounce left and right
        if (bounceLeft) {
            xPosition -= 15;
        } else {
            xPosition += 15;
        }

        if (xPosition > (currentProgressBar.getX() + currentProgressBar.getWidth()) - 15) {
            //when x coordinate is greater than the circles subtract 15 next time
            bounceFatLeft = true;
        } else if (xPosition < currentProgressBar.getX() + 15) {
            //add 15 next time because to make ball stay to the right
            bounceFatLeft = false;
        }

        return xPosition;
    }

    //calculates distance between two circles then checks if it is less than or equal to
    // half the circle to qualify it as being collided
    public boolean isCollision(CircleProgressBar circle, ImageView smallCircle) {
        return distance(circle.getX(), smallCircle.getX(), circle.getY(), smallCircle.getY())
                <= (circle.getWidth() / 2);
    }

    //distance formula
    public double distance(float x1, float x2, float y1, float y2) {
        return Math.sqrt(Math.abs(Math.pow((x1 - x2), 2)) + (Math.pow((y1 - y2), 2)));
    }

    public void setCirclesOutOfScreen() {
        // start from bottom of screen
        addCarb.setX(-80.0f);
        addCarb.setY(-80.0f);
        addProtein.setX(-80.0f);
        addProtein.setY(-80.0f);
        addFat.setX(-80.0f);
        addFat.setY(-80.0f);
    }

    public void getScreenSize() {
        //get screen size coordinates
        Display currentWindow = getActivity().getWindowManager().getDefaultDisplay();
        Point coordinate = new Point();
        currentWindow.getSize(coordinate);
        screenWidth = coordinate.x;
        screenHeight = coordinate.y;
    }

    public void setNutritionFacts() {
        //update previous to current
        caloriesBefore = currentCalories;
        carbs = currentCarbs;
        protein = currentProtein;
        fat = currentFat;

        //add new nutrition
        currentCalories += Double.parseDouble(meal.getCalories()) / Integer.parseInt(meal.getServings());
        currentProtein += Integer.parseInt(meal.getProtein());
        currentCarbs += Integer.parseInt(meal.getCarb());
        currentFat += Integer.parseInt(meal.getFat());
    }

    //uses the parse server to get any added meals on their profile
    public void loadOldMeals() throws ParseException {
        //get old meals for this user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
        query.include(PersonalInfo.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        //add to the arraylist and display
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "problem with loading recipes");
                } else {
                    List<Recipe> oldRecipes = new ArrayList();
                    for (int i = 0; i < objects.size(); i++) {
                        String date = objects.get(i).get("date").toString();
                        String label = objects.get(i).get("label").toString();
                        Log.e(TAG, "old recipes label" + label);
                        if (date.equals(currentDate) && !oldNames.contains(label)) {
                            //get all attributes
                            String recipe = objects.get(i).get("recipeUrl").toString();
                            String image = objects.get(i).get("image").toString();

//                            //update nutrition information

                            currentCalories += Double.parseDouble(objects.get(i).get("calories").toString());
                            currentProtein += Integer.parseInt(objects.get(i).get("protein").toString());
                            currentCarbs += Integer.parseInt(objects.get(i).get("carbs").toString());
                            currentFat += Integer.parseInt(objects.get(i).get("fat").toString());

                            Recipe current = new Recipe(label, image, recipe);
                            oldRecipes.add(current);
                        } else {
                            nextDay = true;
                        }
                    }
                    //update adapter and allMeals
                    allMeals.addAll(oldRecipes);
                    updateProgressBars();
                    adapter.notifyDataSetChanged();

                }

            }
        });
    }


    //at the end of the days clear values
    public void resetValues() {
        currentFat = 0;
        caloriesBefore = 0.0;
        currentCalories = 0.0;
        currentProtein = 0;
        currentCarbs = 0;
        protein = 0;
        fat = 0;
        carbs = 0;
    }


}