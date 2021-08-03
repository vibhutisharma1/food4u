package com.example.food4u;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ParseClassName("Recipe")
public class Meal extends ParseObject {

    private static final String TAG = "Meal" ;
    JSONObject nutritionObject;
    JSONArray ingredientsArray;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createObject(String label, String recipeURL, String image, String protein, String fat, String carbs, String calories ) {
        Meal entity = new Meal();
        if(nutritionObject == null && ingredientsArray == null){
            Log.e(TAG, "Something is null");
        }
        entity.put("label", label);
        entity.put("recipeUrl", recipeURL);
        entity.put("image", image);
        entity.put("user", ParseUser.getCurrentUser());
        entity.put("calories",calories );
        entity.put("carbs",carbs );
        entity.put("protein", protein );
        entity.put("fat", fat);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        entity.put("date", dtf.format(localDate));

        // Saves the new object.
        entity.saveInBackground(e -> {
            if (e==null){
                //Save was done
            }else{
                //Something went wrong
                Log.e(TAG, "did not save");
            }
        });

    }
    public void setIngredientsArray(JSONArray ingredients){
        this.ingredientsArray = ingredients;

    }
}
