package com.example.food4u.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.food4u.DetailsActivity;
import com.example.food4u.R;
import com.example.food4u.RateActivity;
import com.example.food4u.Recipe;
import com.example.food4u.databinding.FragmentIngredientBinding;
import com.example.food4u.databinding.FragmentNutritionBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NutritionFragment extends Fragment {

    private static final String TAG = "NutritionFragment" ;
    FragmentNutritionBinding binding;
    Recipe recipe;

    public NutritionFragment() {
        this.recipe = null;
    }
    public NutritionFragment(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNutritionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //TODO: change font and size
      Map<String, String> nutrition = recipe.getNutrientMap();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("Rating", 3);
        //add favorite recipes into buttons
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "problem with getting the ratings");
                } else {
                    //nutrition: sat, unsat, sugar, calories, fiber, cholesterol
                    for (Map.Entry<String,String> entry : nutrition.entrySet()) {
                        Button fave = new Button(getContext());
                        //retrieve key and value pair
                        String key = dietLabel(entry.getKey());
                        if(key != ""){
                            String value = entry.getValue();
                            //set colors and background
                            fave.setAllCaps(false);
                            fave.setTextColor(getResources().getColor(R.color.orange));
                            fave.setBackgroundResource(R.drawable.background_yellow);
                            //set values and layout param
                            fave.append( key + ": " + value + " grams");
                            fave.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            //add to linear layout

                            binding.llNutrition.addView(fave);
                        }
                    }
                }

            }
        });

    }

    //convert from json labels to understandable nutrition labels
    public String dietLabel(String key){
        if(key.equals("ENERC_KCAL")){
            return "Energy";
        }else if(key.equals("FAT")){
            return "Total Fat";
        }else if(key.equals("FASAT")){
            return "Saturated Fat";
        }else if(key.equals("FAMS")){
            return "Monounsaturated Fat";
        }else if(key.equals("CHOCDF")){
            return "Polyunsaturated Fat";
        }else if(key.equals("FIBTG")){
            return "Fiber";
        }else if(key.equals("SUGAR")){
            return "Total Sugars";
        }
        return "";
    }

}