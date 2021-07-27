package com.example.food4u.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.food4u.HealthLabel;
import com.example.food4u.R;
import com.example.food4u.databinding.FragmentQuestionOneBinding;

public class QuestionOne extends Fragment {

    FragmentQuestionOneBinding binding;
    public static String healthStringTags = "";


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


    }

    public void buttonClicked(View view) {
        Button b = (Button) view;
        b.setBackgroundResource(R.drawable.clicked);
        String buttonText = b.getText().toString();

        if (buttonText == "pescatarian") {
            buttonText = "pecatarian";
        }
        //create the health tag from what they clicked
        healthStringTags += HealthLabel.apiHealthString(buttonText);
    }


}