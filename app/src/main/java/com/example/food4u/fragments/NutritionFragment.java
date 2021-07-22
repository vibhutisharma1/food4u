package com.example.food4u.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food4u.DetailsActivity;
import com.example.food4u.R;
import com.example.food4u.databinding.FragmentIngredientBinding;
import com.example.food4u.databinding.FragmentNutritionBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


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
        //TODO: change font and size
      Map<String, String> nutrition = DetailsActivity.recipe.getNutrientMap();
        //get nutrition label : unsaturated fat, sugar
        //nutrition: sat, unsat, sugar, calories, fiber, cholestrol
        for (Map.Entry<String,String> entry : nutrition.entrySet()){
            TextView tv = new TextView(getActivity());
            //get nutrition label : unsaturated fat, sugar
            String key = entry.getKey();
            tv.setText(dietLabel(key));
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            //bad fats as red
            if(key.equals("FASAT")){
                tv.setTextColor(Color.parseColor("#E74C3C"));
            }
            //good fats as green
            if(key.equals("FAMS") || key.equals("FAPU")){
                tv.setTextColor(Color.parseColor("#27AE60"));
            }

            binding.llNutrition.addView(tv);
            TextView tvQuantity = new TextView(getActivity());
            //set it to the quantity
            tvQuantity.setText(entry.getValue() + " grams \n");
            binding.llNutrition.addView(tvQuantity);

        }

    }

    //convert from json labels to understandable nutrition labels
    public String dietLabel(String key){
        if(key.equals("ENERC_KCAL")){
            return "Energy";
        }else if(key.equals("FAT")){
            return "Total Fat";
        }else if(key.equals("FASAT")){
            return "Saturated Fat";
        }else if(key.equals("FAMS")){
            return "Monounsaturated Fat";
        }else if(key.equals("CHOCDF")){
            return "Polyunsaturated Fat";
        }else if(key.equals("FIBTG")){
            return "Fiber";
        }else{
            return "Total Sugars";
        }
    }

}