package com.schneewittchen.rosandroid.ui.fragments.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.DetailGroupOverviewFragment;
import com.schneewittchen.rosandroid.ui.fragments.details.DetailOverviewFragment;
import com.schneewittchen.rosandroid.ui.fragments.ssh.SshFragment;
import com.schneewittchen.rosandroid.ui.fragments.details.DetailsFragment;
import com.schneewittchen.rosandroid.ui.fragments.viz.VizFragment;
import com.schneewittchen.rosandroid.ui.fragments.MasterFragment;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 10.01.20
 * @updated on 29.01.20
 * @modified by
 */
public class ConfigTabsPagerAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles = new String[]{"Master", "Viz", "Details", "SSH"};
    private final Fragment[] fragments = new Fragment[]{
            MasterFragment.newInstance(),
            VizFragment.newInstance(),
            DetailOverviewFragment.newInstance(),
            SshFragment.newInstance()
    };

    public ConfigTabsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
