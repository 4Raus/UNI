package ru.alexandra_h;

import java.awt.image.BufferedImage;

public class Song {
    private final String name;
    private final int duration;
    //private final BufferedImage image;

    public Song(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {return name;}
    public int getDuration() {return duration;}

    //public BufferedImage getImage() {return image;}
}