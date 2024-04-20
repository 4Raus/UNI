package ru.Alexandra;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;

public class MP3GUI extends JFrame {
    public static final Color FRAME_COLOR = Color.WHITE;
    public static final Color TEXT_COLOR = Color.BLACK;

    private MusicPlayer musicPlayer;

    //allow us to use file explorer in app
    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackButton;
    private JSlider playbackSlider;

    public MP3GUI(){
        //calls JFrame constructor to configure out gui and set the title header
        super("NEUE MUSIK PLAYER");

        //set the width and height
        setSize(400, 600);

        //end process when app is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //launch the app at the center of the screen
        setLocationRelativeTo(null);

        //prevent the app from being resized
        setResizable(false);

        //set layout to null which allows to control the (x, y) coordinates of components
        //and also set the height and width
        setLayout(null);

        //change the frame color
        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new MusicPlayer(this);
        jFileChooser = new JFileChooser();

        //set a default path for file explorer
        jFileChooser.setCurrentDirectory(new File("Player/src/sets"));

        //filter file chooser to only see .mp3 files
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents() {
        //add toolbar
        addToolbar();

        //load record image
        JLabel songImage = new JLabel(loadImage("Player/src/sets/record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        //song title
        songTitle = new JLabel("SONG TITLE");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("DIALOG", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        //song artist
        songArtist = new JLabel("ARTIST");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("DIALOG", Font.PLAIN, 24));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        //playback slider
        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth() / 2 - 300 /2, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                musicPlayer.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e){
                //when the user drops the tick
                JSlider source = (JSlider) e.getSource();

                //get the frame value from where the user wants to playback to
                int frame = source.getValue();

                //update the current frame in the music player to this frame
                 musicPlayer.setCurrentFrame(frame);

                 //update current time in ms as well
                musicPlayer.setCurrentTimeInMilliseconds((int) (frame / (2.08 * musicPlayer.getCurrentSong().getFrameRatePerMilliseconds())));

                 //resume the song
                musicPlayer.playCurrentSong();

                //toggle on pause button and toggle off play button
                enablePauseButtonDisablePlayButton();
            }
        });
        add(playbackSlider);

        //playback button
        addplaybackButton();
    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 20);

        //prevent toolbar from being moved
        toolBar.setFloatable(false);

        //add drop down menu
        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        //add a song menu, place the loading song option
        JMenu songMenu = new JMenu("SONG");
        menuBar.add(songMenu);

