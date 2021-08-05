package com.example.food4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.food4u.databinding.ActivityMainBinding;
import com.example.food4u.fragments.HomeFragment;
import com.example.food4u.fragments.MealFragment;
import com.example.food4u.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

    public static String REQUEST_URL = "https://api.edamam.com/api/recipes/v2?type=public&app_id=20517fda&app_key=56d94b548860a8480583b6eb00346efe";
    public static final String TAG = "MainActivity";
    public static final String TO_MEAL = TAG + "TO_MEAL";
    private String fromDetails;
    private Recipe recipe;
    private boolean mealAdded;

    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        fromDetails = getIntent().getStringExtra(TO_MEAL);
        recipe = (Recipe) getIntent().getSerializableExtra("RECIPE");
        mealAdded = getIntent().getBooleanExtra("MEAL_ADDED", false);
        Log.e(TAG, "mealadded value" + mealAdded);
        setIntent(null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.orange));

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = new HomeFragment();
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        toHomeFragment();
                        break;
                    case R.id.action_meal:
                        toMealFragment();
                        break;
                    case R.id.action_profile:
                        toProfileFragment();
                        break;
                    default:
                        toHomeFragment();
                        break;
                }
                return true;
            }
        });


        if (fromDetails == null) {
            binding.bottomNavigation.setSelectedItemId(R.id.action_home);
        } else {
            binding.bottomNavigation.setSelectedItemId(R.id.action_meal);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_item, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_current_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //no inspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            //this will now be null
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent i = new Intent(MainActivity.this, FrontActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.menu_current_search) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void toMealFragment() {
        fragmentManager.beginTransaction().replace(R.id.flContainer, new MealFragment(this.recipe, mealAdded)).commit();
        mealAdded = false;
    }

    public void toHomeFragment() {
        fragmentManager.beginTransaction().replace(R.id.flContainer, new HomeFragment()).commit();
    }

    public void toProfileFragment() {
        fragmentManager.beginTransaction().replace(R.id.flContainer, new ProfileFragment()).commit();
    }

}

