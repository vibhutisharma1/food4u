package com.example.food4u;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.food4u.fragments.QuestionOne;
import com.example.food4u.fragments.QuestionTwo;


public class QuestionActivity extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        //DepthPageTrasnformer Effect
        viewPager.setPageTransformer(new DepthPageTransformer());


    }


    public void onBackPressed(View v) {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void onNextPressed(View v) {
        if (viewPager.getCurrentItem() == 0) {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public void buttonClicked(View v) {
        Fragment q1 = new QuestionOne();
        ((QuestionOne) q1).buttonClicked(v);
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }


        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                Toast.makeText(QuestionActivity.this, "Slide page to continue", Toast.LENGTH_SHORT).show();
                return new QuestionOne();
            } else
                return new QuestionTwo();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }


}