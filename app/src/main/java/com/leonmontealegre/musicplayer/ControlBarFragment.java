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

    private ImageButton playPauseButton, fastForwardButton, backwardButton;

    private int playImage, pauseImage;

    private boolean isPaused = true;

    private TextView currentTimeTextView, totalTimeTextView;
    private SeekBar durationBar;

    private int currentTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_control_bar, container, false);

        playPauseButton = (ImageButton)rootView.findViewById(R.id.playPauseButton);
        playImage = android.R.drawable.ic_media_play;
        pauseImage = android.R.drawable.ic_media_pause;
        playPauseButton.setImageResource(playImage);

        // Changes the play button to a pause button
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

        // Skips to next song if not the last song
        fastForwardButton = (ImageButton)rootView.findViewById(R.id.forwardButton);
        fastForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MusicService.instance.isStopped) {
                    Song s = MusicService.instance.getCurrentSong();
                    MusicService.instance.stopMusic();
                    if (s.index != SongList.getSongs().size()-1)
                        SongList.play(s.index+1);
                }
            }
        });

        // Goes to beginning of song if past the 3 second mark, otherwise goes to previous song
        backwardButton = (ImageButton)rootView.findViewById(R.id.backwardButton);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MusicService.instance.isStopped) {
                    Song s = MusicService.instance.getCurrentSong();
                    if (currentTime > 3000 || s.index == 0) { // more than 3 seconds through, restart song
                        MusicService.instance.setCurrentPosition(0);
                    } else { // else, go to previous song
                        MusicService.instance.stopMusic();
                        if (s.index != 0)
                            SongList.play(s.index - 1);
                    }
                }
            }
        });

        currentTimeTextView = (TextView)rootView.findViewById(R.id.currentTime);
        totalTimeTextView = (TextView)rootView.findViewById(R.id.totalTime);

        durationBar = (SeekBar)rootView.findViewById(R.id.durationBar);
        // Sets the current time of the song to the value of the slider bar
        durationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && !MusicService.instance.isStopped)
                    MusicService.instance.setCurrentPosition(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (!MusicService.instance.isStopped)
                    seekBar.setFocusable(false);
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setFocusable(true);
            }
        });

        // Updates the 'currentTime' text every second to be the correct time of the song
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MusicService.instance.isPlaying) {
                            int milliseconds = MusicService.instance.getCurrentPosition();
                            if (milliseconds > 0) {
                                int seconds = (milliseconds / (1000)) % 60;
                                int minutes = (milliseconds / (1000 * 60)) % 60;
                                int hours = (milliseconds / (1000 * 60 * 60)) % 24;
                                currentTime = milliseconds;
                                currentTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                                durationBar.setProgress(milliseconds);
                            }
                        }
                    }
                });
            }
        }, 0, 1000);

        return rootView;
    }

    public void onSongPlay(Song song) {
        playPauseButton.setImageResource(pauseImage); // Sets play button to pause button
        isPaused = false;

        // Lets user drag the bar to change song time
        durationBar.setFocusable(true);

        // Sets total time text to the duration of the song
        int milliseconds = MusicService.instance.getCurrentSong().getDuration();
        int seconds = (milliseconds / (1000)) % 60;
        int minutes = (milliseconds / (1000*60)) % 60;
        int hours   = (milliseconds / (1000*60*60)) % 24;
        totalTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        durationBar.setMax(milliseconds);
    }

    public void onSongPause() {
        playPauseButton.setImageResource(playImage); // Sets pause button to play button
        isPaused = true;
    }

    public void onSongStop() {
        onSongPause();
        totalTimeTextView.setText(String.format("%02d:%02d:%02d", 0, 0, 0)); // Sets total time to 00:00:00
        durationBar.setFocusable(false);
    }

}
