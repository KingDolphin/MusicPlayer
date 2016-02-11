package com.leonmontealegre.musicplayer;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by csastudent2015 on 1/28/16.
 */
public abstract class ArtistListFragment extends MusicListFragment {

    public static final String TAG = "ArtistsListFragment";

    @Override
    protected ArrayAdapter getArrayAdapter() {
        return new ArtistAdapter(SongList.getSongs());
    }

    protected void onSearch(String keyword) {

    }

    private class ArtistAdapter extends ArrayAdapter<Song> {
        public ArtistAdapter(List songs) {
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
            }

            Song s = getItem(position);
            Log.d(TAG, "The song at position " + position + " is " + s.getTitle());


            List<Song> musics = new ArrayList<>(SongList.getSongs());
            Set<String> artists = new TreeSet<>();
            for (Song q: musics) {
                artists.add(q.getArtist());
            }


            Button button = (Button)convertView.findViewById(R.id.artists_list_item_button);
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