package com.schneewittchen.rosandroid.ui.fragments;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.helper.IntroViewPagerAdapter;
import com.schneewittchen.rosandroid.ui.helper.ScreenItem;
import com.schneewittchen.rosandroid.viewmodel.IntroViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 22.06.20
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 */

public class IntroFragment extends Fragment {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button buttonNext;
    int position = 0;
    Button buttonGetStarted;
    Animation buttonAnimation;
    Button buttonConfiguration;
    EditText editTextConfigName;

    YouTubePlayerView videoView;

    IntroViewModel mViewModel;

    public static IntroFragment newInstance() {
        return new IntroFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Create the view model
        mViewModel = new ViewModelProvider(this).get(IntroViewModel.class);

        // Init Views
        buttonNext = view.findViewById(R.id.onboarding_btn_next);
        buttonGetStarted = view.findViewById(R.id.onboarding_btn_getStarted);
        buttonConfiguration = view.findViewById(R.id.onboarding_btn_startConfig);
        editTextConfigName = view.findViewById(R.id.onboarding_editText_configName);
        tabIndicator = view.findViewById(R.id.tabIndicator);
        videoView = view.findViewById(R.id.onboarding_video_view);
        buttonAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.onboarding_buttton_animation);

        // Set the video
        getLifecycle().addObserver(videoView);

        // Setup the viewPager
        List<ScreenItem> mList = mViewModel.getScreenItems();
        screenPager = view.findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this.getContext(), mList);
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
                    loadVideoScreen();
                }
            }
        });

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size()) {
                    loadVideoScreen();
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
                loadConfigNameScreen();
            }
        });

        // NameConfig Click Listener
        buttonConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get string for first config name
                Bundle bundle = new Bundle();
                bundle.putString("configName",editTextConfigName.getText().toString());
                // Save the Prefs
                savePrefsData();
                // Start the next fragment
                MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, mainFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    // show the get started button and hide the indicator and the next button
    private void loadVideoScreen() {
        buttonGetStarted.setAnimation(buttonAnimation);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        screenPager.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.VISIBLE);
    }

    private void loadConfigNameScreen() {
        buttonGetStarted.setAnimation(null);
        buttonConfiguration.setAnimation(buttonAnimation);
        buttonGetStarted.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);
        buttonConfiguration.setVisibility(View.VISIBLE);
        editTextConfigName.setVisibility(View.VISIBLE);
    }

    private void savePrefsData() {
        SharedPreferences pref = getContext().getSharedPreferences("onboardingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("CheckedIn",true);
        editor.commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }
}
