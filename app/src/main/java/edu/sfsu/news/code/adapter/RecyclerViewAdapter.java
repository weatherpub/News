package edu.sfsu.news.code.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.sfsu.news.R;
import edu.sfsu.news.code.model.NewsModel;
import edu.sfsu.news.code.picasso.RoundedTransformation;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<NewsModel> newsModel;

    Listener listener;

    // Decouple the Adapter with an interface - HFAD 572
    public interface Listener {
        void onClick(int position);
    }

    public RecyclerViewAdapter(ArrayList<NewsModel> newsModel) {
        this.newsModel = newsModel;
    }

    // patrick - Nested Class, it contains the 'static' keyword.
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("LOG", "onCreateViewHolder()");
        // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        // CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        return new ViewHolder(v);
    }

    /*
     * When a View appears on screen, the Recycler View calls the RecyclerViewAdapter's on BindViewHolder()
     * method to make the Card View match the details of the list item.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, final int position) {
        Log.v("LOG", "onBindViewHolder()");

        View itemView = holder.itemView;
        // CardView cardView = holder.CardView;

        NewsModel item = newsModel.get(position);
        holder.title.setText(String.format("%s", item.getTitle()));

        Picasso.get().load(Uri.parse(item.getUrlToImage())).resize(200, 150).centerCrop().transform(new RoundedTransformation(10, 0)).into(holder.urlToImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
        // cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onClick(position);
                }
            }
        });

        /*
        holder.itemView.setOnClickListener(view -> {
            if(listener != null) {
                listener.onClick(position, item);
            }
        });
        */

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

    public void setListener(Listener listener) {
        this.listener = listener;
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

        // private CardView CardView;
        public ViewHolder(@NonNull View itemView) {
        // public ViewHolder(@NonNull CardView itemView) {
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