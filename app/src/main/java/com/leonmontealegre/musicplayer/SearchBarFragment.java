package com.leonmontealegre.musicplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SearchBarFragment extends Fragment {

    private EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search_bar, container, false);

        // Finds search bar
        searchBar = (EditText)rootView.findViewById(R.id.search_bar);

        // This is just to fix an annoying bug with android, just kinda ignore it
        searchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchBar.requestFocusFromTouch();
                return false;
            }
        });

        // On type, call the search method so that the ListView is filtered
        searchBar.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Calls the search method on the current fragment
                MainActivity.tabbedFragment.currentFragment.onSearch(searchBar.getText().toString());
            }

        });
        return rootView;
    }
}
