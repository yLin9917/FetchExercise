package edu.uga.cs.fetchexercise;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Item> itemList;

    // constructor
    public RecyclerViewAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * A ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView idTextView;
        public TextView listIdTextView;

        // constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            idTextView = itemView.findViewById(R.id.id);
            listIdTextView = itemView.findViewById(R.id.listId);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item currentItem = itemList.get(position);
        holder.nameTextView.setText(currentItem.getName());
        holder.idTextView.setText(String.valueOf(currentItem.getId()));
        holder.listIdTextView.setText(String.valueOf(currentItem.getListId()));
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
