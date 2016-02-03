package com.leonmontealegre.musicplayer;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public abstract class MusicListFragment extends ListFragment {

    private static final String TAG = "MusicListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, parent, savedInstanceState);

        getActivity().setTitle(R.string.app_title);
        ArrayAdapter adapter = getArrayAdapter();
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        ListView listView = (ListView)rootView.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return rootView;
    }

    protected abstract ArrayAdapter getArrayAdapter();

    protected abstract void onSearch(String keyword);

    public abstract void onSongFinish(Song s);

}
