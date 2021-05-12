package com.schneewittchen.rosandroid.ui.fragments.intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.schneewittchen.rosandroid.ui.fragments.main.MainFragment;
import com.schneewittchen.rosandroid.viewmodel.IntroViewModel;

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

    private static final String TAG = IntroFragment.class.getSimpleName();


    ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button buttonNext;
    Button buttonGetStarted;
    Animation buttonAnimation;
    Button buttonConfiguration;
    EditText editTextConfigName;
    YouTubePlayerView videoView;
    IntroViewModel mViewModel;
    List<ScreenItem> screenItems;
    int itemPosition;
    boolean requireCheckIn;


    public static IntroFragment newInstance() {
        return new IntroFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        requireCheckIn = requireCheckIn();

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

        // Setup the viewPager
        if (requireCheckIn) screenItems = mViewModel.getOnboardingScreenItems();
        else screenItems = mViewModel.getUpdateScreenItems();
        screenPager = view.findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this.getContext(), screenItems);
        screenPager.setAdapter(introViewPagerAdapter);

        // Setup tablayout
        tabIndicator.setupWithViewPager(screenPager);

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == screenItems.size()) {
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

        // Set the video
        getLifecycle().addObserver(videoView);

        // next button click listener
        buttonNext.setOnClickListener(v -> {
            try {
                jumpToNextScreen();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });
        // Get started Button click listener
        buttonGetStarted.setOnClickListener(v -> loadConfigNameScreen());
        // NameConfig Click Listener
        buttonConfiguration.setOnClickListener(v -> {
            try {
                createFirstConfig();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    private void createFirstConfig() throws PackageManager.NameNotFoundException {
        // Get string for first config name
        Bundle bundle = new Bundle();
        bundle.putString("configName", editTextConfigName.getText().toString());

        // Save the Prefs
        setCheckInPrefData();

        loadMainFragment(bundle);
    }

    private void jumpToNextScreen() throws PackageManager.NameNotFoundException {
        itemPosition = screenPager.getCurrentItem();
        itemPosition++;

        if (itemPosition < screenItems.size()) {
            screenPager.setCurrentItem(itemPosition);

        } else {
            if (requireCheckIn) loadVideoScreen();
            else {
                setUpdatePrefData();
                loadMainFragment(null);
            }
        }
    }

    private void loadMainFragment(Bundle bundle) {
        // Start the next fragment
        if (getActivity() == null) {
            return;
        }

        MainFragment mainFragment = new MainFragment();
        if (bundle != null) mainFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, mainFragment)
                .addToBackStack(null)
                .commit();
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

    private void setCheckInPrefData() throws PackageManager.NameNotFoundException {
        if (getContext() == null) {
            return;
        }

        SharedPreferences pref = getContext().getSharedPreferences("introPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("CheckedIn", true);
        editor.apply();

        setUpdatePrefData();
    }

    private void setUpdatePrefData() throws PackageManager.NameNotFoundException {
        if (getContext() == null) {
            return;
        }

        SharedPreferences pref = getContext().getSharedPreferences("introPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("VersionNumber", getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionCode);
        editor.apply();
    }

    // Get pref data
    private boolean requireCheckIn() {
        SharedPreferences pref = getContext().getSharedPreferences("introPrefs", MODE_PRIVATE);
        return !pref.getBoolean("CheckedIn", false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }
}
