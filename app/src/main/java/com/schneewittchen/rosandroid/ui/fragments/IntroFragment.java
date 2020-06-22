package com.schneewittchen.rosandroid.ui.fragments;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
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
 * @updated on
 * @modified by
 */

public class IntroFragment extends Fragment {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button buttonNext;
    int position = 0;
    Button buttonGetStarted;
    Animation buttonAnimation;

    YouTubePlayerView videoView;

    IntroViewModel mViewModel;

    public static IntroFragment newInstance() {
        return new IntroFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Init Views
        buttonNext = view.findViewById(R.id.onboarding_btn_next);
        buttonGetStarted = view.findViewById(R.id.onboarding_btn_getStarted);
        tabIndicator = view.findViewById(R.id.tabIndicator);
        videoView = view.findViewById(R.id.onboarding_video_view);
        buttonAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.onboarding_buttton_animation);

        // Fill the list
        List<ScreenItem> mList = new ArrayList<>();
        String[] title_array = getResources().getStringArray(R.array.intro_title);
        String[] descr_array = getResources().getStringArray(R.array.intro_descr);
        TypedArray img_array = getResources().obtainTypedArray(R.array.intro_img);
        for(int i=0; i<title_array.length; i++) {
            mList.add(new ScreenItem(title_array[i], descr_array[i], img_array.getResourceId(i,-1)));
        }

        // Set the video
        getLifecycle().addObserver(videoView);

        // Setup the viewPager
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
                // TODO: Get a String from a EditText and then name the first Config as this
                // String test = "testTest";
                // mViewModel.setConfigName(test);
                // First save the checked in
                savePrefsData();
                // Start the next fragment
                MainFragment mainFragment = new MainFragment();
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
        mViewModel = new ViewModelProvider(this).get(IntroViewModel.class);
    }


    // show the get started button and hide the indicator and the next button
    private void loadLastScreen() {
        buttonNext.setVisibility(View.INVISIBLE);
        buttonGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        screenPager.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.VISIBLE);
        // Setup Button animation
        buttonGetStarted.setAnimation(buttonAnimation);
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
