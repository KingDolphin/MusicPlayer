package com.leonmontealegre.musicplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SongListFragment extends MusicListFragment {

    public static final String TAG = "SongListFragment";

    @Override
    // Returns the SongAdapter with all the songs in alphabetical order
    protected ArrayAdapter getArrayAdapter() {
        return new SongAdapter(SongList.getSongs());
    }

    @Override
    // Searches for the songs with the given keyword in their title
    protected void onSearch(String keyword) {
        // ArrayList of songs
        ArrayList<Song> results = new ArrayList<>();

        // Loops through all songs
        for(Song song : SongList.getSongs()) {
            // If title has keyword, ignores case
            if (song.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                results.add(song);
        }
        SongAdapter adapter = (SongAdapter)this.getListAdapter();

        // Clears the adapter of all current songs
        adapter.clear();

        // Adds all the ones from the search
        adapter.addAll(results);

        // Changes it
        adapter.notifyDataSetChanged();
    }

    private class SongAdapter extends ArrayAdapter<Song> {
        public SongAdapter(List songs) {
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // Inflates the view
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
            }

            // Gets the song at the index
            Song s = getItem(position);
            Button button = (Button)convertView.findViewById(R.id.song_list_item_button);
            button.setText(s.getTitle());
            button.setSelected(true);

            // Sets OnClickListener to play the song
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongList.play(position);
                }
            });
            return convertView;
        }
    }

}