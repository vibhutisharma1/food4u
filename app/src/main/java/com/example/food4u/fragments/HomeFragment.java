package com.example.food4u.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.food4u.HomeAdapter;
import com.example.food4u.MainActivity;
import com.example.food4u.Recipe;
import com.example.food4u.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    protected HomeAdapter adapter;
    protected List<Recipe> allRecipes;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Find RecyclerView and bind to adapter

        // allows for optimizations
        binding.rvPosts.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 2);

        //add the new health tags to url
        String healthTags = QuestionOne.healthStringTags;

        allRecipes = new ArrayList<>();

        //maybe set this info into a new string to avoid failure if crash
        //remove duplicate tags
        if (healthTags != null) {
            MainActivity.REQUEST_URL += healthTags + "&mealType=" + mealType();
        }

        // Create an adapter
        adapter = new HomeAdapter(getContext(), allRecipes);
        // Bind adapter to list
        binding.rvPosts.setAdapter(adapter);
        //set layout manager
        binding.rvPosts.setLayoutManager(layout);

        //get recipes based on health tags
        Recipe.retrieveFromAPI(MainActivity.REQUEST_URL, getContext(), allRecipes, adapter);

    }


    //displays recipes based on current time to get accurate mealTypes(breakfast, lunch, dinner)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String mealType() {
        //gets local time
        LocalTime myObj = LocalTime.now();
        int currentHour = myObj.getHour();

        if (6 <= currentHour  && currentHour <= 10) {
            //6am to 10am breakfast
            return "Breakfast";
        } else if (11 <= currentHour && currentHour <= 15){
            //11am to 3pm lunch
            return "Lunch";
        }else if(18 <= currentHour && currentHour <= 21 ){
            //6pm to 9pm dinner
            return "Dinner";
        }else {
            //otherwise late evening and midnight snack
            return "Snack";
        }
    }


}