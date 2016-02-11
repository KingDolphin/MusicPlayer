package com.leonmontealegre.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class SongList {

    private static SongList instance = new SongList();

    private final ArrayList<Song> songs;

    private SongList() {
        songs = new ArrayList<Song>();
    }

    public static void add(Song song) {
        instance.songs.add(song);
    }

    public static void play(int index) {
        if (index >= 0 && index < instance.songs.size())
            instance.songs.get(index).play();
    }

    public static void sort() {
        Collections.sort(instance.songs, new Comparator<Song>() {
            @Override
            public int compare(Song s1, Song s2) {
                return s1.getTitle().compareToIgnoreCase(s2.getTitle());
            }
        });
        for (int i = 0; i < instance.songs.size(); i++)
            instance.songs.get(i).index = i;
    }

    public static ArrayList<Song> getSongs() {
        return (ArrayList<Song>)instance.songs.clone();
    }

}
