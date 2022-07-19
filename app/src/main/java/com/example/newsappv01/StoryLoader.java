package com.example.newsappv01;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StoryLoader extends AsyncTaskLoader<List<Story>> {

    String url;
    public StoryLoader(@NonNull Context context, String... requestUrl) {
        super(context);
        if(requestUrl.length < 1 || requestUrl[0] == null)
        {
            Log.e("Loader Error", "StoryLoader: Error in input String");
            return;
        }
        url = requestUrl[0];
    }

    @Nullable
    @Override
    public List<Story> loadInBackground() {
        ArrayList<Story> stories = QueryUtils.getRelevantStories(url);
        return stories;
    }
}
