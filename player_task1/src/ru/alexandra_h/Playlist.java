package ru.alexandra_h;

import java.util.ArrayList;

public class Playlist {
    private final ArrayList<Song> songs;
    private final String athor;
    private final String name;

    public ArrayList<Song> getSongs() {return songs;}
    public String getAthor() {return athor;}
    public String getName() {return name;}
    public void addSong(Song song) {songs.add(song);}

    public Playlist(String athor, String name) {
        this.athor = athor;
        this.name = name;
        this.songs = new ArrayList<>();
    }
}