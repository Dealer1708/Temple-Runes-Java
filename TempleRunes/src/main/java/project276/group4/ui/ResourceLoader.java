package project276.group4.ui;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.image.ImageView;

/**
 * Utility class for loading game resources such as images, audio clips, and media files.
 * 
 * Provides static helper methods to safely load resources from the classpath
 * and wrap images in ImageViews with specified dimensions.
 * 
 */
public class ResourceLoader {

    /** Private constructor to prevent instantiation. */
    private ResourceLoader() {}

    /**
     * Loads an image from the given classpath resource.
     *
     * @param path the path to the image resource
     * @return the loaded Image object
     * @throws RuntimeException if the image cannot be found
     */
    public static Image loadImage(String path) {
        InputStream is = ResourceLoader.class.getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException("Image not found: " + path);
        }
        return new Image(is);
    }

    /**
     * Loads an audio clip from the given classpath resource.
     *
     * @param path the path to the audio clip resource
     * @return the loaded AudioClip object
     * @throws RuntimeException if the resource cannot be found
     */
    public static AudioClip loadAudioClip(String path) {
        return new AudioClip(ResourceLoader.class.getResource(path).toExternalForm());
    }

    /**
     * Loads a media file from the given classpath resource.
     *
     * @param path the path to the media resource
     * @return the loaded Media object
     * @throws RuntimeException if the resource cannot be found
     */
    public static Media loadMedia(String path) {
        return new Media(ResourceLoader.class.getResource(path).toExternalForm());
    }

    /**
     * Loads an Image and wraps it in an ImageView.
     *
     * @param path path to the image resource
     * @param width desired width 
     * @param height desired height
     * @return ImageView with the loaded image
     */
    public static ImageView loadImageView(String path, double width, double height) {
        Image img = loadImage(path);
        ImageView imgView = new ImageView(img);
        if (width > 0) imgView.setFitWidth(width);
        if (height > 0) imgView.setFitHeight(height);
        return imgView;
    }
}
