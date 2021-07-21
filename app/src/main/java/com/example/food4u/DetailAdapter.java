package com.example.food4u;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.food4u.fragments.DirectionFragment;
import com.example.food4u.fragments.IngredientFragment;
import com.example.food4u.fragments.NutritionFragment;

public class DetailAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context mContext;

    public DetailAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        mContext = context;
        this.mNumOfTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 2) {
            return new NutritionFragment();
        } else if (position == 1) {
            return new DirectionFragment();
        } else {
            return new IngredientFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}