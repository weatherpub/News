package edu.sfsu.news.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.sfsu.news.R;
import edu.sfsu.news.code.adapter.RecyclerViewAdapter;
import edu.sfsu.news.code.model.NewsModel;
import edu.sfsu.news.databinding.FragmentHomeBinding;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    ArrayList<NewsModel> newsModel = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new AsyncCategory().execute("https://newsapi.org/v2/everything?q=biden&from=2024-06-29&sortBy=popularity&apiKey=6a5b4f0943e447a092cc59f7fbe690ef");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.id_recycler_view);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Inner Class - There isn't a 'static' keyword.
    public class AsyncCategory extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("LOG", "onPreExecute() -> Running ");
            // progress = (ProgressBar) findViewById(R.id.id_progress_bar);
            //progress.setVisibility(View.VISIBLE);
            //progress.setProgress(0);
        }

        @Override
        protected String doInBackground(String... param) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(param[0]).build();

            try {
                Response response = client.newCall(request).execute();

                if(!response.isSuccessful())
                    return null;

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) { // onPostExecute - runs on the main thread.
            super.onPostExecute(result);

            Log.v("LOG", "result -> " + result);

            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("articles");

                for(int i =  0; i < list.length(); i++) {
                    newsModel.add(new NewsModel(
                            list.getJSONObject(i).getJSONObject("source").getString("name"),
                            list.getJSONObject(i).getString("author"),
                            list.getJSONObject(i).getString("title"),
                            list.getJSONObject(i).getString("description"),
                            list.getJSONObject(i).getString("url"),
                            list.getJSONObject(i).getString("urlToImage"),
                            list.getJSONObject(i).getString("publishedAt"),
                            list.getJSONObject(i).getString("content")));
                }

                Log.v("LOG", "Name -> " + newsModel.get(0).getName());
                Log.v("LOG", "Author -> " + newsModel.get(0).getAuthor());
                Log.v("LOG", "Title -> " + newsModel.get(0).getTitle());
                Log.v("LOG", "Description -> " + newsModel.get(0).getDescription());
                Log.v("LOG", "URL -> " + newsModel.get(0).getUrl());
                Log.v("LOG", "UrlToImage -> " + newsModel.get(0).getUrlToImage());
                Log.v("LOG", "PublishedAt -> " + newsModel.get(0).getPublishedAt());
                Log.v("LOG", "Content -> " + newsModel.get(0).getContent());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new RecyclerViewAdapter(newsModel));
        }
    }
}