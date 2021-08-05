package com.example.food4u;

import android.util.Log;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Favorite")
public class Favorite extends ParseObject {

    private final String TAG = "Favorite";

    public void createObject(String recipe, String url, float rating) {
        Favorite entity = new Favorite();

        entity.put("Recipe", recipe);
        entity.put("recipeURL", url);
        entity.put("Rating", rating);
        entity.put("user", ParseUser.getCurrentUser());

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        entity.saveInBackground(e -> {
            if (e==null){
                Log.e(TAG, "save was successful");
                //Save was done
            }else{
                //Something went wrong
               Log.e(TAG, "something went wrong");
            }
        });

    }
}
