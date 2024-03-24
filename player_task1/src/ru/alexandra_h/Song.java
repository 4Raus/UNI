package ru.alexandra_h;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Song implements Serializable {
    private final String name;
    private final String author;
    private final int duration;
    //private final BufferedImage image;

    public Song(String name, int duration, String author) {
        this.name = name;
        this.duration = duration;
        this.author = author;
    }

    public  String getAuthor() {return author;}
    public String getName() {return name;}
    public int getDuration() {return duration;}
    //public BufferedImage getImage() {return image;}

    @Override
    public String toString(){
        return "\nSong: " + name + ", Author: " + author + ", Duration: " + duration;
    }
}