package com.schneewittchen.rosandroid.ui.fragments.intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.schneewittchen.rosandroid.R;

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

public class IntroViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<ScreenItem> mListScreenItem;


    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreenItem) {
        this.mContext = mContext;
        this.mListScreenItem = mListScreenItem;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.fragent_onboarding, null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.introImage);
        TextView title = layoutScreen.findViewById(R.id.introTitle);
        TextView description = layoutScreen.findViewById(R.id.introDescription);

        title.setText(mListScreenItem.get(position).getTitle());
        description.setText(mListScreenItem.get(position).getDescription());
        imgSlide.setImageResource(mListScreenItem.get(position).getScreenImage());

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return mListScreenItem.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