        //add the "load song" item in the songMenu
        JMenuItem loadSong = new JMenuItem("LOAD SONG");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //an integer is returned to us to let us know what the user did
                int result = jFileChooser.showOpenDialog(MP3GUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                //this means that it's checking to see if the user pressed the "open" button
                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    //create a song obj based on selected file
                    Song song = new Song(selectedFile.getPath());

                    //load song in music player
                    musicPlayer.loadSong(song);

                    //update song title and artist
                    updateSongTitleAndArtist(song);

                    //update playback slider
                    updatePlaybackSlider(song);

                    //toggle on pause button and toggle off play button
                    enablePauseButtonDisablePlayButton();
                }
            }
        });
        songMenu.add(loadSong);

        //now the adding of the playlist menu
        JMenu playlistMenu = new JMenu("PLAYLIST");
        menuBar.add(playlistMenu);

        //then the adding of the items to the playlist menu
        JMenuItem createPlaylist = new JMenuItem("CREATE PLAYLIST");
        createPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //load music playlist dialog
                new Playlist(MP3GUI.this).setVisible(true);
            }
        });
        playlistMenu.add(createPlaylist);

        JMenuItem loadPlaylist = new JMenuItem("LOAD PLAYLIST");
        loadPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("PLAYLIST", "txt"));
                jFileChooser.setCurrentDirectory(new File("Player/src/sets"));

                int result = jFileChooser.showOpenDialog(MP3GUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if(result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    //stop the music
                    musicPlayer.stopSong();

                    //load playlist
                    musicPlayer.loadPlaylist(selectedFile);
                }
            }
        });
        playlistMenu.add(loadPlaylist);

        JMenuItem showPlaylistINFO = new JMenuItem("SHOW INFO");
        showPlaylistINFO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("PLAYLIST", "txt"));
                jFileChooser.setCurrentDirectory(new File("Player/src/sets"));

                int result = jFileChooser.showOpenDialog(MP3GUI.this);

                if(result == JFileChooser.APPROVE_OPTION){
                    try {
                        File selectedFile = jFileChooser.getSelectedFile();
                        Scanner scanner = new Scanner(selectedFile);
                        List<Song> songs = new ArrayList<>();
                        while(scanner.hasNextLine()){
                            String pathSong = scanner.nextLine();
                            if (new File(pathSong).exists()) songs.add(new Song(pathSong));
                        }
                        for (Song song : songs) {
                            System.out.println(song.getSongTitle());
                        }
                        scanner.close();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        playlistMenu.add(showPlaylistINFO);

        JMenuItem deletePlaylist = new JMenuItem("DELETE PLAYLIST");
        deletePlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("PLAYLIST", "txt"));
                jFileChooser.setCurrentDirectory(new File("Player/src/sets"));

                int result = jFileChooser.showOpenDialog(MP3GUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null) {
                    // Confirmation dialog (optional)
                    int confirmDelete = JOptionPane.showConfirmDialog(
                            MP3GUI.this,  // Parent component for the dialog
                            "Are you sure you want to delete " + selectedFile.getName() + "?",
                            "DELETE PLAYLIST",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmDelete == JOptionPane.YES_OPTION) {
                        // Delete the playlist file
                        if (selectedFile.delete()) {
                            JOptionPane.showMessageDialog(MP3GUI.this, "PLAYLIST DELETED SUCCESSFULLY!");
                            // (Optional) Update playlist list or UI
                        } else {
                            JOptionPane.showMessageDialog(MP3GUI.this, "ERROR DELETING PLAYLIST!");
                        }
                    }
                }
            }
        });
        playlistMenu.add(deletePlaylist);
        add(toolBar);
    }

    private void addplaybackButton() {
        playbackButton = new JPanel();
        playbackButton.setBounds(0, 435, getWidth() - 10, 80);
        playbackButton.setBackground(null);

        //previous button
        JButton prevButton = new JButton(loadImage("Player/src/sets/previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to the previous song
                musicPlayer.prevSong();
            }
        });
        playbackButton.add(prevButton);

        //play button
        JButton playButton = new JButton(loadImage("Player/src/sets/play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //toggle off play button and toggle on pause button
                enablePauseButtonDisablePlayButton();

                //play or resume song
                musicPlayer.playCurrentSong();
            }
        });
        playbackButton.add(playButton);

        //pause button
        JButton pauseButton = new JButton(loadImage("Player/src/sets/pause.png"));
        pauseButton.setBackground(null);
        pauseButton.setBorderPainted(false);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //toggle off pause button anf toggle on play button
                enablePlayButtonDisablePauseButton();

                //pause the song
                musicPlayer.pauseSong();
            }
        });
        playbackButton.add(pauseButton);

        //next button
        JButton nextButton = new JButton(loadImage("Player/src/sets/next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to the next song
                musicPlayer.nextSong();
            }
        });
        playbackButton.add(nextButton);

        add(playbackButton);
    }

    public void setPlaybackSliderValue(int frame){
        playbackSlider.setValue(frame);
    }

    public void updateSongTitleAndArtist(Song song){
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    public void updatePlaybackSlider(Song song){
        //update max count for slider
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        //create the song length label
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        //beginning will be 00:00
        JLabel labelBeginning = new JLabel("00:00");
        labelBeginning.setFont(new Font("DIALOG", Font.PLAIN, 18));
        labelBeginning.setForeground(TEXT_COLOR);

        //end will vary depending on the song
        JLabel labelEnd = new JLabel(song.getSongLength());
        labelEnd.setForeground(TEXT_COLOR);
        labelEnd.setFont(new Font("DIALOG", Font.PLAIN, 18));

        labelTable.put(0, labelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);

        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }

    public void enablePauseButtonDisablePlayButton() {
        //retrieve reference to play button from playbackButton panel
        JButton playButton = (JButton) playbackButton.getComponent(1);
        JButton pauseButton = (JButton) playbackButton.getComponent(2);

        //turn off play button
        playButton.setVisible(false);
        playButton.setEnabled(false);

        //turn on pause button
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    public void enablePlayButtonDisablePauseButton(){
        //retrieve reference to play button from playbackButton panel
        JButton playButton = (JButton) playbackButton.getComponent(1);
        JButton pauseButton = (JButton) playbackButton.getComponent(2);

        //turn on play button
        playButton.setVisible(true);
        playButton.setEnabled(true);

        //turn off pause button
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    private ImageIcon loadImage(String imagePath) {
        try{
            //read the image file from the given path
            BufferedImage image = ImageIO.read(new File(imagePath));

            //returns an image icon so that component can render the image
            return new ImageIcon(image);
        }catch (Exception e){
            e.printStackTrace();
        }

        //couldn't find resource
        return null;
    }
}
