package edu.sfsu.news.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.sfsu.news.R;
import edu.sfsu.news.code.adapter.RecyclerViewAdapter;
import edu.sfsu.news.code.helper.OnFragmentInteractionListener;
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
        String today = fmt.format(new Date());

        final String API = "https://newsapi.org/v2/everything?q=biden&from=" + fmt.format(new Date()) + "&sortBy=popularity&language=en&apiKey=6a5b4f0943e447a092cc59f7fbe690ef";
        new AsyncCategory().execute(API);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.id_recycler_view);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

/*
        if(context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeExecption(context.toString() + "must implement OnFragmentInteractionListener");
        }
 */
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

            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            try {
                File file = new File(Objects.requireNonNull(getContext()).getFilesDir(), fmt.format(new Date()) + ".txt");
                FileWriter fileWriter = new FileWriter(file);

                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(result.toString());
                bufferedWriter.close();

                //  Read file from disk
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();

                while(line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }

                bufferedReader.close();

                // This is being populated from disk
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

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new RecyclerViewAdapter(newsModel));
        }
    }
}