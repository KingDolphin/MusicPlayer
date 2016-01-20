package com.leonmontealegre.musicplayer;

import java.util.ArrayList;

public final class SongList {

    private static SongList instance;

    private ArrayList<Song> songs;

    private SongList() {
        songs = new ArrayList<Song>();
    }

    public static void add(Song song) {
        if (instance == null)
            instance = new SongList();
        instance.songs.add(song);
    }

    public static void play(int index) {
        if (instance.songs != null && index >= 0 && index < instance.songs.size())
            instance.songs.get(index).play();
    }

    public static ArrayList<Song> getSongs() {
        return instance.songs;
    }

}
