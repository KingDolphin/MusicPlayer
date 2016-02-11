package com.leonmontealegre.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class SongList {

    // Single instance of it
    private static SongList instance = new SongList();

    private final ArrayList<Song> songs;

    private SongList() {
        songs = new ArrayList<Song>();
    }

    // Adds song to the list, should only be done from MainActivity
    public static void add(Song song) {
        instance.songs.add(song);
    }

    // Plays the song at the given index
    public static void play(int index) {
        if (index >= 0 && index < instance.songs.size())
            instance.songs.get(index).play();
    }

    // Sorts the songs in alphabetical order
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

    // Returns a duplicate of the ArrayList of the songs, it needs to be a
    // duplicate because the ArrayAdapter would delete them all on .clear otherwise
    public static ArrayList<Song> getSongs() {
        return (ArrayList<Song>)instance.songs.clone();
    }

}
