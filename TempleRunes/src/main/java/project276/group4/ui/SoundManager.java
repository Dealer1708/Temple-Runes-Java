package project276.group4.ui;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;


/**
 * Manages game audio playback including sound effects and background music.
 * This class provides static methods to play various sounds (like collectible pickups, player damage,
 * trap sounds, and door opening) as well as control background music for gameplay, menu, and victory screens.
 */
public class SoundManager {

    private static AudioClip collectibleSound;
    private static AudioClip doorOpen;
    private static AudioClip playerOof;
    private static AudioClip trapSound;
    private static MediaPlayer gameBGM;
    private static MediaPlayer menuBGM;
    private static MediaPlayer victoryBGM;


    /**
     * Constructs a SoundManager and loads all audio resources.
     * Handles errors gracefully if a file cannot be found or loaded.
     */
    public SoundManager() {
        try {
            collectibleSound = ResourceLoader.loadAudioClip("/assets/sounds/runeSound.wav");
            doorOpen = ResourceLoader.loadAudioClip("/assets/sounds/doorOpeningSound.wav");
            playerOof = ResourceLoader.loadAudioClip("/assets/sounds/oof.wav");
            trapSound = ResourceLoader.loadAudioClip("/assets/sounds/thump.wav");
    
            menuBGM = createMediaPlayer("/assets/sounds/menu.wav", 0.5);
            gameBGM = createMediaPlayer("/assets/sounds/gameplay.wav", 0.3);
            victoryBGM = createMediaPlayer("/assets/sounds/victory.wav", 0.1);

        } catch (Exception e) {
            System.out.println("[SoundManager] Failed to load sounds: " + e.getMessage());
        }
    }

    /**
     * Helper method to create a MediaPlayer for background music.
     *
     * @param path the path to the media file
     * @param volume the initial volume of the MediaPlayer
     * @return a MediaPlayer object ready to play the specified media by path
     * @throws IllegalArgumentException if the media file cannot be loaded
     */
    private static MediaPlayer createMediaPlayer(String path, double volume) {
        Media media = ResourceLoader.loadMedia(path);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        return mediaPlayer;
    }
    
    // SOUND EFFECTS ----------

    /** Plays the sound effect for when the player takes damage. */
    public static void playPlayerDamaged() { if (playerOof != null) playerOof.play(); }

    /** Plays the sound effect for traps being triggered. */
    public static void playTrapSound() { if (trapSound != null) trapSound.play(); }

    /** Plays the sound effect when a collectible item is picked up. */
    public static void playCollectiblePickup() { if (collectibleSound != null) collectibleSound.play(); }

    /** Plays the sound effect for opening doors. */
    public static void playDoorOpen() { if (doorOpen != null) doorOpen.play(); }


    // BACKGROUD MUSIC ----------

    /** Starts the gameplay background music. Loops indefinitely. */
    public static void startGameBGM() { if (gameBGM != null) gameBGM.play(); }

    /** Stops the gameplay background music if it is playing. */
    public static void stopGameBGM() { if (gameBGM != null) gameBGM.stop(); }

    /** Starts the menu background music. Loops indefinitely. */
    public static void startMenuBGM() { if (menuBGM != null) menuBGM.play(); }

    /** Stops the menu background music if it is playing. */
    public static void stopMenuBGM() { if (menuBGM != null) menuBGM.stop(); }

    /** Starts the victory background music. Loops indefinitely. */
    public static void startVictoryBGM() { if (victoryBGM != null) victoryBGM.play(); }

    /** Stops the victory background music if it is playing. */
    public static void stopVictoryBGM() { if (victoryBGM != null) victoryBGM.stop(); }

}
