package com.example.food4u;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.VH> {
    private Context mContext;
    private List<Recipe> recipeList;


    public HomeAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
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
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.rootView.setTag(recipe);
        //set the recipe name
        holder.tvName.setText(recipe.getRecipeName());
        // Instruct Glide to load the image
        Glide.with(mContext).load(recipe.getImage()).centerCrop().into(holder.ivFood);
    }

    @Override
    public int getItemCount() {
        System.out.println(recipeList.size());
        return recipeList.size();
    }

    // Provide a reference to the views for each contact item
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

        }


    }


}
