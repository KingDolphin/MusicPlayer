package com.leonmontealegre.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ControlBarFragment extends Fragment {

    private ImageButton playPauseButton;

    private int playImage, pauseImage;

    private boolean isPaused = true;

    private TextView currentTimeTextView, totalTimeTextView;
    private SeekBar durationBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_control_bar, container, false);

        playPauseButton = (ImageButton)rootView.findViewById(R.id.playPauseButton);
        playImage = android.R.drawable.ic_media_play;
        pauseImage = android.R.drawable.ic_media_pause;
        playPauseButton.setImageResource(playImage);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MusicService.instance.isStopped) {
                    if (isPaused) {
                        MusicService.instance.resumeMusic();
                    } else {
                        MusicService.instance.pauseMusic();
                    }
                } else {
                    SongList.play(0);
                }
            }
        });

        currentTimeTextView = (TextView)rootView.findViewById(R.id.currentTime);
        totalTimeTextView = (TextView)rootView.findViewById(R.id.totalTime);

        return rootView;
    }

    public void onSongPlay(Song song) {
        playPauseButton.setImageResource(pauseImage);
        isPaused = false;

        totalTimeTextView.setText(MusicService.instance.getCurrentSong().getDuration());
        //durationBar.setMax(mPlayer.getDuration());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTimeTextView.setText(MusicService.instance.getCurrentPosition());
                //durationBar.setProgress(mPlayer.getCurrentPosition());
            }
        }, 0, 1000);
    }

    public void onSongPause() {
        playPauseButton.setImageResource(playImage);
        isPaused = true;
    }

}
