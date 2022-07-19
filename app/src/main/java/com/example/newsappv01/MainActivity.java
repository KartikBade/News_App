package com.example.newsappv01;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

    private StoryAdapter storyAdapter;
    private String newsUrl;
    ImageView searchButton;
    EditText searchBar;
    TextView emptyView;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView mainNewsList = findViewById(R.id.main_news_list);
        storyAdapter = new StoryAdapter(this, new ArrayList<Story>());
        searchButton = findViewById(R.id.search_button);
        searchBar = findViewById(R.id.search_bar);
        emptyView = findViewById(R.id.empty_view);
        progressBar = findViewById(R.id.progress_bar);

        mainNewsList.setAdapter(storyAdapter);
        mainNewsList.setEmptyView(emptyView);
        progressBar.setVisibility(View.GONE);

        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storyAdapter.clear();
                if (connectivityManager.getActiveNetworkInfo() == null) {
                    emptyView.setText("No Internet :(");
                    return;
                }

                String query = String.valueOf(searchBar.getText());
                if (query == null || query.length() < 1) {
                    return;
                }
                newsUrl = "http://content.guardianapis.com/search?q=" + query + "&api-key=test";
                getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                emptyView.setText("");
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        mainNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Story currentStory = storyAdapter.getItem(position);
                Uri link = Uri.parse(currentStory.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, link);
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {
        return new StoryLoader(this, newsUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        storyAdapter.clear();
        emptyView.setText("No News Found :(");
        progressBar.setVisibility(View.GONE);
        if(stories == null || stories.isEmpty())
        {
            Log.e("Loader Error", "onLoadFinished: List of stories is empty or null");
            return;
        }
        storyAdapter.addAll(stories);
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        storyAdapter.clear();
    }
}