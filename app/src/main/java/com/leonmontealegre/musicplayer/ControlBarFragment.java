package com.leonmontealegre.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class ControlBarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_control_bar, container, false);

        ImageButton playPauseButton = (ImageButton)rootView.findViewById(R.id.playPauseButton);
        //playPauseButton.setImageDrawable(android.R.drawable.ic_media_play);

        return rootView;
    }

}
