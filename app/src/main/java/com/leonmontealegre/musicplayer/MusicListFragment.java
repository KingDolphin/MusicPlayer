package com.leonmontealegre.musicplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
