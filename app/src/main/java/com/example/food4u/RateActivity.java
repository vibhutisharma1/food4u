package com.example.food4u;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.food4u.databinding.ActivityRateBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RateActivity extends AppCompatActivity {

    private final String TAG = "RateActivity";
    private ActivityRateBinding binding;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            findAmazeRated();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            findMehRated();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            findNoRated();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void findAmazeRated() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("Rating", 3);
        //add favorite recipes into buttons
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "problem with getting the ratings");
                } else {
                    for (int i = 0; i < objects.size(); i++) {
                        Button fave = new Button(RateActivity.this);
                        //edit appearance
                        String label = objects.get(i).get("Recipe").toString();
                        url = objects.get(i).get("recipeURL").toString();
                        fave.setAllCaps(false);
                        fave.setText(label);
                        fave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });
                        fave.setTextColor(getResources().getColor(R.color.green));
                        fave.setBackgroundResource(R.drawable.background_green);
                        fave.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        //add to linear list
                        binding.llAmaze.addView(fave);
                    }
                }

            }
        });

    }


    public void findMehRated() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("Rating", 2);
        //add favorite recipes into buttons
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "problem with getting the ratings");
                } else {
                    for (int i = 0; i < objects.size(); i++) {
                        Button current = new Button(RateActivity.this);
                        //edit appearance
                        String label = objects.get(i).get("Recipe").toString();
                        url = objects.get(i).get("recipeURL").toString();
                        current.setAllCaps(false);
                        current.setText(label);
                        current.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });
                        current.setTextColor(getResources().getColor(R.color.orange));
                        current.setBackgroundResource(R.drawable.background_yellow);
                        current.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        //add to linear list
                        binding.llMeh.addView(current);
                    }
                }

            }
        });

    }

    public void findNoRated() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("Rating", 1);
        //add favorite recipes into buttons
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "problem with getting the ratings");
                } else {
                    for (int i = 0; i < objects.size(); i++) {
                        Button current = new Button(RateActivity.this);
                        //edit appearance
                        String label = objects.get(i).get("Recipe").toString();
                        url = objects.get(i).get("recipeURL").toString();
                        current.setAllCaps(false);
                        current.setText(label);
                        current.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });
                        current.setTextColor(getResources().getColor(R.color.red));
                        current.setBackgroundResource(R.drawable.background_red);
                        current.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        //add to linear list
                        binding.llNope.addView(current);
                    }
                }

            }
        });

    }

}