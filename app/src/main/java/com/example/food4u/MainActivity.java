package com.example.food4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.food4u.databinding.ActivityMainBinding;
import com.example.food4u.databinding.ActivitySearchBinding;
import com.example.food4u.fragments.HomeFragment;
import com.example.food4u.fragments.QuestionOne;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static String REQUEST_URL = "https://api.edamam.com/api/recipes/v2?type=public&app_id=f19437bb&app_key=131073dfbd2333ba6685e733f0a9ecb5";
    public static final String TAG = "MainActivity";
    SearchActivity current_search = new SearchActivity();
    protected List<Recipe> searchRecipes;

    ActivityMainBinding binding;
    final FragmentManager fragmentManager  = getSupportFragmentManager();
    //details view : nested fragments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        //toolbar
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        //toolbar.
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         //Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);


        //add the health tags to the URL if not null

        //maybe set this info into a new string to avoid failure if crash
        //remove duplicate tags

        binding.bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new HomeFragment();
                        break;
                }

               fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });

        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_item, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                String new_url = MainActivity.REQUEST_URL;
                if(query != null){
                    new_url+="&q=" + query;
                }
                current_search.retrieveFromAPI(new_url);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchItem.expandActionView();
        searchView.requestFocus();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


   // public class SearchActivity{

//        ActivitySearchBinding binding;
//        public static final String TAG = "PostsFragment";
//        protected HomeAdapter adapter;
//        protected List<Recipe> searchRecipes;
//
//        public SearchActivity(){
//
//        }
//
//        protected void onCreate(Bundle savedInstanceState) {
//            onCreate(savedInstanceState);
//            //binding
//            binding = ActivitySearchBinding.inflate(getLayoutInflater());
//            View v = binding.getRoot();
//            setContentView(v);
//
//            // allows for optimizations
//            binding.rvPosts.setHasFixedSize(true);
//
//            // Define 2 column grid layout
//            final GridLayoutManager layout = new GridLayoutManager(MainActivity.this, 2);
//
//            searchRecipes = new ArrayList<>();
//
//            // Create an adapter
//            adapter = new HomeAdapter(MainActivity.this, searchRecipes);
//            // Bind adapter to list
//            binding.rvPosts.setAdapter(adapter);
//            //set layout manager
//            binding.rvPosts.setLayoutManager(layout);
//
//        }
//
//
//        public void retrieveFromAPI(String url){
//            //retrieve api
//            RequestQueue recipeQueue = Volley.newRequestQueue(MainActivity.this);
//            //filter different pages
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        JSONArray results = response.getJSONArray("hits");
//                        Log.i(TAG, "Results" + results.toString());
//                        Log.i(TAG, "OnSuccess");
//                        processResults(results);
//                        adapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.i(TAG, "OnFailure");
//                    error.printStackTrace();
//
//                }
//            });
//            recipeQueue.add(jsonObjectRequest);
//        }
//
//        public void processResults(JSONArray response){
//            try {
//                for (int i = 0; i < response.length(); i++) {
//                    //gets specific hit
//                    JSONObject recipeJSON = response.getJSONObject(i);
//                    //goes into the recipe portion of hit
//                    JSONObject currentRecipe = recipeJSON.getJSONObject("recipe");
//                    String recipeName = currentRecipe.getString("label");
//                    String image = currentRecipe.getString("image");
//                    String recipeURL = currentRecipe.getString("url");
//                    String calories = Integer.toString(currentRecipe.getInt("calories"));
//                    String servings = Integer.toString(currentRecipe.getInt("yield"));
//
//                    //looks into an array of ingredients and add them to the recipe
//                    ArrayList<String> ingredients = new ArrayList<>();
//                    JSONArray ingredientList = currentRecipe.getJSONArray("ingredientLines");
//                    for (int j = 0; j < ingredientList.length(); j++) {
//                        ingredients.add(ingredientList.get(j).toString());
//                    }
//                    Recipe recipe = new Recipe(recipeName, image, recipeURL, ingredients, calories, servings);
//                    searchRecipes.add(recipe);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

}

