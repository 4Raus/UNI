package ru.alexandra_h;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Player_2 {
    public static ArrayList<Playlist> playlists = new ArrayList<>();
    public static Playlist loadedPlaylist = null;
    public static int playingSong = -1;
    public static Scanner scanner = new Scanner(System.in);
    public static void createPlaylist() {
        System.out.print("Введите название плейлиста: ");
        String name = scanner.next();
        System.out.print("Введите имя автора плейлиста: ");
        String author = scanner.next();
        loadedPlaylist = new Playlist(name, author);
        playingSong = -1;
        System.out.println("Плейлист успешно создан!");
    }
    public static void loadPlaylist() {
        if (playlists.isEmpty()) {
            System.out.println("Плейлистов нет!");
            return;
        }
        System.out.print("Введите номер плейлиста: ");
        int number = scanner.nextInt();
        if (number < 1 || number > playlists.size()) {
            System.out.println("Неверный номер!");
            return;
        }
        loadedPlaylist = playlists.get(number - 1);
        playingSong = -1;
        System.out.println("Плейлист успешно загружен!");
    }

    public static void savePlaylist() {
        if (loadedPlaylist == null) return;
        for (int i = 0; i < playlists.size(); i++) {
            if (loadedPlaylist.getName().equals(playlists.get(i).getName())) {
                playlists.set(i, loadedPlaylist);
                System.out.println("Плейлист успешно сохранен!");
                return;
            }
        }
        playlists.add(loadedPlaylist);
        System.out.println("Плейлист успешно сохранен!");
    }

    public static void deletePlaylist(){
        int number;

        if(playlists.isEmpty()){
            System.out.println("Плейлистов нету");
            return;
        }

        clearInput();
        System.out.print("Введите номер плейлиста: ");
        try{
            number = scanner.nextInt();
        } catch(InputMismatchException e){
            clearInput();
            System.out.println("Введено неверное число!");
            return;
        }

        if(number < 1 || number > playlists.size()){
            System.out.println("Введено неверное число!");
            return;
        }

        playlists.remove(number - 1);
        System.out.println("Плейлист успешно удален");
    }

    public static void addSong() {
        if (loadedPlaylist == null) return;

        String name, author;
        int duration;

        clearInput();
        System.out.print("Введите название песни: ");
        name = scanner.nextLine();

        System.out.print("Введите имя исполнителя: ");
        author = scanner.nextLine();

        do {
            System.out.print("Введите длительность песни в секундах: ");
            try {
                duration = scanner.nextInt();
            } catch (InputMismatchException e) {
                clearInput();
                System.out.println("Введено неверное число! Попробуйте еще раз");
                continue;
            }
            break;
        } while (true);

        loadedPlaylist.addSong(new Song(name, duration, author));
        System.out.println("Песня успешно добавлена");
    }

    public static void deleteSong() {
        if (loadedPlaylist == null) return;
        if (loadedPlaylist.size() == 0) {
            System.out.println("Плейлист пуст");
            return;
        }

        int number;

        clearInput();
        System.out.print("Введите номер песни: ");
        try {
            number = scanner.nextInt();
        } catch (InputMismatchException e) {
            clearInput();
            System.out.println("Введено неверное число!");
            return;
        }

        if (number < 1 || number > loadedPlaylist.size()) {
            System.out.println("Неверный номер!");
            return;
        }

        loadedPlaylist.removeSong(number - 1);
        if (playingSong >= number - 1) {
            if (playingSong > 0) {
                playingSong--;
            } else {
                playingSong = -1;
            }
        }
        System.out.println("Песня успешно удалена");
    }

    public static void playSong(){
        if(loadedPlaylist == null) return;
        if(loadedPlaylist.size() == 0){
            System.out.println("Плейлист пуст");
            return;
        }

        int number;

        clearInput();
        System.out.print("Введите номер песни: ");
        try{
            number = scanner.nextInt();
        } catch(InputMismatchException e){
            clearInput();
            System.out.println("Введено неверное число!");
            return;
        }

        if(number < 1 || number > loadedPlaylist.size()){
            System.out.println("Введено неверное число!");
            return;
        }

        playingSong = number - 1;
    }

    public static void nextSong(){
        if(loadedPlaylist == null || playingSong == -1) return;
        if(loadedPlaylist.size() == 0){
            System.out.println("Плейлист пуст");
            return;
        }

        playingSong = (playingSong + 1) % loadedPlaylist.size();
    }

    public static void prevSong(){
        if(loadedPlaylist == null || playingSong == -1) return;
        if(loadedPlaylist.size() == 0){
            System.out.println("Плейлист пуст");
            return;
        }

        playingSong = (playingSong - 1) % loadedPlaylist.size();
        if(playingSong < 0) playingSong = loadedPlaylist.size() + playingSong;
    }

    public static void exportPlaylists(){
        if(playlists.isEmpty()){
            System.out.println("Плейлистов нету");
            return;
        }

        clearInput();
        System.out.print("Введите имя файла: ");
        String fileName = scanner.nextLine();

        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(playlists);

            System.out.println("Плейлисты были сохранены в файл с именем " + fileName);
            fileOut.close();
            objectOut.close();
        }
        catch (IOException e) {
            System.out.println("Произошла ошибка во время сохранения в файл");
        }
    }

    public static void importPlaylists(){
        clearInput();
        System.out.print("Введите имя файла: ");
        String fileName = scanner.nextLine();

        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            playlists = (ArrayList<Playlist>) objectIn.readObject();

            System.out.println("Плейлисты были загружены из файла");
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Произошла ошибка во время загрузки из файла");
        }
    }

    public static void clearInput(){
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}