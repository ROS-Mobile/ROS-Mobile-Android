package com.schneewittchen.rosandroid.ui.helper;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;

import java.util.ArrayList;

public class SshRecyclerViewAdapter extends RecyclerView.Adapter<SshRecyclerViewAdapter.SshViewHolder> {

    private ArrayList<String> data;

    // Constructor
    public SshRecyclerViewAdapter() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SshRecyclerViewAdapter.SshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView)  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ssh_text_view, parent, false);
        SshViewHolder vh = new SshViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SshViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(data.get(position));
    }

    public void addItem(String item) {
        System.err.println("Add item: " + item);
        this.data.add(item);
        // Check size
        while (data.size() > 1000) {
            data.remove(0);
        }
        this.notifyItemInserted(data.size()-1);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SshViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public SshViewHolder(TextView view) {
            super(view);
            textView = view;
        }
    }
}
