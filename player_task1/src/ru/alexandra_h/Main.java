package ru.alexandra_h;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player = new Player();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Select an action:");
            System.out.println("0 - Exit");
            System.out.println("1 - Show all songs");
            System.out.println("2 - Create a playlist (by name and author)");
            System.out.println("3 - Load a playlist (by name)");
            System.out.println("4 - Save a playlist (by name)");
            System.out.println("5 - Delete a playlist (by name)");
            System.out.println("6 - Add a song to a playlist (by playlist number, song name, artist)");
            System.out.println("7 - Show all playlists");
            System.out.println("8 - Remove a song from a playlist (by playlist number, song name)");
            System.out.println("9 - Play the previous song");
            System.out.println("10 - Play the next song");
            System.out.println("11 - Repeat the current song");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 0:
                    System.out.println("Exiting the player.");
                    break;
                case 1:
                    player.showAllSongs();
                    break;
                case 2:
                    System.out.println("Enter the name of the new playlist:");
                    String playlistName = scanner.nextLine();
                    System.out.println("Enter the author's name:");
                    String author = scanner.nextLine();
                    player.createPlaylist(playlistName, author);
                    break;
                case 3:
                    System.out.println("Enter the name of the playlist to load:");
                    String playlistInput = scanner.nextLine();
                    player.loadPlaylists(playlistInput);
                    break;
                case 4:
                    System.out.println("Enter the name or index of the playlist to save:");
                    String playlistToSave = scanner.nextLine();
                    player.savePlaylist(playlistToSave);
                    break;
                case 5:
                    System.out.println("Enter the index and name of the playlist to delete:");
                    String playlistToDelete = scanner.nextLine();
                    int playlistIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    player.deletePlaylist(playlistIndex, playlistToDelete);
                    break;
                case 6:
                    System.out.println("Enter the playlist number:");
                    int playlistIndexToAdd = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println("Enter the song name:");
                    String songName = scanner.nextLine();
                    System.out.println("Enter the artist:");
                    String artist = scanner.nextLine();
                    player.addSongToPlaylist(String.valueOf(playlistIndexToAdd), songName, artist);
                    break;
                case 7:
                    player.showAllSongs();
                    break;
                case 8:
                    System.out.println("Enter the playlist number:");
                    int playlistIndexToRemove = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println("Enter the song name:");
                    String songToRemove = scanner.nextLine();
                    player.removeSongFromPlaylist(String.valueOf(playlistIndexToRemove), songToRemove);
                    break;
                case 9:
                    player.playPreviousSong();
                    break;
                case 10:
                    player.playNextSong();
                    break;
                case 11:
                    player.repeatCurrentSong();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
        scanner.close();
    }
}
