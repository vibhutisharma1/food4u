package com.example.food4u;

import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.VH> {
    private Context mContext;
    private List<Cuisine> cuisineList;
    private static final String TAG = "CuisineAdapter" ;

    public CuisineAdapter(Context context, List<Cuisine> cuisine) {
        mContext = context;
        if (cuisine == null) {
            throw new IllegalArgumentException("recipes must not be null");
        }
        this.cuisineList = cuisine;
    }


    // Inflate the view based on the viewType provided.
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuisine, parent, false);
        return new VH(itemView, mContext);
    }

    // Display data at the specified position
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        Cuisine cuisine = cuisineList.get(position);
        holder.rootView.setTag(cuisine);
        String name = cuisine.getCuisineName();

        //set the recipe name
        holder.tvCuisine.setText(name);
        // Instruct Glide to load the image
        Glide.with(mContext).load(cuisine.getDrawableName(name)).circleCrop().centerCrop().into(holder.ivCuisine);
    }
    @Override
    public int getItemCount() {
        System.out.println(cuisineList.size());
        return cuisineList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView ivCuisine;
        final TextView tvCuisine;

        public VH(View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            ivCuisine = (ImageView) itemView.findViewById(R.id.ivCuisine);
            tvCuisine = (TextView) itemView.findViewById(R.id.tvLabel);

            // Navigate to recipe details activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CuisinePage.class);
                    intent.putExtra("CUISINE_NAME", tvCuisine.getText().toString());
                    context.startActivity(intent);

                }
            });

        }

    }



}