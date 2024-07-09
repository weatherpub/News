package edu.sfsu.news.code.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.sfsu.news.R;
import edu.sfsu.news.code.model.NewsModel;
import edu.sfsu.news.code.picasso.RoundedTransformation;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<NewsModel> newsModel;

    public RecyclerViewAdapter(ArrayList<NewsModel> newsModel) {
        this.newsModel = newsModel;
    }

    // patrick - Nested Class, it contains the 'static' keyword.
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("LOG", "onCreateViewHolder()");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Log.v("LOG", "onBindViewHolder()");

        NewsModel mod = newsModel.get(position);
        holder.title.setText(String.format("%s", mod.getTitle()));
        Picasso.get().load(Uri.parse(mod.getUrlToImage())).resize(200, 150).centerCrop().transform(new RoundedTransformation(10, 0)).into(holder.urlToImage);
        /*
        holder.name.setText(String.format("%s", mod.getName()));
        holder.author.setText(String.format("%s", mod.getAuthor()));
        holder.description.setText(String.format("%s", mod.getDescription()));
        holder.url.setText(String.format("%s", mod.getUrl()));
        holder.publishedAt.setText(String.format("%s", mod.getPublishedAt()));
        holder.content.setText(String.format("%s", mod.getContent()));
        */
    }

    @Override
    public int getItemCount() {
        return newsModel.size();
    }

    // Nested Class - This class contains a static keyword.
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView author;
        public TextView title;
        public TextView description;
        public TextView url;
        public ImageView urlToImage;
        public TextView publishedAt;
        public TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // name = itemView.findViewById(R.id.tv_name);
            // author = itemView.findViewById(R.id.tv_author);
            title = itemView.findViewById(R.id.tv_title);
            // description = itemView.findViewById(R.id.tv_description);
            // url = itemView.findViewById(R.id.tv_url);
            urlToImage = itemView.findViewById(R.id.tv_url_to_image);
            // publishedAt = itemView.findViewById(R.id.tv_publishedAt);
            // content = itemView.findViewById(R.id.tv_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}