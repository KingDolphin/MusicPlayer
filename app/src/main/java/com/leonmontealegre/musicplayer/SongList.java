package com.leonmontealegre.musicplayer;

import java.util.ArrayList;

public final class SongList {

    private static SongList instance;

    private ArrayList<Song> songs;

    private SongList() {}

    public static void add(Song song) {
        if (instance == null)
            instance = new SongList();
        instance.songs.add(song);
    }

    public void play(int index) {
        if (songs != null && index >= 0 && index < songs.size())
            songs.get(index).play();
    }

}
