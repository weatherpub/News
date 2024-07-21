package edu.sfsu.news.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.sfsu.news.R;
import edu.sfsu.news.code.activity.ContentActivity;
import edu.sfsu.news.code.adapter.RecyclerViewAdapter;
import edu.sfsu.news.code.model.NewsModel;
import edu.sfsu.news.databinding.FragmentHomeBinding;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public RecyclerView recyclerView;
    public ArrayList<NewsModel> newsModel = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        final String TOPIC = "biden";
        final String TODAY = fmt.format(new Date());
        // final String URL = "https://newsapi.org/v2/everything?q=" + TOPIC + "&from=2024-07-19&sortBy=popularity&language=en&apiKey=6a5b4f0943e447a092cc59f7fbe690ef";
        final String URL = "https://newsapi.org/v2/everything?q=" + TOPIC + "&from=2024-07-19&sortBy=popularity&language=en&apiKey=6a5b4f0943e447a092cc59f7fbe690ef";
        Log.v("LOG", URL);
        /* Sources: Top Headlines
         * Find sources that display news of this category.
         * Possible options: business, entertainment, general, health, science, sports, technology.
         * Default: all categories.
         *
         * Categories
         * business, entertainment, general, health, science, sports, technology
         *
         * https://newsapi.org/v2/top-headlines/sources?category=" + category + "&lang=en&country=us&apiKey=6a5b4f0943e447a092cc59f7fbe690ef
         *
         * Default - no category defined
         * https://newsapi.org/v2/top-headlines/sources?lang=en&country=us&apiKey=6a5b4f0943e447a092cc59f7fbe690ef
         * */
        /* If you specify the AsyncTask.THREAD_POOL_EXECUTOR a new Thread Pool will be created,
         * sized appropriately for the number of CPU's available on the device,
         * and your Async Tasks will be executed in parallel.
         * */

        new AsyncCategory().execute(URL);
        // new AsyncCategory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.id_recycler_view);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * AsyncCategory makes an HTTP request to an API and returns JSON.
     * 1. Makes remote call
     * 2. Downloads to the local directory.
     * 3. Opens the file to read the json.
     */
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

            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            // background thread to open and parse the downloaded json.
            final Handler handler = new Handler();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.v("LOG", "Inside of the new Runnable Thread!");

                    try {
                        File file = new File(requireContext().getFilesDir(), fmt.format(new Date()) + "_home.txt");
                        FileWriter fileWriter = new FileWriter(file);

                        // Write JSON response to disk.
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(result.toString());
                        bufferedWriter.close();

                        // Read JSON response from disk.
                        FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String line = bufferedReader.readLine();

                        while(line != null) {
                            stringBuilder.append(line).append("\n");
                            line = bufferedReader.readLine();
                        }

                        bufferedReader.close();

                        // This is being populated from disk.
                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                        JSONArray obj = jsonObject.getJSONArray("articles");

                        for(int i =  0; i < obj.length(); i++) {
                            newsModel.add(new NewsModel(
                                    obj.getJSONObject(i).getJSONObject("source").getString("name"),
                                    obj.getJSONObject(i).getString("author"),
                                    obj.getJSONObject(i).getString("title"),
                                    obj.getJSONObject(i).getString("description"),
                                    obj.getJSONObject(i).getString("url"),
                                    obj.getJSONObject(i).getString("urlToImage"),
                                    obj.getJSONObject(i).getString("publishedAt"),
                                    obj.getJSONObject(i).getString("content")));
                        }
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Handler allows the UI to be updated.
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(newsModel);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            adapter.setListener(new RecyclerViewAdapter.Listener() {
                                @Override
                                public void onClick(int position) {
                                    Intent intent = new Intent(getActivity(), ContentActivity.class);
                                    Log.v("LOG", "[ July 17, 2024 onClick intent in HomeFragment was clicked ] => " + position);
                                    String url = newsModel.get(position).getUrlToImage();
                                    intent.putExtra("image", url);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }).start();
        }
    }
}