package com.example.food4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.food4u.databinding.ActivityMainBinding;
import com.example.food4u.databinding.ActivityQuestionBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String REQUEST_URL = "https://api.edamam.com/api/recipes/v2?type=public&app_id=f19437bb&app_key=655d39c01f4f38804731f9996ab01ee8&health=vegan&dishType=Biscuits%20and%20cookies";
            //"https://api.edamam.com/api/recipes/v2?type=public&app_id=f19437bb&app_key=655d39c01f4f38804731f9996ab01ee8&health=vegan";
            //"https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=f19437bb&app_key=655d39c01f4f38804731f9996ab01ee8&diet=balanced&mealType=Dinner&imageSize=REGULAR";
            //  crustacean free doesnt work "https://api.edamam.com/api/recipes/v2?type=public&app_id=f19437bb&app_key=655d39c01f4f38804731f9996ab01ee8&health=alcohol-free&health=celery-free&health=crustacean-free&health=dairy-free&health=egg-free&cuisineType=Indian&mealType=Dinner";

    public static final String TAG = "MainActivity";

    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);


        //retrieve api
        RequestParams params = new RequestParams();
        params.put("limit", "20");
        params.put("page", 1);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(REQUEST_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("hits");
                    Log.i(TAG, "Results" + results.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
                Log.e(TAG, "Hit json exception", throwable);

            }
        });


        binding.bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_camera:
                        //fragment = new ComposeFragment();
                        break;
                    case R.id.action_home:
                        //fragment = new PostsFragment();
                        break;
                    case R.id.action_profile:
                    default:
                       // fragment = new ProfileFragment();
                        break;
                }
                //fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });

        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);

    }
}