package com.schneewittchen.rosandroid.ui.fragments.config;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 24.01.20
 * @updated on 05.02.20
 * @modified by
 */
public class ConfigListAdapter extends RecyclerView.Adapter<ConfigListAdapter.MyViewHolder> {

    public List<String> configNameList;


    public ConfigListAdapter() {
        configNameList = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.config_chooser_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String name = configNameList.get(position);
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return configNameList.size();
    }

    public void setConfigs(List<String> newConfigs){
        this.configNameList.clear();
        this.configNameList.addAll(newConfigs);
        
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;


        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.config_name_textview);
        }
    }
}