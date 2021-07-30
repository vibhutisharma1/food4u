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
import com.example.food4u.Recipe;
import com.example.food4u.databinding.FragmentDirectionBinding;
import com.example.food4u.databinding.FragmentIngredientBinding;

import org.jetbrains.annotations.NotNull;


public class DirectionFragment extends Fragment {

    FragmentDirectionBinding binding;
    Recipe recipe;

    public DirectionFragment(Recipe recipe) {
        this.recipe = recipe;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDirectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        binding.wvRecipe.loadUrl(recipe.getRecipeURL());
    }
}