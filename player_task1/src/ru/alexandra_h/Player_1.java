//package ru.alexandra_h;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class Player_1 {
//
//    public static void main(String[] args) {
//        Player_1 player = new Player_1();
//        player.loadPlaylists("player.txt"); // Загружает все плейлисты из файла "player.txt"
//
//        // Выберите плейлист для воспроизведения
//        String playlistName = "My Playlist";
//
//        // Запуск воспроизведения выбранного плейлиста
//        player.playPlaylist(playlistName);
//    }
//
//    public void loadPlaylists(String filename) {
//        try (Scanner scanner = new Scanner(new File(filename))) {
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                if (line.startsWith("Playlist: ")) {
//                    String playlistName = line.substring(9);
//                    Playlist playlist = new Playlist(playlistName);
//
//                    while (scanner.hasNextLine()) {
//                        line = scanner.nextLine();
//                        if (line.isEmpty()) {
//                            break; // End of playlist
//                        }
//
//                        if (line.startsWith("Song: ")) {
//                            String songName = line.substring(6);
//                            int duration = Integer.parseInt(scanner.nextLine().substring(9));
//                            Song song = new Song(songName, duration);
//                            playlist.addSong(song);
//                        }
//                    }
//
//                    playlists.add(playlist);
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Ошибка загрузки плейлистов: " + e.getMessage());
//        }
//    }
//
//    private ArrayList<Playlist> playlists = new ArrayList<>();
//    private Playlist currentPlaylist;
//    private int currentSongIndex;
//    private int index;
//
//    public Song getSongByName(String name) {
//        for (Playlist playlist : playlists) {
//            for (Song song : playlist.getSongs()) {
//                if (song.getName().equalsIgnoreCase(name)) {
//                    return song;
//                }
//            }
//        }
//        return null;
//    }
//
//    public void getSongDuration(String songName) {
//        Song song = getSongByName(songName);
//        if (song == null) {
//            System.out.println("Песня с таким названием не найдена.");
//            return;
//        }
//
//        int duration = song.getDuration();
//        System.out.println("Продолжительность песни '" + songName + "': " + duration + " секунд");
//    }
//
//    public Playlist getPlaylistByName(String name) {
//        for (Playlist playlist : playlists) {
//            if (playlist.getName().equalsIgnoreCase(name)) {
//                return playlist;
//            }
//        }
//        return null;
//    }
//
//    public int getPlaylistDuration(String playlistName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return -1;
//        }
//
//        int totalDuration = 0;
//        for (Song song : playlist.getSongs()) {
//            totalDuration += song.getDuration();
//        }
//
//        return totalDuration;
//    }
//
//    public void showAllSongsFromPlayerFile() {
//        try (Scanner scanner = new Scanner(new File("player.txt"))) {
//            boolean readingSongs = false; // Flag to indicate song section
//
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//
//                if (line.startsWith("Song:")) { // Start reading songs
//                    readingSongs = true;
//                }
//
//                if (readingSongs) {
//                    String[] parts = line.split(": "); // Split based on ": "
//                    if (parts.length == 2) { // Ensure valid song format
//                        String songName = parts[0].substring(6).trim(); // Extract name after "Song:"
//                        int duration = Integer.parseInt(parts[1].substring(10).trim()); // Extract duration after "Duration:"
//                        Song song = new Song(songName, duration);
//                        System.out.println(song); // Print song details
//                    } else {
//                        // Handle invalid song lines (optional):
//                        // System.out.println("Skipping invalid song line: " + line);
//                    }
//                }
//
//                if (line.isEmpty()) { // Stop reading songs when an empty line is encountered
//                    readingSongs = false;
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading player file: " + e.getMessage());
//        } catch (NumberFormatException e) {
//            System.err.println("Error parsing song duration: " + e.getMessage());
//        }
//    }
//
//    public void savePlaylist(Playlist playlist) {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(playlist.getName() + ".txt"))) {
//            writer.println("Playlist: " + playlist.getName());
//            writer.println("Author: " + playlist.getAuthor());
//            writer.println("Duration: " + playlist.getDuration());
//            for (Song song : playlist.getSongs()) {
//                writer.println("Song: " + song.getName() + ", Duration: " + song.getDuration());
//            }
//            System.out.println("Плейлист '" + playlist.getName() + "' сохранен.");
//        } catch (IOException e) {
//            System.err.println("Ошибка сохранения плейлиста: " + e.getMessage());
//        }
//    }
//
//    public void createPlaylist(String playlistName, String author, ArrayList<Song> songs) {
//        if (playlistName == null || playlistName.isEmpty()) {
//            System.out.println("Введите название плейлиста.");
//            return;
//        }
//
//        if (author == null || author.isEmpty()) {
//            System.out.println("Введите имя автора плейлиста.");
//            return;
//        }
//
//        if (songs == null || songs.isEmpty()) {
//            System.out.println("Добавьте хотя бы одну песню в плейлист.");
//            return;
//        }
//
//        int totalDuration = 0;
//        for (Song song : songs) {
//            totalDuration += song.getDuration();
//        }
//
//        Playlist playlist = new Playlist(playlistName, author, totalDuration, songs);
//        playlists.add(playlist);
//
//        savePlaylist(playlist); // Сохранение плейлиста
//        System.out.println("Плейлист '" + playlistName + "' создан.");
//    }
//
//    public void deletePlaylist(String playlistName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        // Удаление файла плейлиста
//        File playlistFile = new File(playlistName + ".txt");
//        if (!playlistFile.delete()) {
//            System.err.println("Ошибка удаления файла плейлиста: " + playlistFile.getName());
//            return;
//        }
//
//        // Удаление плейлиста из памяти
//        playlists.remove(playlist);
//
//        // Обновление всех TXT-файлов (предполагается, что каждый файл содержит информацию о всех плейлистах)
//        // Примечание: этот подход может быть неоптимальным для больших объемов данных.
//        File[] files = new File(".").listFiles((dir, name) -> name.endsWith(".txt"));
//        for (File file : files) {
//            try (Scanner scanner = new Scanner(file);
//                 PrintWriter writer = new PrintWriter(new FileWriter(file.getName() + ".tmp"))) {
//                while (scanner.hasNextLine()) {
//                    String line = scanner.nextLine();
//                    if (!line.startsWith("Playlist: " + playlistName)) { // Пропустить информацию о удаляемом плейлисте
//                        writer.println(line);
//                    }
//                }
//                writer.flush(); // Убедитесь, что все данные записаны перед переименованием
//            } catch (IOException e) {
//                System.err.println("Ошибка обновления файла " + file.getName() + ": " + e.getMessage());
//            }
//
//            if (!file.delete()) {
//                System.err.println("Ошибка удаления временного файла " + file.getName());
//                continue; // Переход к следующему файлу, если временный файл не удалился
//            }
//
//            if (!new File(file.getName() + ".tmp").renameTo(file)) {
//                System.err.println("Ошибка переименования временного файла " + file.getName() + ".tmp");
//            }
//        }
//
//        System.out.println("Плейлист '" + playlistName + "' удален.");
//    }
//
//    public void playPlaylist(String playlistName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        System.out.println("**Проигрывание плейлиста '" + playlistName + "'**");
//
//        // Воспроизведение каждой песни в плейлисте
//        for (Song song : playlist.getSongs()) {
//            System.out.println("**Воспроизводится:** " + song.getName());
//            // **Вставьте код для воспроизведения песни (например, используя API)**
//            // ....
//
//            // Задержка между песнями
//            try {
//                Thread.sleep(1000); // 1 секунда
//            } catch (InterruptedException e) {
//                System.err.println("Ошибка паузы между песнями: " + e.getMessage());
//            }
//        }
//
//        System.out.println("**Плейлист '" + playlistName + "' окончен.**");
//    }
//
//    public void addSongToPlaylist(String playlistName, Song song) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        if (song == null) {
//            System.out.println("Не удалось добавить песню. Песня не найдена.");
//            return;
//        }
//
//        // Проверка, есть ли уже песня в плейлисте
//        if (playlist.getSongs().contains(song)) {
//            System.out.println("Песня '" + song.getName() + "' уже есть в плейлисте.");
//            return;
//        }
//
//        playlist.addSong(song);
//        playlist.setDuration(playlist.getDuration() + song.getDuration());
//
//        // Обновление TXT-файлов (предполагается, что каждый файл содержит информацию о всех плейлистах)
//        // Примечание: этот подход может быть неоптимальным для больших объемов данных.
//        File[] files = new File(".").listFiles((dir, name) -> name.endsWith(".txt"));
//        for (File file : files) {
//            try (Scanner scanner = new Scanner(file);
//                 PrintWriter writer = new PrintWriter(new FileWriter(file.getName() + ".tmp"))) {
//                while (scanner.hasNextLine()) {
//                    String line = scanner.nextLine();
//                    if (line.startsWith("Playlist: " + playlistName)) {
//                        writer.println("Playlist: " + playlistName);
//                        writer.println("Author: " + playlist.getAuthor());
//                        writer.println("Duration: " + playlist.getDuration());
//                        for (Song pSong : playlist.getSongs()) {
//                            writer.println("Song: " + pSong.getName() + ", Duration: " + pSong.getDuration());
//                        }
//                    } else {
//                        writer.println(line);
//                    }
//                }
//                writer.flush(); // Убедитесь, что все данные записаны перед переименованием
//            } catch (IOException e) {
//                System.err.println("Ошибка обновления файла " + file.getName() + ": " + e.getMessage());
//            }
//
//            if (!file.delete()) {
//                System.err.println("Ошибка удаления временного файла " + file.getName());
//                continue; // Переход к следующему файлу, если временный файл не удалился
//            }
//
//            if (!new File(file.getName() + ".tmp").renameTo(file)) {
//                System.err.println("Ошибка переименования временного файла " + file.getName() + ".tmp");
//            }
//        }
//
//        System.out.println("Песня '" + song.getName() + "' добавлена в плейлист '" + playlistName + "'.");
//    }
//
//    public void showPlaylist(String playlistName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        System.out.println("**Плейлист '" + playlistName + "'**");
//        System.out.println("Автор: " + playlist.getAuthor());
//        System.out.println("Продолжительность: " + playlist.getDuration() + " секунд");
//        System.out.println("---------------------");
//        for (Song song : playlist.getSongs()) {
//            System.out.println(song.getName() + " (" + song.getDuration() + " секунд)");
//        }
//        System.out.println("---------------------");
//    }
//
//    public void removeSongFromPlaylist(String playlistName, String songName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        Song song = getSongByName(songName);
//        if (song == null) {
//            System.out.println("Песня с таким названием не найдена.");
//            return;
//        }
//
//        // Проверка, есть ли песня в плейлисте
//        if (!playlist.getSongs().contains(song)) {
//            System.out.println("Песня '" + songName + "' не находится в плейлисте '" + playlistName + "'.");
//            return;
//        }
//
//        playlist.getSongs().remove(song);
//        playlist.setDuration(playlist.getDuration() - song.getDuration());
//
//        // Обновление TXT-файлов (предполагается, что каждый файл содержит информацию о всех плейлистах)
//        // Примечание: этот подход может быть неоптимальным для больших объемов данных.
//        File[] files = new File(".").listFiles((dir, name) -> name.endsWith(".txt"));
//        for (File file : files) {
//            try (Scanner scanner = new Scanner(file);
//                 PrintWriter writer = new PrintWriter(new FileWriter(file.getName() + ".tmp"))) {
//                while (scanner.hasNextLine()) {
//                    String line = scanner.nextLine();
//                    if (line.startsWith("Playlist: " + playlistName)) {
//                        writer.println("Playlist: " + playlistName);
//                        writer.println("Author: " + playlist.getAuthor());
//                        writer.println("Duration: " + playlist.getDuration());
//                        for (Song pSong : playlist.getSongs()) {
//                            writer.println("Song: " + pSong.getName() + ", Duration: " + pSong.getDuration());
//                        }
//                    } else {
//                        writer.println(line);
//                    }
//                }
//                writer.flush(); // Убедитесь, что все данные записаны перед переименованием
//            } catch (IOException e) {
//                System.err.println("Ошибка обновления файла " + file.getName() + ": " + e.getMessage());
//            }
//
//            if (!file.delete()) {
//                System.err.println("Ошибка удаления временного файла " + file.getName());
//                continue; // Переход к следующему файлу, если временный файл не удалился
//            }
//
//            if (!new File(file.getName() + ".tmp").renameTo(file)) {
//                System.err.println("Ошибка переименования временного файла " + file.getName() + ".tmp");
//            }
//        }
//
//        System.out.println("Песня '" + songName + "' удалена из плейлиста '" + playlistName + "'.");
//    }
//
//    public void playSong(String songName, String author) {
//        Song song = getSongByName(songName);
//        if (song == null) {
//            System.out.println("Песня с таким названием не найдена.");
//            return;
//        }
//
//        // ** код для воспроизведения песни (например, используя API)**
//        // ....
//
//        System.out.println("**Воспроизводится:** " + songName + " (" + author + ")");
//        // Задержка, имитирующая продолжительность песни
//        try {
//            Thread.sleep(song.getDuration() * 1000); // 1 секунда = 1000 миллисекунд
//        } catch (InterruptedException e) {
//            System.err.println("Ошибка паузы: " + e.getMessage());
//        }
//
//        System.out.println("**Песня '" + songName + "' окончена.**");
//    }
//
//    public void playPreviousSong(String playlistName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        if (currentPlaylist != playlist) {
//            currentPlaylist = playlist;
//            currentSongIndex = playlist.getSongs().size() - 1; // Start from the last song
//        }
//
//        if (currentSongIndex < 0) {
//            System.out.println("В плейлисте нет песен.");
//            return;
//        }
//
//        Song song = playlist.getSongs().get(currentSongIndex);
//        playSong(song.getName(), song.getAuthor());
//
//        currentSongIndex--; // Move to the previous song
//    }
//
//    public void playNextSong(String playlistName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        if (currentPlaylist != playlist) {
//            currentPlaylist = playlist;
//            currentSongIndex = 0; // Start from the first song
//        }
//
//        if (currentSongIndex >= playlist.getSongs().size()) {
//            System.out.println("В плейлисте нет больше песен.");
//            return;
//        }
//
//        Song song = playlist.getSongs().get(currentSongIndex);
//        playSong(song.getName(), song.getAuthor());
//
//        currentSongIndex++; // Move to the next song
//    }
//
//    public void playCurrentSongAgain(String playlistName) {
//        Playlist playlist = getPlaylistByName(playlistName);
//        if (playlist == null) {
//            System.out.println("Плейлист с таким названием не найден.");
//            return;
//        }
//
//        if (currentPlaylist != playlist) {
//            System.out.println("Песня не была воспроизведена из этого плейлиста.");
//            return;
//        }
//
//        if (currentSongIndex < 0 || currentSongIndex >= playlist.getSongs().size()) {
//            System.out.println("Невозможно воспроизвести песню. Индекс песни некорректен.");
//            return;
//        }
//
//        Song song = playlist.getSongs().get(currentSongIndex);
//        playSong(song.getName(), song.getAuthor());
//    }
//
//    public void pauseSong() {
//        // **код для остановки воспроизведения песни (паузы)**
//        // ....
//
//        System.out.println("**Воспроизведение песни приостановлено.**");
//    }
//}