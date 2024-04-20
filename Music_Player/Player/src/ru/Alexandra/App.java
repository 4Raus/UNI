package ru.Alexandra;

import javax.swing.*;

public class App {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MP3GUI().setVisible(true);
            }
        });
    }
}
