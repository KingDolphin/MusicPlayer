package com.leonmontealegre.musicplayer;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class Song {

    private String artist, album, title, dataPath;
    private Bitmap bitmap;
    private long albumId;
    private int duration;
    private Uri albumArtUri;

    public Song(String artist, Bitmap bitmap, String album, String title, String dataPath, long albumId, int duration, Uri albumArtUri) {
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.dataPath = dataPath;
        this.bitmap = bitmap;
        this.albumId = albumId;
        this.duration = duration;
        this.albumArtUri = albumArtUri;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAlbumTitle() {
        return this.album;
    }

    public Bitmap getAlbumCover() {
        return bitmap;
    }

    public String getArtist() {
        return this.artist;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getDataPath() { return this.dataPath; }

    public void play() {
        MusicService.instance.setSong(this);
    }

}
