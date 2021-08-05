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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.VH> {
    private Context mContext;
    private List<Recipe> recipeList;
    private static final String TAG = "HomeAdapter" ;
    OnLongClickListener longClickListener;
    private boolean red;
    private boolean yellow;
    private boolean green;

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }
    public HomeAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
        if (recipes == null) {
            throw new IllegalArgumentException("recipes must not be null");
        }
        recipeList = recipes;
    }

    public HomeAdapter(Context context, List<Recipe> recipes,  OnLongClickListener longClickListener) {
        mContext = context;
        this.longClickListener = longClickListener;
        if (recipes == null) {
            throw new IllegalArgumentException("recipes must not be null");
        }
        recipeList = recipes;
    }


    // Inflate the view based on the viewType provided.
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new VH(itemView, mContext);
    }

    // Display data at the specified position
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.rootView.setTag(recipe);
        //find rating value
        String name = recipe.getRecipeName();
        try {
            checkIfRated(name);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //set the recipe name
        holder.tvName.setText(name);
        // Instruct Glide to load the image
        Glide.with(mContext).load(recipe.getImage()).centerCrop().into(holder.ivFood);
    }

    @Override
    public int getItemCount() {
        System.out.println(recipeList.size());
        return recipeList.size();
    }
    public void checkIfRated(String name) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("Recipe", name);
        if(query.getFirst() != null){
            int rating = Integer.parseInt(query.getFirst().get("Rating").toString());
            if(rating == 1){
                red = true;
            }else if(rating == 2){
                green = true;
            }else if(rating == 3){
                yellow = true;
            }
        }

    }


    public class VH extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView ivFood;
        final TextView tvName;
        final View vPalette;

        public VH(View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            ivFood = (ImageView) itemView.findViewById(R.id.ivFood);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            vPalette = itemView.findViewById(R.id.vPalette);

            // Navigate to recipe details activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Recipe recipe = (Recipe) v.getTag();
                    if (recipe != null) {
                        // Fire an intent when a recipe is selected
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra(DetailsActivity.CURRENT_RECIPE, (Serializable) recipe);
                        context.startActivity(intent);

                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //notify listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

        }

    }



}