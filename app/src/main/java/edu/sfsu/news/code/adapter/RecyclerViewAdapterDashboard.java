package edu.sfsu.news.code.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.sfsu.news.R;
import edu.sfsu.news.code.model.NewsModel;

public class RecyclerViewAdapterDashboard extends RecyclerView.Adapter<RecyclerViewAdapterDashboard.ViewHolder> {
    ArrayList<NewsModel> model;

    // 1. The constructor takes a param of NewsModel
    public RecyclerViewAdapterDashboard(ArrayList<NewsModel> m) {
       this.model = m;
    }

    /*  onCreateViewHolder - gets called when the Recycler View needs a new view holder.
        The recycler view calls the method repeatedly when the recycler view is first constructed to build
        the set of view holders that will be display on the screen. */
    @NonNull
    @Override
    public RecyclerViewAdapterDashboard.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("LOG", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_dash, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterDashboard.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // nested class - "static" keyword
    // Tells the Adapter which views to to use for the data items.
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}