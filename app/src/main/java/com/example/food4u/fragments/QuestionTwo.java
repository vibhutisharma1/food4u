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
import com.example.food4u.databinding.FragmentQuestionTwoBinding;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

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
                PersonalInfo info = new PersonalInfo();
                //set the parse object variables
                info.setAge(Integer.parseInt(binding.etAge.getText().toString()));

                int feet = Integer.parseInt(binding.etFeet.getText().toString());
                int inches = Integer.parseInt(binding.etInch.getText().toString());
                info.setHeight(convertFtToCm(feet, inches));

                info.setHealth(QuestionOne.healthStringTags);
                info.setUser(ParseUser.getCurrentUser());
                info.setWeight(Integer.parseInt(binding.etLbs.getText().toString()));
                info.setActivity(binding.activityDropdown.getSelectedItem().toString());

                info.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Log.e(TAG, "Error while saving", e);
                            Toast.makeText(getContext(),"Error while saving!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "personal info save was successful :)");
                        binding.etLbs.setText(0);
                        binding.etFeet.setText(0);
                        binding.etInch.setText(0);
                        binding.etAge.setText(0);
                    }
                });
                goMainActivity();
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