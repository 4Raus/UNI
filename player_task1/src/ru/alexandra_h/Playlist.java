package ru.alexandra_h;

import java.util.ArrayList;
import java.io.Serializable;

public class Playlist implements Serializable {
    private final ArrayList<Song> songs;
    private final String author;
    private final String name;
    private int duration;

    public Playlist(String name, String author) {
        this.name = name;
        this.author = author;
        this.songs = new ArrayList<>();
    }

    public int size() {return songs.size();}
    public Song getSong(int index) {return songs.get(index);}
    public void addSong(Song song) {songs.add(song);}
    public void removeSong(int index){songs.remove(index);}
    public ArrayList<Song> getSongs() {
        return songs;
    }
    public String getAuthor() {
        return author;
    }
    public String getName() {
        return name;
    }

    public int getTotalDuration() {
        int totalDuration = 0;
        for (Song song : getSongs()) {
            totalDuration += song.getDuration();
        }
        return totalDuration;
    }

    public void showSongs() {
        if(songs.isEmpty()){
            System.out.println("Плейлист пуст.\n");
            return;
        }
        for(int i = 1; i < songs.size() + 1; i++) {
            System.out.println(i + " - " + getSong(i - 1));
        }
    }

    @Override
    public String toString(){
        return "Playlist: " + name + "\nAuthor: " + author + "\nDuration: " + getTotalDuration() + "\n";
    }
}