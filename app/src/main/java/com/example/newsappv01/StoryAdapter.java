package com.example.newsappv01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends ArrayAdapter<Story> {
    public StoryAdapter(@NonNull Context context, @NonNull ArrayList<Story> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if(listViewItem == null)
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.story_list_item, parent, false);

        Story currentStory = getItem(position);

        TextView storyTitle = listViewItem.findViewById(R.id.story_headline);
        TextView storyWriter = listViewItem.findViewById(R.id.story_writer);
        TextView storyDate = listViewItem.findViewById(R.id.story_date);
        TextView storySection = listViewItem.findViewById(R.id.story_section);

        storyTitle.setText(currentStory.getTitle());
        storyWriter.setText(currentStory.getWriter());
        storyDate.setText(currentStory.getDate());
        storySection.setText(currentStory.getSection());

        return listViewItem;
    }
}
