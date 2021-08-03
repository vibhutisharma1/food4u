package com.example.food4u;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.VH> {
    private Context mContext;
    private String [] health_buttons;
    private static final String TAG = "QuestionAdapter" ;

    public QuestionAdapter(Context context, String [] healthButtons) {
        mContext = context;
        if (healthButtons == null) {
            throw new IllegalArgumentException("recipes must not be null");
        }
        this.health_buttons = healthButtons;
    }

    // Inflate the view based on the viewType provided.
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health, parent, false);
        return new VH(itemView, mContext);
    }

    // Display data at the specified position
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        String health = health_buttons[position];
        if(health.equals("immuno-supportive")){
            Log.e(TAG, "inside immuno supprotive");
            holder.tvName.setTextSize(13);
        }
        if(health.equals("No-oil-added")){
            health = "no-oil-added";
        }
        holder.rootView.setTag(health);
        //set the recipe name
        holder.tvName.setText(health);
    }

    @Override
    public int getItemCount() {
        return health_buttons.length;
    }


    public class VH extends RecyclerView.ViewHolder {
        final View rootView;
        final TextView tvName;

        public VH(View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tvHealthName);

        }

    }


}