package project276.group4.ui;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


/**
 * PlayerAnimation handles sprite sheet animation for a player character in JavaFX.
 * 
 * It cycles through an array of {@link Image} frames and updates the given {@link ImageView}.
 * The animation loops indefinitely and interpolates linearly between frames.
 */
public class PlayerAnimation extends Transition {

    private final ImageView imageView;
    private final Image[] frames;
    private int lastIndex = -1;

    /**
     * Constructs a PlayerAnimation.
     *
     * @param imageView the ImageView that will display the animation frames.
     * @param frames the array of images representing the animation frames.
     * @param durationMs the total duration of one animation cycle in milliseconds.
     */
    public PlayerAnimation(ImageView imageView, Image[] frames, int durationMs) {
        this.imageView = imageView;
        this.frames = frames;

        setCycleDuration(Duration.millis(durationMs));
        setInterpolator(Interpolator.LINEAR);
        setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Immediately displays the first frame of the animation.
     */
    public void showFirstFrame() {
        lastIndex = 0;
        imageView.setImage(frames[0]);
    }

    /**
     * Called to update the animation based on the fraction of the cycle completed.
     * Updates the ImageView with the correct frame.
     *
     * @param frac the fraction of the current animation cycle (0.0 to 1.0).
     */
    @Override
    protected void interpolate(double frac) {
        // FIX: prevent out-of-bounds when frac == 1
        int index = (int) (frac * frames.length) % frames.length;

        if (index != lastIndex) {
            imageView.setImage(frames[index]);
            lastIndex = index;
        }
    }
}
