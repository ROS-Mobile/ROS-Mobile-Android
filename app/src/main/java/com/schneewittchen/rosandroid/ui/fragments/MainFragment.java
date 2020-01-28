package com.schneewittchen.rosandroid.ui.fragments;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.schneewittchen.rosandroid.ui.helper.OnBackPressedListener;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.helper.ConfigTabsPagerAdapter;
import com.schneewittchen.rosandroid.ui.helper.LockableViewPager;
import com.schneewittchen.rosandroid.viewmodel.MainViewModel;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class MainFragment extends Fragment implements OnBackPressedListener {

    ConfigTabsPagerAdapter pagerAdapter;
    LockableViewPager viewPager;
    TabLayout tabLayout;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    MainViewModel mViewModel;


    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.configViewpager);
        tabLayout = view.findViewById(R.id.tabs);
        toolbar = view.findViewById(R.id.toolbar);
        drawerLayout = view.findViewById(R.id.drawer_layout);

        drawerLayout.setScrimColor(getResources().getColor(R.color.drawerFadeColor));

        // Connect toolbar to application
        if(getActivity() instanceof AppCompatActivity){

            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            // Setup home indicator to open drawer layout
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        // Setup tabs for navigation
        pagerAdapter = new ConfigTabsPagerAdapter(this.getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.getConfigTitle().observe(this, s -> setTitle(s));
    }

    private void setTitle(String newTitle) {
        if (newTitle.equals(toolbar.getTitle().toString())) {
            return;
        }

        toolbar.setTitle(newTitle);
    }

    public boolean onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home){
                drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_whole_configuration, container, false);
    }

}
