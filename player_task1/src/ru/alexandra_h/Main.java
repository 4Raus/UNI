package ru.alexandra_h;

import java.util.InputMismatchException;
import java.util.Scanner;

import static ru.alexandra_h.Player_2.*;

public class Main {
    public static void main(String[] args) {
        Player_2 player = new Player_2();
        int choice;

        do {
            if(loadedPlaylist != null){
                System.out.println("Текущий плейлист: " + loadedPlaylist.getName());
                if(playingSong > -1){
                    System.out.println("Играет песня: " + loadedPlaylist.getSong(playingSong).getName());
                }
            } else {
                System.out.println("Плейлист не загружен");
            }

            System.out.print(
                    "\n--- Меню плеера ---"+
                    "\n 0 - Выйти\n" +
                            " 1 - Показать список песен\n" +
                            " 2 - Показать список плейлистов\n" +
                            " 3 - Создать плейлист\n" +
                            " 4 - Включить плейлист\n" +
                            " 5 - Сохранить плейлист\n" +
                            " 6 - Удалить плейлист\n" +
                            " 7 - Добавить песню в плейлист\n" +
                            " 8 - Удалить песню из плейлиста\n" +
                            " 9 - Включить песню по номеру\n" +
                            " 10 - Включить предыдущий трек\n" +
                            " 11 - Включить следующий трек\n" +
                            " 12 - Сохранить плейлисты в файл\n" +
                            " 13 - Загрузить плейлисты из файла\n" +
                            " >> "
            );

            try{
                choice = scanner.nextInt();
                System.out.print("\033[H\033[2J");
            } catch(InputMismatchException e){
                clearInput();
                System.out.print("\033[H\033[2J");
                System.out.println("Введено неверное число!");
                continue;
            }

            if(choice == 0) break;

            switch(choice){
                case 1:
                    if(loadedPlaylist != null){
                        loadedPlaylist.showSongs();
                    }
                    break;
                case 2:
                    if(playlists.isEmpty()){
                        System.out.println("Плейлистов нету");
                        continue;
                    }
                    for(int i = 1; i < playlists.size() + 1; i++){
                        System.out.println(i + " - " + playlists.get(i - 1) +
                                "\nКоличество песен: " + playlists.get(i - 1).size() + "\n");
                    }
                    break;
                case 3:
                    createPlaylist();
                    break;
                case 4:
                    loadPlaylist();
                    break;
                case 5:
                    savePlaylist();
                    break;
                case 6:
                    deletePlaylist();
                    break;
                case 7:
                    addSong();
                    break;
                case 8:
                    deleteSong();
                    break;
                case 9:
                    playSong();
                    break;
                case 10:
                    prevSong();
                    break;
                case 11:
                    nextSong();
                    break;
                case 12:
                    exportPlaylists();
                    break;
                case 13:
                    importPlaylists();
                    break;

                default:
                    System.out.println("Введено неверное число!");
                    break;
            }
        } while(true);
    }
}
