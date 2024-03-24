//package ru.alexandra_h;
//
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class Main2 {
//    public static void main(String[] args) {
//        Player_1 player = new Player_1();
//        player.loadPlaylists("player.txt"); // Загрузка плейлистов из файла
//
//        Scanner scanner = new Scanner(System.in);
//        int choice;
//
//        do {
//            System.out.println("\n--- Меню плеера ---");
//            System.out.println("1. Показать все песни");
//            System.out.println("2. Создать плейлист");
//            System.out.println("3. Удалить плейлист");
//            System.out.println("4. Сохранить плейлист");
//            System.out.println("5. Проиграть плейлист");
//            System.out.println("6. Добавить песню в плейлист");
//            System.out.println("7. Показать плейлист");
//            System.out.println("8. Удалить песню из плейлиста");
//            System.out.println("9. Проиграть песню");
//            System.out.println("10. Проиграть предыдущую песню");
//            System.out.println("11. Проиграть следующую песню");
//            System.out.println("12. Повторить песню");
//            System.out.println("13. Пауза");
//            System.out.println("0. Выход");
//
//            System.out.print("Введите номер пункта меню: ");
//            choice = scanner.nextInt();
//
//            switch (choice) {
//                case 1:
//                    player.showAllSongsFromPlayerFile();
//                    break;
//                case 2:
//                    System.out.print("Введите название плейлиста: ");
//                    String playlistName = scanner.next();
//                    System.out.print("Введите имя автора плейлиста: ");
//                    String author = scanner.next();
//                    ArrayList<Song> songs = new ArrayList<>();
//                    player.createPlaylist(playlistName, author, songs);
//                    break;
//                case 3:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    player.deletePlaylist(playlistName);
//                    break;
//                case 4:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    Playlist playlist = player.getPlaylistByName(playlistName);
//                    if (playlist != null) {
//                        player.savePlaylist(playlist);
//                    } else {
//                        System.out.println("Плейлист с таким названием не найден.");
//                    }
//                    break;
//                case 5:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    player.playPlaylist(playlistName);
//                    break;
//                case 6:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    System.out.print("Введите название песни: ");
//                    String songName = scanner.next();
//                    Song song = player.getSongByName(songName);
//                    if (song != null) {
//                        player.addSongToPlaylist(playlistName, song);
//                    } else {
//                        System.out.println("Песня с таким названием не найдена.");
//                    }
//                    break;
//                case 7:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    player.showPlaylist(playlistName);
//                    break;
//                case 8:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    System.out.print("Введите название песни: ");
//                    songName = scanner.next();
//                    player.removeSongFromPlaylist(playlistName, songName);
//                    break;
//                case 9:
//                    System.out.print("Введите название песни: ");
//                    songName = scanner.nextLine();
//                    System.out.print("Введите имя автора песни: ");
//                    author = scanner.nextLine();
//                    player.playSong(songName, author);
//                    break;
//                case 10:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    player.playPreviousSong(playlistName);
//                    break;
//                case 11:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    player.playNextSong(playlistName);
//                    break;
//                case 12:
//                    System.out.print("Введите название плейлиста: ");
//                    playlistName = scanner.next();
//                    player.playCurrentSongAgain(playlistName);
//                    break;
//                case 13:
//                    player.pauseSong();
//                    break;
//                case 0:
//                    System.out.println("До встречи!");
//                    break;
//                default:
//                    System.out.println("Неверный пункт меню. Пожалуйста, выберите пункт из списка.");
//            }
//        } while (choice != 0);
//
//        scanner.close();
//    }
//}