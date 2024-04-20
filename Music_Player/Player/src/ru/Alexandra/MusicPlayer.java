package ru.Alexandra;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.*;
import java.util.ArrayList;

public class MusicPlayer extends PlaybackListener {
    //this will be used to update isPaused more synchronously
    private static final Object playSignal = new Object();

    //need reference so that it can update the gui in this class
    private MP3GUI musicPlayerGUI;

    //way to store song's details, create a song class
    private Song currentSong;
    public Song getCurrentSong(){
        return currentSong;
    }

    private ArrayList<Song> playlist;

    //to keep current track's index
    private int currentPlaylistIndex;

    //use JPlayer library to create an AdvancedPlayer obj which will handle playing the music
    private AdvancedPlayer advancedPlayer;

    //pause flag used to indicate whether the player has been paused
    private boolean isPaused;

    //pause boolean flag used to tell when the song has finished
    private boolean songFinished;

    private boolean pressedNext, pressedPrev;

    //stores in teh last frame when the playback is finished (used for pausing and resuming)
    private int currentFrame;
    public void setCurrentFrame(int frame){
        currentFrame = frame;
    }

    //track how many milliseconds has passed since playing the song (used for updating the slider)
    private int currentTimeInMilliseconds;
    public void setCurrentTimeInMilliseconds(int timeInMilliseconds){
        currentTimeInMilliseconds = timeInMilliseconds;
    }

    //constructor
    public MusicPlayer(MP3GUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
    }

    public void loadSong(Song song){
        currentSong = song;
        playlist = null;

        //stop the song if possible
        if(!songFinished) {
            stopSong();
        }

        //play the current song if not null
        if(currentSong != null){
            //reset frame
            currentFrame = 0;

            //reset current time in ms
            currentTimeInMilliseconds = 0;

            //update gui
            musicPlayerGUI.setPlaybackSliderValue(0);

            playCurrentSong();
        }
    }

    public void loadPlaylist(File playlistFile){
        playlist = new ArrayList<>();

        //store the paths from the text file into the playlist array list
        try{
            FileReader fileReader = new FileReader(playlistFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //reach each line from the text file and store the text into the songPath variable
            String songPath;
            while((songPath = bufferedReader.readLine()) != null) {
                //create song object based on song path
                Song song = new Song(songPath);

                //add to playlist array list
                playlist.add(song);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(playlist.size() > 0){
            //reset playback slider
            musicPlayerGUI.setPlaybackSliderValue(0);
            currentTimeInMilliseconds = 0;

            //update current song to the first song in the playlist
            currentSong = playlist.get(0);

            //start from the beginning frame
            currentFrame = 0;

            //update gui
            musicPlayerGUI.enablePauseButtonDisablePlayButton();
            musicPlayerGUI.updateSongTitleAndArtist(currentSong);
            musicPlayerGUI.updatePlaybackSlider(currentSong);

            //start song
            playCurrentSong();
        }
    }

    public void pauseSong(){
        if (advancedPlayer != null){
            //update isPaused flag
            isPaused = true;

            //then stop the player
            stopSong();
        }
    }

    public void stopSong(){
        if(advancedPlayer != null){
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }

    public void nextSong(){
        //no need to go to the next song if there is no playlist
        if(playlist == null) return;

        //check to see if it has reached the end of the playlist, if so then don't do anything
        if (currentPlaylistIndex + 1 > playlist.size() - 1) return;

        pressedNext = true;

        //stop the song if possible
        if(!songFinished) stopSong();

        //increase current playlist index
        currentPlaylistIndex++;

        //update current song
        currentSong = playlist.get(currentPlaylistIndex);

        //reset frame
        currentFrame = 0;

        //reset current time in null
        currentTimeInMilliseconds = 0;

        //update gui
        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        //play the song
        playCurrentSong();
    }

    public void prevSong(){
        //no need to go to the next song if there is no playlist
        if(playlist == null) return;

        //check to see if it can go to the previous song
        if (currentPlaylistIndex - 1 < 0) return;

        pressedPrev = true;

        //stop the song if possible
        if (!songFinished) stopSong();

        //decrease current song
        currentPlaylistIndex--;

        //update current song
        currentSong = playlist.get(currentPlaylistIndex);

        //reset frame
        currentFrame = 0;

        //reset current time in ms
        currentTimeInMilliseconds = 0;

        //update gui
        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        //play the song
        playCurrentSong();
    }

    public void playCurrentSong(){
        if (currentSong == null) return;

        try{
            //read mp3 audio data
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            //create a new advanced player
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);

            //start music
            startMusicThread();

            //start playback slider thread
            startPlaybackSliderThread();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //create a thread that will handle playing the music
    private void startMusicThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(isPaused){
                        synchronized (playSignal){
                            //update flag
                            isPaused = false;

                            //notify the other thread to continue (makes sure that isPaysed is update to false properly)
                            playSignal.notify();
                        }

                        //resume music from last frame
                        advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
                    } else{
                        //play music from the beginning
                        advancedPlayer.play();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //create a thread that will handle updating the slider
    private void startPlaybackSliderThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isPaused){
                    try{
                        //wait till it gets notified by other thread to continue
                        //makes sure that isPaused boolean flag updates to false before continuing
                        synchronized (playSignal){
                            playSignal.wait();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                while(!isPaused && !songFinished && !pressedNext && !pressedPrev){
                    try{
                        //increment current time ms
                        currentTimeInMilliseconds++;

                        //calculate into frame value
                        int calculatedFrame = (int) ((double) currentTimeInMilliseconds * 2.08 * currentSong.getFrameRatePerMilliseconds());

                        //update gui
                        musicPlayerGUI.setPlaybackSliderValue(calculatedFrame);

                        //mimic 1 ms using thread.sleep
                        Thread.sleep(1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt){
        //this method gets called in the beginning of the song
        System.out.println("PLAYBACK STARTED");
        songFinished = false;
        pressedNext = false;
        pressedPrev = false;
    }

    @Override
    public void playbackFinished(PlaybackEvent evt){
        //this method gets called when the song finished or if the player gets closed
        System.out.println("PLAYBACK FINISHED");
        if(isPaused){
            currentFrame += (int) ((double) evt.getFrame() * currentSong.getFrameRatePerMilliseconds());
        }else {
            //if the user passed next or prev, needn't execute the rest of the code
            if (pressedNext || pressedPrev) return;

            //when the song ends
            songFinished = true;

            if (playlist == null){
                //update gui
                musicPlayerGUI.enablePlayButtonDisablePauseButton();
            }else {
                //last song in the playlist
                if(currentPlaylistIndex == playlist.size() - 1){
                    //update gui
                    musicPlayerGUI.enablePlayButtonDisablePauseButton();
                } else {
                    //go to the next song in the playlist
                    nextSong();
                }
            }
        }
    }
}
