package com.schneewittchen.rosandroid.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.VideoView;

import com.google.android.material.tabs.TabLayout;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.helper.IntroViewPagerAdapter;
import com.schneewittchen.rosandroid.ui.helper.ScreenItem;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 19.06.20
 * @updated on
 * @modified by
 */

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button buttonNext;
    int position = 0;
    Button buttonGetStarted;
    Animation buttonAnimation;
    VideoView videoView;

    String configName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Check wether the user already checked in
        if(restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }


        setContentView(R.layout.activity_intro);


        // Init Views
        buttonNext = findViewById(R.id.onboarding_btn_next);
        buttonGetStarted = findViewById(R.id.onboarding_btn_getStarted);
        tabIndicator = findViewById(R.id.tabIndicator);
        videoView = findViewById(R.id.onboarding_video_view);
        buttonAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.onboarding_buttton_animation);

        // Fill the list
        List<ScreenItem> mList = new ArrayList<>();
        String[] title_array = getResources().getStringArray(R.array.intro_title);
        String[] descr_array = getResources().getStringArray(R.array.intro_descr);
        TypedArray img_array = getResources().obtainTypedArray(R.array.intro_img);
        for(int i=0; i<title_array.length; i++) {
            mList.add(new ScreenItem(title_array[i], descr_array[i], img_array.getResourceId(i,-1)));
        }

        // Set the video
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro_video);
        videoView.setVideoURI(uri);

        // Setup the viewPager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // Steup tablayout
        tabIndicator.setupWithViewPager(screenPager);

        // next button click listener
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if(position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size()) { // when we reach the last screen
                    loadLastScreen();
                }
            }
        });

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size()) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Get started Button click listener
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the main activity
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                configName = "TestConfig";
                mainActivity.putExtra("CHECKIN_CONFIG_NAME", configName);
                startActivity(mainActivity);
                // Save flag to ensure onboarding occcure just once
                savePrefsData();
                finish();
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("onboardingPrefs", MODE_PRIVATE);
        Boolean checkedIn = pref.getBoolean("CheckedIn", false);
        return checkedIn;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("onboardingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("CheckedIn",true);
        editor.commit();
    }

    // show the get started button and hide the indicator and the next button
    private void loadLastScreen() {
        buttonNext.setVisibility(View.INVISIBLE);
        buttonGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        screenPager.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
        // Setup Button animation
        buttonGetStarted.setAnimation(buttonAnimation);
    }
}
