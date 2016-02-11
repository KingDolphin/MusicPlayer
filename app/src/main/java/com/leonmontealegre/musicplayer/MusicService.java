package com.leonmontealegre.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    public static final MusicService instance = new MusicService();

    private final IBinder mBinder = new Binder() {
        public MusicService getService() {
            return MusicService.this;
        }
    };

    private MediaPlayer mPlayer;
    private int currentTime = 0;
    private Song currentSong;

    public boolean isPlaying, isStopped = true;

    @Override
    public IBinder onBind(Intent intent){ return mBinder; }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        if (mPlayer != null)
            mPlayer.start();

        return START_STICKY;
    }


    public void setSong(Song song) {
        if (song == null)
            return;

        if (mPlayer != null && mPlayer.isPlaying())
            stopMusic();

        currentSong = song;
        mPlayer = new MediaPlayer();

        try {
            // Sets data for song
            mPlayer.setDataSource(song.getDataPath());

            // Streams music from the system instead of loading it all into memory
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            // To debug errors
            mPlayer.setOnErrorListener(this);

            // Song shouldn't loop
            mPlayer.setLooping(false);

            // Set default volume
            mPlayer.setVolume(100, 100);

            // Called when the song is done
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Should never be null, but good to check
                    if (MainActivity.tabbedFragment.currentFragment != null) {
                        isPlaying = false;
                        stopMusic();
                    }
                }
            });

            startMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Song getCurrentSong() {
        return this.currentSong;
    }

    public int getCurrentPosition() {
        // Returns current time of song if it's playing
        if (mPlayer != null && mPlayer.isPlaying())
            return mPlayer.getCurrentPosition();
        return 0;
    }

    public void setCurrentPosition(int position) {
        // Sets current time of song if it's playing
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.seekTo(position);
    }

    public void startMusic() throws IOException {
        // Attempts to start the current song
        if (mPlayer != null && !mPlayer.isPlaying()) {
            // Prepares the player
            mPlayer.prepare();
            mPlayer.start();

            isPlaying = true;
            isStopped = false;

            // Calls onPlay method on the control bar to update the UI
            MainActivity.controlBar.onSongPlay(currentSong);
        }
    }

    public void pauseMusic() {
        // Attempts to pause the current song if a song is playing
        if (mPlayer != null && mPlayer.isPlaying()) {
            // Pauses the player
            mPlayer.pause();

            // Sets current time since it becomes '0' when paused
            currentTime = mPlayer.getCurrentPosition();

            isPlaying = false;
            isStopped = false;

            // Calls onPause method on the control bar to update the UI
            MainActivity.controlBar.onSongPause();
        }
    }

    public void resumeMusic() {
        // Attempts to resume the song is a song is paused
        if (mPlayer != null && !mPlayer.isPlaying()) {
            // Sets current time to previous saved time
            mPlayer.seekTo(currentTime);

            // Starts song again
            mPlayer.start();

            isPlaying = true;
            isStopped = false;

            // Calls onPlay method on the control bar to update the UI
            MainActivity.controlBar.onSongPlay(currentSong);
        }
    }

    public void stopMusic() {
        // Attempts to stop the current song so that another song can be played
        if (mPlayer != null) {
            // Stops the player
            mPlayer.stop();

            // Releases the resources from memory
            mPlayer.release();

            // Resets the player
            mPlayer = null;

            isPlaying = false;
            isStopped = true;

            // Calls onStop method on the control bar to update the UI
            MainActivity.controlBar.onSongStop();
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        isStopped = true;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "Music player failed!", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

}
