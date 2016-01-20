package com.leonmontealegre.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    public static final MusicService instance = new MusicService();

    private final IBinder mBinder = new Binder() {
        public MusicService getService() {
            return MusicService.this;
        }
    };

    private MediaPlayer mPlayer;
    private int length = 0;
    private Song currentSong;

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

        mPlayer = new MediaPlayer();//MediaPlayer.create(this, Uri.parse(song.getDataPath()));


        try {
            //mPlayer.release();
            mPlayer.setDataSource(song.getDataPath());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setOnErrorListener(this);

            mPlayer.setLooping(false);
            mPlayer.setVolume(100, 100);

            mPlayer.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    onError(mPlayer, what, extra);
                    return true;
                }
            });
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startMusic() {
        if(mPlayer != null && !mPlayer.isPlaying()) {

        }
    }

    public void pauseMusic() {
        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
        }
    }

    public void resumeMusic() {
        if(mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        if(mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "Music player failed!", Toast.LENGTH_SHORT).show();
        if(mPlayer != null) {
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
