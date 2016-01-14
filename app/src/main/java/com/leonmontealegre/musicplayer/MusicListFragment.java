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

    protected SongList songs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        getActivity().setTitle(R.string.app_title);
        ArrayAdapter adapter = getArrayAdapter();
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        registerForContextMenu(listView);
        return v;
    }

    protected abstract ArrayAdapter getArrayAdapter();

    protected abstract void onSearch(String keyword);

    @Override
    public final void onListItemClick(ListView l, View v, int position, long id) {
          /* TODO : Switch to player with song */
//        Object o = ((ArrayAdapter)getListAdapter()).getItem(position);
//        Log.d(TAG, o.toString() + " was clicked.");
//        Intent i = new Intent(getActivity(), FamilyMemberActivity.class);
//        i.putExtra(FamilyMember.EXTRA_RELATION, f.getRelation());
//        i.putExtra(FamilyMember.EXTRA_INDEX, position);
//        startActivity(i);
    }

}
