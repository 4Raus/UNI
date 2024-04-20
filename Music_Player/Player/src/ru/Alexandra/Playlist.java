package ru.Alexandra;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Playlist extends JDialog {
    private MP3GUI musicPlayerGUI;
    private ArrayList<String> songPaths;

    public Playlist(MP3GUI musicPlayerGUI) {
        this.musicPlayerGUI = musicPlayerGUI;
        songPaths = new ArrayList<>();

        //configure dialog
        setTitle("CREATE PLAYLIST");
        setSize(400, 400);
        setResizable(false);
        getContentPane().setBackground(MP3GUI.FRAME_COLOR);
        setLayout(null);
        setModal(true); //this property makes it so that the dialog has to be closed to give focus
        setLocationRelativeTo(musicPlayerGUI);

        addDialogComponents();
    }

    private void addDialogComponents() {
        //container to hold each song path
        JPanel songContainer = new JPanel();
        songContainer.setLayout(new BoxLayout(songContainer, BoxLayout.Y_AXIS));
        songContainer.setBounds((int) (getWidth() * 0.025), 10, (int) (getWidth() * 0.90), (int) (getHeight() * 0.75));
        add(songContainer);

        //add song button
        JButton addSongButton = new JButton("ADD");
        addSongButton.setBounds(60, (int) (getHeight() * 0.80), 100, 25);
        addSongButton.setFont(new Font("DIALOG", Font.BOLD, 14));
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
                jFileChooser.setCurrentDirectory(new File("Player/src/sets"));
                int result = jFileChooser.showOpenDialog(Playlist.this);

                File selectedFile = jFileChooser.getSelectedFile();
                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null) {
                    JLabel filePathLabel = new JLabel(selectedFile.getPath());
                    filePathLabel.setFont(new Font("DIALOG", Font.BOLD, 12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                    //add to the list
                    songPaths.add(filePathLabel.getText());

                    //add to the container
                    songContainer.add(filePathLabel);

                    //refreshes dialog to show newly added JLabel
                    songContainer.revalidate();
                }
            }
        });
        add(addSongButton);

        //save playlist button
        JButton savePlaylistButton = new JButton("SAVE");
        savePlaylistButton.setBounds(215, (int) (getHeight() * 0.80), 100, 25);
        savePlaylistButton.setFont(new Font("DIALOG", Font.BOLD, 14));
        savePlaylistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setCurrentDirectory(new File("Player/src/sets"));
                    int result = jFileChooser.showSaveDialog(Playlist.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        //we use getSelectedFile() to get reference to the file that we are about to save
                        File selectedFile = jFileChooser.getSelectedFile();

                        //convert to .txt file if not done do already
                        //this will check to see if the file doesn't have the ".txt" file extension
                        if (!selectedFile.getName().substring(selectedFile.getName().length() - 4).equalsIgnoreCase(".txt")) {
                            selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");
                        }

                        //create the new file at the destinated directory
                        selectedFile.createNewFile();

                        //now we will write all the songs' paths into this file
                        FileWriter fileWriter = new FileWriter(selectedFile);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        //iterate through our song paths list and write each string into the file
                        //each song will be written in their own row
                        for (String songPath : songPaths) {
                            bufferedWriter.write(songPath + "\n");
                        }
                        bufferedWriter.close();

                        //display success dialog
                        JOptionPane.showMessageDialog(Playlist.this, "SUCCESSFULLY CREATED PLAYLIST");

                        //close this dialog
                        Playlist.this.dispose();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        add(savePlaylistButton);
    }
}