package com.schneewittchen.rosandroid.ui.fragments.ssh;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;

import java.util.ArrayList;


public class SshRecyclerViewAdapter extends RecyclerView.Adapter<SshRecyclerViewAdapter.SshViewHolder> {

    private final ArrayList<String> dataset;


    // Constructor
    public SshRecyclerViewAdapter() {
        dataset = new ArrayList<>();
    }


    @Override
    public SshRecyclerViewAdapter.SshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView)  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ssh_text_view, parent, false);
        return new SshViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SshViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String data = dataset.get(position);
        holder.textView.setText(data);
    }

    public void addItem(String item) {
        this.dataset.add(item);

        // Remove data in circular fashion
        while (dataset.size() > 1000) {
            dataset.remove(0);
        }

        this.notifyItemInserted(dataset.size()-1);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }


    /**
     * Provide a reference to the views for each data item.
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder.
     */
    public static class SshViewHolder extends RecyclerView.ViewHolder {
        
        // each data item is just a string in this case
        public TextView textView;

        public SshViewHolder(TextView view) {
            super(view);
            textView = view;
        }
    }
}
