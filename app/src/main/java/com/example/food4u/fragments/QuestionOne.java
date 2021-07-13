package com.example.food4u.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.food4u.HealthLabel;
import com.example.food4u.MainActivity;
import com.example.food4u.R;
import com.example.food4u.databinding.FragmentQuestionOneBinding;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setBackgroundTintList;

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

//        String[] healthTags = {"alcoholFree","celeryFree", "dairyFree", "eggFree", "fishFree", "fodmapFree", "glutenFree",
//                "immunoSupportive", "ketoFriendly", "kidneyFriendly", "kosher","lowFatAbs", "lowPotassium","lowSugar",
//                "lupineFree", "mustardFree", "noOilAdded", "paleo", "peanutFree", "pecatarian", "porkFree", "redMeatFree",
//                "sesameFree", "shellfishFree", "soyFree", "sugarConscious", "treeNutFree", "vegan", "vegetarian", "wheatFree"};
        Button b = (Button)view;
        String buttonText = b.getText().toString();

       // b.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.));

        b.setBackground(Drawable.createFromPath("#37AEE4"));
//        android:background="#37AEE4"
//        android:backgroundTint="#FEFBDE"


        if(buttonText == "pescatarian"){
            buttonText = "pecatarian";
        }
        //create the health tag from what they clicked
        healthStringTags+= HealthLabel.apiHealthString(buttonText);


    }




}