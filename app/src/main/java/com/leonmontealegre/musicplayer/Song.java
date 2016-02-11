package com.leonmontealegre.musicplayer;

import android.graphics.Bitmap;
import android.net.Uri;

public class Song implements Comparable <Song> {

    // To keep track of the amount of songs
    private static int COUNT = 0;

    private String artist, album, title, dataPath;
    private Bitmap bitmap;
    private long albumId;
    private int duration;
    private Uri albumArtUri;
    public int index;

    public Song(String artist, Bitmap bitmap, String album, String title, String dataPath, long albumId, int duration, Uri albumArtUri) {
        this.index = COUNT++;
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

    @Override
    public int compareTo(Song another) {
        return this.artist.compareTo(another.artist);
    }
}
