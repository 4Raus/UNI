package ru.Alexandra;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    Song song;

    @org.junit.jupiter.api.Test
    void getSongTitle() {
        String name = "Es ist an der Zeit";
        song = new Song("C:\\Users\\ASUS\\Desktop\\UNI\\СурГу\\Java\\Music_Player\\Player\\src\\sets\\Alligatoah_-_Es_ist_an_der_Zeit.mp3");
        assertEquals(name, song.getSongTitle());
    }

    @org.junit.jupiter.api.Test
    void getSongArtist() {
        String artist = "Alligatoah";
        song = new Song("C:\\Users\\ASUS\\Desktop\\UNI\\СурГу\\Java\\Music_Player\\Player\\src\\sets\\Alligatoah_-_Es_ist_an_der_Zeit.mp3");
        assertEquals(artist, song.getSongArtist());
    }

    @org.junit.jupiter.api.Test
    void getFilePath() {
        String filePath = "C:\\Users\\ASUS\\Desktop\\UNI\\СурГу\\Java\\Music_Player\\Player\\src\\sets\\Alligatoah_-_Es_ist_an_der_Zeit.mp3";
        song = new Song("C:\\Users\\ASUS\\Desktop\\UNI\\СурГу\\Java\\Music_Player\\Player\\src\\sets\\Alligatoah_-_Es_ist_an_der_Zeit.mp3");
        assertEquals(filePath, song.getFilePath());
    }

    @org.junit.jupiter.api.Test
    void getSongLength() {
        String length = "06:27";
        song = new Song("C:\\Users\\ASUS\\Desktop\\UNI\\СурГу\\Java\\Music_Player\\Player\\src\\sets\\Alligatoah_-_Es_ist_an_der_Zeit.mp3");
        assertEquals(length, song.getSongLength());
    }

    @org.junit.jupiter.api.Test
    void getFrameRatePerMilliseconds() {
        double frameRate = 0.03828120961488757;
        song = new Song("C:\\Users\\ASUS\\Desktop\\UNI\\СурГу\\Java\\Music_Player\\Player\\src\\sets\\Alligatoah_-_Es_ist_an_der_Zeit.mp3");
        assertEquals(frameRate, song.getFrameRatePerMilliseconds());
    }
}