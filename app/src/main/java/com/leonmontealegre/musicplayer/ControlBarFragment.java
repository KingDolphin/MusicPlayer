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
import java.util.concurrent.TimeUnit;

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
        durationBar = (SeekBar)rootView.findViewById(R.id.durationBar);
        durationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && MusicService.instance.isPlaying)
                    MusicService.instance.setCurrentPosition(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (!MusicService.instance.isPlaying)
                    seekBar.setFocusable(false);
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setFocusable(true);
            }
        });

        return rootView;
    }

    public void onSongPlay(Song song) {
        playPauseButton.setImageResource(pauseImage);
        isPaused = false;

        durationBar.setFocusable(true);

        int milliseconds = MusicService.instance.getCurrentSong().getDuration();
        int seconds = (milliseconds / (1000)) % 60;
        int minutes = (milliseconds / (1000*60)) % 60;
        int hours   = (milliseconds / (1000*60*60)) % 24;
        totalTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        durationBar.setMax(milliseconds);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int milliseconds = MusicService.instance.getCurrentPosition();
                        if (milliseconds > 0) {
                            int seconds = (milliseconds / (1000)) % 60;
                            int minutes = (milliseconds / (1000 * 60)) % 60;
                            int hours = (milliseconds / (1000 * 60 * 60)) % 24;
                            currentTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                            durationBar.setProgress(milliseconds);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    public void onSongPause() {
        playPauseButton.setImageResource(playImage);
        isPaused = true;
    }

    public void onSongStop() {
        onSongPause();
        durationBar.setFocusable(false);
    }

}
