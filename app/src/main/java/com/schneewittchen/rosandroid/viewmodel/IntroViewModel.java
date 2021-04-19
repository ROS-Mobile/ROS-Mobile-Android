package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.content.res.TypedArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;
import com.schneewittchen.rosandroid.ui.fragments.intro.ScreenItem;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 22.06.20
 * @updated on 06.08.20
 * @modified by Nico Studt
 */
public class IntroViewModel extends AndroidViewModel {

    private static final String TAG = IntroViewModel.class.getSimpleName();

    ConfigRepository configRepo;


    public IntroViewModel(@NonNull Application application) {
        super(application);
        configRepo = ConfigRepositoryImpl.getInstance(getApplication());
    }


    public List<ScreenItem> getOnboardingScreenItems() {
        List<ScreenItem> mList = new ArrayList<>();
        String[] titleArray = getApplication().getResources().getStringArray(R.array.intro_title);
        String[] descrArray = getApplication().getResources().getStringArray(R.array.intro_descr);
        TypedArray imgArray = getApplication().getResources().obtainTypedArray(R.array.intro_img);

        for (int i = 0; i < titleArray.length; i++) {
            mList.add(new ScreenItem(titleArray[i], descrArray[i], imgArray.getResourceId(i, -1)));
        }

        imgArray.recycle();

        List<ScreenItem> mListUpdate = getUpdateScreenItems();
        mList.addAll(mListUpdate);

        return mList;
    }

    public List<ScreenItem> getUpdateScreenItems() {
        List<ScreenItem> mList = new ArrayList<>();
        String[] titleArray = getApplication().getResources().getStringArray(R.array.update_title);
        String[] descrArray = getApplication().getResources().getStringArray(R.array.update_descr);
        TypedArray imgArray = getApplication().getResources().obtainTypedArray(R.array.update_img);

        for (int i = 0; i < titleArray.length; i++) {
            mList.add(new ScreenItem(titleArray[i], descrArray[i], imgArray.getResourceId(i, -1)));
        }

        imgArray.recycle();

        return mList;
    }
}
