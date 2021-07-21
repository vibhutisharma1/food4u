package com.example.food4u.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food4u.DetailsActivity;
import com.example.food4u.R;
import com.example.food4u.databinding.FragmentIngredientBinding;
import com.example.food4u.databinding.FragmentNutritionBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class NutritionFragment extends Fragment {

    FragmentNutritionBinding binding;

    public NutritionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNutritionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       String nutrition = DetailsActivity.recipe.getCalories();
       binding.tvNutrition.append(nutrition);
    }
}