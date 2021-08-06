package com.example.food4u.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.food4u.Cuisine;
import com.example.food4u.CuisineAdapter;
import com.example.food4u.HomeAdapter;
import com.example.food4u.MainActivity;
import com.example.food4u.PersonalInfo;
import com.example.food4u.Recipe;
import com.example.food4u.databinding.FragmentHomeBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    protected HomeAdapter adapter;
    protected List<Recipe> allRecipes;
    protected List<Cuisine> allCuisines;
    protected CuisineAdapter adapterCuisine;

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
        binding.rvCuisine.setHasFixedSize(true);
        //set profile pic if there is one
        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("profile_image");
        if (profileImage != null) {
            Glide.with(requireContext()).load(profileImage.getUrl()).circleCrop().into(binding.ivPfp);
        }
        binding.tvname.append(ParseUser.getCurrentUser().get("firstName") + " " + ParseUser.getCurrentUser().get("lastName") +"!");

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 2);
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        //add the new health tags to url
        String healthTags = QuestionOne.healthStringTags;

        allRecipes = new ArrayList<>();
        allCuisines = new ArrayList<>();

        //get their health tag
        ParseQuery<ParseObject> query = ParseQuery.getQuery("personalInfo");

        query.include(PersonalInfo.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        MainActivity main = new MainActivity();

        if (healthTags != null) {
            try {
                main.REQUEST_URL += query.getFirst().getString("health");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //another url to hold mealType to keep separate from main request url for search
        String mealAddition = main.REQUEST_URL;
        mealAddition += "&mealType=" + mealType();
        Log.e(TAG,mealAddition);

        // Create an adapter
        adapter = new HomeAdapter(getContext(), allRecipes);
        adapterCuisine = new CuisineAdapter(getContext(), allCuisines);
        // Bind adapter to list
        binding.rvPosts.setAdapter(adapter);
        binding.rvCuisine.setAdapter(adapterCuisine);
        //set layout manager
        binding.rvPosts.setLayoutManager(layout);
        binding.rvCuisine.setLayoutManager(horizontalLayout);

        //get recipes based on health tags
        Recipe.retrieveFromAPI(mealAddition, getContext(), allRecipes, adapter);
        setAllCuisines();
    }
    public void setAllCuisines(){
        String [] cuisines = {"American", "Asian", "Chinese", "Indian", "Italian",
                "Japanese", "Mexican", "Middle Eastern"};
        for(int x = 0; x < cuisines.length; x++){
            Cuisine cuisine = new Cuisine(cuisines[x]);
            allCuisines.add(cuisine);
        }

    }



    //displays recipes based on current time to get accurate mealTypes(breakfast, lunch, dinner)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String mealType() {
        //gets local time
        LocalTime myObj = LocalTime.now();
        int currentHour = myObj.getHour();

        if (6 <= currentHour && currentHour <= 11)
            //6am to 12pm breakfast
            return "Breakfast";
        else if (12 <= currentHour && currentHour <= 15)
            //11am to 3pm lunch
            return "Lunch";
        else if (18 <= currentHour && currentHour <= 21)
            //6pm to 9pm dinner
            return "Dinner";
        else
            //otherwise late evening and midnight snack
            return "Snack";

    }

}