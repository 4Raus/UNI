package ru.alexandra_h;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    public static void main(String[] args) {
        Player player = new Player();
        player.loadPlaylists("player.txt");
    }

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private Playlist currentPlaylist;
    private int currentSongIndex;
    private int index;

    private Song getSongByName(String name, Playlist playlist) {
        for (Song song : playlist.getSongs()) {
            if (song.getName().equalsIgnoreCase(name)) {
                return song;
            }
        }
        return null;
    }

    public Song getSongByIndex(int index, Playlist playlist) {
        if (index < 0 || index >= playlist.getSongs().size()) {
            return null;
        }
        return playlist.getSongs().get(index);
    }

    private Playlist getPlaylistByIndex(int index) {
        if (index < 0 || index >= playlists.size()) {
            return null;
        }
        return playlists.get(index);
    }

    private Playlist getPlaylistByName(String name) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        return null;
    }

    public int getTotalDuration(String playlistName) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("Playlist not found.");
            return 0;
        }
        int totalDuration = 0;
        for (Song song : playlist.getSongs()) {
            totalDuration += song.getDuration();
        }
        return totalDuration;
    }

    public void showAllSongs() {
        for (Playlist playlist : playlists) {
            for (Song song : playlist.getSongs()) {
                System.out.println(song);
            }
        }
    }

    public void showAllSongs(String name) {
        Playlist playlist = getPlaylistByName(name);
        if (playlist == null) {
            System.out.println("/Nothing was found. Error/");
            return;
        }
        for (Song song : playlist.getSongs()) {
            System.out.println(song);
        }
    }

    public void createPlaylist(String playlistName, String author) {
        Playlist playlist = new Playlist(playlistName, author);
        playlists.add(playlist);
        File playlistFile = new File(playlistName + ".txt");
        try {
            if (!playlistFile.exists()) {
                playlistFile.createNewFile();
                FileWriter writer = new FileWriter(playlistFile);
                writer.write("Playlist: " + playlistName + "\n");
                writer.close();
            }
            PrintWriter playerWriter = new PrintWriter(new FileWriter("player.txt", true));
            playerWriter.println(playlistName + ", " + author);
            playerWriter.close();
        } catch (IOException e) {
            System.out.println("Error creating playlist: " + e.getMessage());
        }
    }

    public void loadPlaylists(String playlistInput) {
        try {
            Scanner scanner = new Scanner(new File(playlistInput));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] segments = line.split(", ");
                Playlist playlist = new Playlist(segments[0], segments[1]);
                playlists.add(playlist);
                File playlistFile = new File(segments[0] + ".txt");
                if (!playlistFile.exists()) playlistFile.createNewFile();
                PrintWriter writer = new PrintWriter(playlistFile);
                writer.println("Playlist: " + segments[0]);
                writer.println("Author: " + segments[1]);
                for (int i = 2; i < segments.length; i += 2) {
                    Song song = new Song(segments[i], Integer.parseInt(segments[i + 1]));
                    playlist.getSongs().add(song);
                    writer.println("Song: " + segments[i] + ", Duration: " + segments[i + 1]);
                }
                writer.close();
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("/Was not read. Error/" + e.getMessage());
        }
    }

    public void loadSongsFromPlaylist(String playlistName) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("Playlist not found.");
            return;
        }
        try {
            File playlistFile = new File(playlistName + "_songs.txt");
            FileWriter writer = new FileWriter(playlistFile);
            writer.write("Playlist: " + playlistName + "\n");
            for (Song song : playlist.getSongs()) {
                writer.write("Song: " + song.getName() + ", Duration: " + song.getDuration() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing songs to file: " + e.getMessage());
        }
    }

    public void deletePlaylist(int index, String name) {
        Playlist playlist;
        if (index >= 0 && index < playlists.size()) {
            playlist = playlists.get(index);
            playlists.remove(playlist);
        } else {
            System.out.println("Playlist with that index does not exist.");
            return;
        }

        if (name != null && !name.isEmpty()) {
            playlist = getPlaylistByName(name);
            if (playlist != null) {
                playlists.remove(playlist);
            } else {
                System.out.println("Playlist with that name does not exist.");
                return;
            }
        }

        savePlaylists();
        System.out.println("Playlist deleted.");
    }

    public void savePlaylist(String nameOrIndex) {
        Playlist playlist;
        try {
            int index = Integer.parseInt(nameOrIndex);
            playlist = getPlaylistByIndex(index);
            if (playlist == null) {
                System.out.println("Playlist with that index does not exist.");
                return;
            }
        } catch (NumberFormatException e) {
            playlist = getPlaylistByName(nameOrIndex);
            if (playlist == null) {
                System.out.println("Playlist with that name does not exist.");
                return;
            }
        }

        String fileName = playlist.getName() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Playlist: " + playlist.getName() + "\n");
            writer.write("Author: " + playlist.getAthor() + "\n");
            for (Song song : playlist.getSongs()) {
                writer.write("Song: " + song.getName() + ", Duration: " + song.getDuration() + "\n");
            }
            System.out.println("Playlist saved to file " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving playlist: " + e.getMessage());
        }

        savePlaylists();
    }

    private void savePlaylists() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("player.txt"))) {
            for (Playlist playlist : playlists) {
                writer.write(playlist.getName() + ", " + playlist.getAthor());
                for (Song song : playlist.getSongs()) {
                    writer.write(", " + song.getName() + ", " + song.getDuration());
                }
                writer.newLine();
            }
            System.out.println("All playlists saved to file player.txt");
        } catch (IOException e) {
            System.err.println("Error saving playlists: " + e.getMessage());
        }
    }

    public void playPlaylist(String indexOrName) {
        Playlist playlist;
        try {
            int index = Integer.parseInt(indexOrName);
            playlist = getPlaylistByIndex(index);
            if (playlist == null) {
                System.out.println("Playlist with that index does not exist.");
                return;
            }
        } catch (NumberFormatException e) {
            playlist = getPlaylistByName(indexOrName);
            if (playlist == null) {
                System.out.println("Playlist with that name does not exist.");
                return;
            }
        }
        playPlaylist(playlist);
    }

    private void playPlaylist(Playlist playlist) {
        currentPlaylist = playlist;
        currentSongIndex = 0;
        System.out.println("Playlist '" + playlist.getName() + "' selected.");
    }

    public void addSongToPlaylist(String playlistName, String songName, String artist) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("Playlist with that name does not exist.");
            return;
        }

        Song song = getSongByName(songName, playlist);
        if (song == null) {
            System.out.println("Song with that name does not exist.");
            return;
        }

        playlist.addSong(song);
        System.out.println("Song '" + song.getName() + "' added to playlist '" + playlist.getName() + "'.");
    }

    public void removeSongFromPlaylist(String playlistName, String songName) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("Playlist with that name does not exist.");
            return;
        }

        Song song = getSongByName(songName, playlist);
        if (song == null) {
            System.out.println("Song with that name does not exist.");
            return;
        }

        if (playlist.getSongs().contains(song)) {
            playlist.getSongs().remove(song);
            System.out.println("Song '" + song.getName() + "' removed from playlist '" + playlist.getName() + "'.");
        } else {
            System.out.println("Song not found in playlist.");
        }
    }

    public void playPreviousSong() {
        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) {
            System.out.println("No playlist selected or playlist is empty.");
            return;
        }

        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = currentPlaylist.getSongs().size() - 1;
        }

        Song previousSong = currentPlaylist.getSongs().get(currentSongIndex);
        System.out.println("Now playing previous song: " + previousSong.getName());
    }

    public void playNextSong() {
        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) {
            System.out.println("No playlist selected or playlist is empty.");
            return;
        }

        currentSongIndex++;
        if (currentSongIndex >= currentPlaylist.getSongs().size()) {
            currentSongIndex = 0;
        }

        Song nextSong = currentPlaylist.getSongs().get(currentSongIndex);
        System.out.println("Now playing next song: " + nextSong.getName());
    }

    public void repeatCurrentSong() {
        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) {
            System.out.println("No playlist selected or playlist is empty.");
            return;
        }

        if (currentSongIndex < 0 || currentSongIndex >= currentPlaylist.getSongs().size()) {
            System.out.println("Invalid song index in playlist.");
            return;
        }

        Song currentSong = currentPlaylist.getSongs().get(currentSongIndex);
        System.out.println("Now playing current song: " + currentSong.getName());
    }
}
