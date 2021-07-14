package com.example.food4u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;

import com.bumptech.glide.request.target.CustomTarget;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.VH> {
    private Context mContext;
    private List<Recipe> recipeList;
    private Palette palette;


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
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new VH(itemView, mContext);
    }

    // Display data at the specified position
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.rootView.setTag(recipe);
        holder.tvName.setText(recipe.getRecipeName());
       // Glide.with(mContext).load(recipe.getImage()).into(holder.ivFood);
//        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
//
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                // TODO 1. Instruct Glide to load the bitmap into the `holder.ivProfile` profile image view
//                //made holder final...not sure if necessary or supposed to...
//
//
//
//                // TODO 2. Use generate() method from the Palette API to get the vibrant color from the bitmap
//                // Set the result as the background color for `holder.vPalette` view containing the contact's name.
//                Palette color = palette.from(resource).generate();
//
//                Palette.Swatch vibrant = color.getVibrantSwatch();
//                if (color != null) {
//                    holder.vPalette.setBackgroundColor(vibrant.getRgb());
//                }
//            }
//
//            @Override
//            public void onLoadCleared(@Nullable Drawable placeholder) {
//                // can leave empty
//            }
//        };
        // TODO: Clear the bitmap and the background color in adapter


        // Instruct Glide to load the bitmap into the asynchronous target defined above
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

            // Navigate to contact details activity on click of card view.
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final Recipe contact = (Recipe) v.getTag();
//                    if (contact != null) {
//                        // Fire an intent when a contact is selected
//                        Intent intent = new Intent(context, DetailsActivity.class);
//                        intent.putExtra(DetailsActivity.EXTRA_CONTACT, contact);
//                        Pair<View, String> p1 = Pair.create((View) ivProfile, "profile");
//                        Pair<View, String> p2 = Pair.create(vPalette, "palette");
//                        Pair<View, String> p3 = Pair.create((View) tvName, "text");
//                        ActivityOptionsCompat options = ActivityOptionsCompat.
//                                makeSceneTransitionAnimation(mContext, p1, p2, p3);
//                        context.startActivity(intent, options.toBundle());
//
//                        //context.startActivity(intent);
//                        // Pass contact object in the bundle and populate details activity.
//
//                    }
//                }
//            });
        }


    }


}
