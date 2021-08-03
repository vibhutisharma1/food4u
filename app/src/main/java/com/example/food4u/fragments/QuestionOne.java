package com.example.food4u.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.food4u.HealthLabel;
import com.example.food4u.HomeAdapter;
import com.example.food4u.QuestionAdapter;
import com.example.food4u.R;
import com.example.food4u.databinding.FragmentQuestionOneBinding;

public class QuestionOne extends Fragment {

    FragmentQuestionOneBinding binding;
    public static String healthStringTags = "";
    private String[] health_tags;
    protected QuestionAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuestionOneBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GridLayoutManager layout = new GridLayoutManager(getContext(), 3);
        health_tags = new String[]{
                "alcohol-free",
                "immuno-supportive",
                "celery-free",
                "crustacean-free",
                "dairy-free",
                "egg-free",
                "fish-free",
                "fodmap-free",
                "gluten-free",
                "keto-friendly",
                "kidney-friendly",
                "kosher",
                "low-potassium",
                "lupine-free",
                "mustard-free",
                "No-oil-added",
                "low-sugar",
                "paleo",
                "peanut-free",
                "pecatarian",
                "pork-free",
                "red-meat-free",
                "sesame-free",
                "shellfish-free",
                "soy-free",
                "sugar-conscious",
                "tree-nut-free",
                "vegan",
                "vegetarian",
                "wheat-free"};

        // Create an adapter
        adapter = new QuestionAdapter(getContext(), health_tags);
        // Bind adapter to list
        binding.rvHealth.setAdapter(adapter);
        //set layout manager
        binding.rvHealth.setLayoutManager(layout);

    }


    public void buttonClicked(View view) {
        Button b = (Button) view;
        b.setBackgroundResource(R.drawable.clicked);
        String buttonText = b.getText().toString();

        if (buttonText == "no-oil-added") {
            buttonText = "pecatarian";
        }
        //create the health tag from what they clicked
        healthStringTags += HealthLabel.apiHealthString(buttonText);
    }


}