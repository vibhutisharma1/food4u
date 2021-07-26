package com.example.food4u.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.food4u.MainActivity;
import com.example.food4u.PersonalInfo;
import com.example.food4u.R;
import com.example.food4u.SignupPage;
import com.example.food4u.databinding.FragmentQuestionTwoBinding;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QuestionTwo extends Fragment {
    FragmentQuestionTwoBinding binding;
    public static final String TAG = "QuestionTwo";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuestionTwoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //dropdown menu items
        String[] items = new String[]{"Sedentary", "Light ", "Moderate", "Active"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one
        binding.activityDropdown.setAdapter(adapter);

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the parse object variables

                //retrieve object values
                int feet = Integer.parseInt(binding.etFeet.getText().toString());
                int inches = Integer.parseInt(binding.etInch.getText().toString());
                double height =  convertFtToCm(feet, inches);
                int age = Integer.parseInt(binding.etAge.getText().toString());
                int weight =  Integer.parseInt(binding.etLbs.getText().toString());
                String activity = binding.activityDropdown.getSelectedItem().toString();
                String health = QuestionOne.healthStringTags;

                if(SignupPage.SignupActivity){
                    //creating personalInfo for the first time when sign up
                    setObject(health, weight, activity, height, age);
                }else {
                    //update personalInfo object
                    updateObject(health, weight, activity, height, age);
                }
                goMainActivity();

            }
        });

    }

    public void setObject(String health, int weight, String activity, double height, int age){
        PersonalInfo info = new PersonalInfo();
        Log.e(TAG, " inside set object");
        info.setAge(age);
        info.setHeight(height);
        info.setHealth(health);
        info.setUser(ParseUser.getCurrentUser());
        info.setWeight(weight);
        info.setActivity(activity);
        info.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(),"Error while saving!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateObject(String health, int weight, String activity, double height, int age){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("personalInfo");

        query.include(PersonalInfo.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        Log.e(TAG, " inside update object");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                Log.d(TAG, "ParseObject: " + object);
                object.put("health", health);
                object.put("weight", weight);
                object.put("activity", activity);
                object.put("height", height);
                object.put("age", age);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //saved successfully
                            Log.e(TAG, "Information saved successfully!");
                            Toast.makeText(getContext(), "Information successfully saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            //save failed
                            Log.e(TAG, "Information unsuccessfully saved");
                        }
                    }
                });
            }
        });
    }

    //convert user ft and inches to cm
    public double convertFtToCm(int feet, int inches){
        return (2.54 * inches) + (30.48 * feet);
    }

    private void goMainActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
    }
}