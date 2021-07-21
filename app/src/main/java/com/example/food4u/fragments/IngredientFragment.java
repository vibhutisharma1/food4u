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
import com.example.food4u.databinding.FragmentHomeBinding;
import com.example.food4u.databinding.FragmentIngredientBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class IngredientFragment extends Fragment {
    FragmentIngredientBinding binding;

    public IngredientFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIngredientBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList<String> ingredients = DetailsActivity.recipe.getIngredients();
        for(int i = 0; i < ingredients.size(); i++){
            binding.tvIngredients.append(ingredients.get(i));
        }
//        Map<String,String> currentIngredients = DetailsActivity.recipe.getIngredientList();
        // Get keys and values
//        for (Map.Entry<String, String> entry : currentIngredients.entrySet()) {
//            String k = entry.getKey();
//            String v = entry.getValue();
//           binding.tvIngredients.append(k);
//           binding.tvIngredients.append(v);
//        }

    }
}