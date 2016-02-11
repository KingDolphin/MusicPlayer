package com.leonmontealegre.musicplayer;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public abstract class MusicListFragment extends ListFragment {

    private static final String TAG = "MusicListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, parent, savedInstanceState);

        getActivity().setTitle(R.string.app_title);

        // Gets the ArrayAdapter
        ArrayAdapter adapter = getArrayAdapter();

        // Sets it as the list adapter
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Registers ListView
        ListView listView = (ListView)rootView.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return rootView;
    }

    protected abstract ArrayAdapter getArrayAdapter();

    protected abstract void onSearch(String keyword);

    public void onSongFinish(Song s) {
        // Plays next song if it's not the last song
        if (s.index != SongList.getSongs().size()-1)
            SongList.play(s.index+1);
    }

}
