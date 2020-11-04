package com.schneewittchen.rosandroid.ui.fragments.config;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 05.02.20
 * @updated on 05.02.20
 * @modified by
 */
public interface RecyclerViewItemClickListener {

    void onClick(RecyclerView parent, View view, int position);

    //void onLongClick(View view, int position);
}
