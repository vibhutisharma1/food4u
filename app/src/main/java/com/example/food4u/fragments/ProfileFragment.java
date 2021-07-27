package com.example.food4u.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food4u.FrontActivity;
import com.example.food4u.MainActivity;
import com.example.food4u.QuestionActivity;
import com.example.food4u.databinding.FragmentProfileBinding;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    public ProfileFragment() {
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                //this will now be null
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(getContext(), FrontActivity.class);
                startActivity(i);
            }
        });

        binding.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to Question Activity
                Intent intent = new Intent(getContext(), QuestionActivity.class);
                //reset request url to original
                MainActivity.REQUEST_URL = "https://api.edamam.com/api/recipes/v2?type=public&app_id=20517fda&app_key=56d94b548860a8480583b6eb00346efe";
                QuestionOne.healthStringTags = "";
                startActivity(intent);
            }
        });
    }


}