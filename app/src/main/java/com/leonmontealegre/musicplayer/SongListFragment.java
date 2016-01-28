package com.leonmontealegre.musicplayer;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 382277 on 1/26/16.
 */
public class SongListFragment extends MusicListFragment {

    public static final String TAG = "SongListFragment";

    protected ArrayAdapter getArrayAdapter() {
        return new SongAdapter(SongList.getSongs());
    }

    protected void onSearch(String keyword) {

    }

    private class SongAdapter extends ArrayAdapter<Song> {
        public SongAdapter(List songs) {
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
            }

            Song s = getItem(position);
            Log.d(TAG, "The song at position " + position + " is " + s.getTitle());

            Button button = (Button)convertView.findViewById(R.id.song_list_item_button);
            button.setText(s.getTitle());
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
