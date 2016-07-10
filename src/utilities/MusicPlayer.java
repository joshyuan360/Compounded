package utilities;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * The music player used in the Compounded Game.
 * @author George Lim
 * Time spent: 22 hours
 * @version 5.0 8/8/2014
 */ 
public class MusicPlayer {
    /** A list of music files to be played. */
    private ArrayList<Clip> tracks;
    /** The song that is currently being played. */
    private Clip currentlyPlaying;
    /** The current track number. */
    private int trackNumber = 0;
    /** The name of the file to be played by this music player. */
    private String fileName;
    private LineListener listener;
    /** Stores the volume limit. */
    public static int musicDecibels = -10;
    /** Stores the number of decibels for the sound. */
    public static int soundDecibels = 3;
    /** Stores the state of whether or not the playlist should change songs after the current one is being played. */
    private boolean dontListen;
    
    /**
     * Constructor adds all the songs in the music folder to the tracks ArrayList.
     * The try catch is used to catch multiple exceptions that could happen with using LineListener. The first for loop iterates through all of the files in importedSongs.
     * <p> LineListener inner class documentation:
     * update () is used change the currently playing song if the song has ended.
     * @param importedSongs File array: A list of songs in the music folder.
     * @param listener LineListener reference: The LineListener that is added to all of the songs, changing the song when needed.
     * @param e Exception reference: Contains info about an exception, if it is thrown.
     * @param event LineEvent reference in inner class: Contains info about the line event.
     * @param i Loop integer variable: used to iterate through the for-loop to import all songs from folder.
     * @throws LineUnavailableException if the line is unavailable.
     * @throws UnsupportedAudioFileException if the audio file is unsupported.
     * @throws IOException if an error occurs while reading to the file.
     */ 
    public MusicPlayer () {
        try {
            tracks = new ArrayList<Clip>();
            File[] importedSongs = new File("./data/music/").listFiles();
            for (int i = 0; i < importedSongs.length; i++) {
                tracks.add(AudioSystem.getClip());
                tracks.get(i).open(AudioSystem.getAudioInputStream(importedSongs[i]));
            }
            listener = new LineListener() {
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP && !dontListen) {
                        trackNumber = currentlyPlaying == tracks.get(tracks.size() - 1) ? 0 : trackNumber + 1;
                        currentlyPlaying.removeLineListener(listener);
                        currentlyPlaying = tracks.get(trackNumber);
                        currentlyPlaying.addLineListener(listener);
                        currentlyPlaying.setFramePosition(0);
                        adjustVolume ();
                        currentlyPlaying.start();
                    }
                }
            };
        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e){
        } catch (java.io.IOException e){
        }
        currentlyPlaying = tracks.get(trackNumber);
        currentlyPlaying.addLineListener(listener);
        adjustVolume ();
        currentlyPlaying.start();
    }
    
    /** Creates a new MusicPlayer with the specified fileName.
      * @param fileName The name of the file to be played by this music player.
      */
    public MusicPlayer (String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Begins playing the file specified by fileName.
     * The first if statement exits the method if the music is already playing.
     * The try-catch is used to catch a LineUnavailableException, UnsupportedAudioFileException, and IOException.
     * @param e Exception reference: Contains info about an exception, if thrown.
     * @throws LineUnavailableException - if the line is unavailable.
     * @throws UnsupportedAudioFileException - if the audio file is not supported.
     * @throws IOException - if an error occurs while reading to the file.
     */ 
    public void start() {
        if (currentlyPlaying != null)
            return;
        try {
            currentlyPlaying = AudioSystem.getClip();
            currentlyPlaying.open(AudioSystem.getAudioInputStream(new File("./data/soundEffects/" + fileName + ".wav")));
            adjustVolume ();
            currentlyPlaying.start();
        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e){
        } catch (java.io.IOException e){
        }
    }
    
    /** Synchronizes music volume. */
    public void sync() {
        dontListen = true;
        currentlyPlaying.stop();
        adjustVolume ();
        currentlyPlaying.start();
        dontListen = false;
    }
    
    /** Adjusts the volume to the specified number of decibels. */
    public void adjustVolume () {
        ((FloatControl)currentlyPlaying.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float)(fileName == null ? musicDecibels : soundDecibels));
    }
    
    /**
     * Stops playing the music that is specified by currentlyPlaying.
     * The first if statement checks that currentlyPlaying does not have a null reference.
     * The second if statement checks that tracks does not have a null reference.
     */ 
    public void stop () {
        if (currentlyPlaying != null) {
            currentlyPlaying.stop();
            currentlyPlaying = null;
        }
        if (tracks != null) {
            trackNumber = tracks.size() - 1;
        }
    }
}