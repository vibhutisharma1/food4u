package com.example.food4u.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food4u.FrontActivity;
import com.example.food4u.MainActivity;
import com.example.food4u.QuestionActivity;
import com.example.food4u.RateActivity;
import com.example.food4u.databinding.FragmentProfileBinding;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    public final String TAG = "ProfileFragment";

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

        //set profile pic if there is one
        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("profile_image");
        if (profileImage != null) {
            Glide.with(requireContext()).load(profileImage.getUrl()).circleCrop().into(binding.ivProfile);
        }
        //set user's name
        String username = ParseUser.getCurrentUser().getUsername();
        binding.tvProfileName.setText(username);

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
                MainActivity main = new MainActivity();
                main.REQUEST_URL = "https://api.edamam.com/api/recipes/v2?type=public&app_id=20517fda&app_key=56d94b548860a8480583b6eb00346efe";
                QuestionOne.healthStringTags = "";
                startActivity(intent);
            }
        });

        binding.btEditPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "inside on blick edit picture");
                onPickPhoto(v);

            }
        });

        binding.btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RateActivity.class);
                startActivity(intent);
            }
        });
    }

    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2000);
            Log.e(TAG, "requested persmissons");
        } else {
            Log.e(TAG, " persmissons true start gallery");
            startGallery();
        }
    }

    private void startGallery() {
        Log.e(TAG, "inside start gallery");
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.e(TAG, "pick image");
        startActivityForResult(intent, PICK_PHOTO_CODE);

    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            Bitmap selectedImage = loadFromUri(photoUri);
            // Load the selected image into a preview
            //ivProfileImage.setImageBitmap(selectedImage);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 60, stream);
            byte[] dataImage = stream.toByteArray();

            //convert byte[] to parse file
            ParseFile file = new ParseFile("ProfileImage.png", dataImage);

            saveProfilePhoto(file);
        }
    }


    public void saveProfilePhoto(ParseFile photoFile) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Other attributes than "email" will remain unchanged!
            currentUser.put("profile_image", photoFile);
            Log.i(TAG, "Going good");

            // Saves the object.
            currentUser.saveInBackground(e -> {
                if (e == null) {
                    //Save successfull
                    Log.i(TAG, "Save Successful");
                    Toast.makeText(getContext(), "Save Successful", Toast.LENGTH_SHORT).show();
                    ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("profile_image");
                    if (profileImage != null) {
                        Glide.with(getContext()).load(profileImage.getUrl()).circleCrop().into(binding.ivProfile);
                    }
                } else {
                    // Something went wrong while saving
                    Log.i(TAG, "Save unsuccessful", e);
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}