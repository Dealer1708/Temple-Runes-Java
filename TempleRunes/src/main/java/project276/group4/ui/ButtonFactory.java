package project276.group4.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory class for creating JavaFX Buttons with custom images.
 * Adds transparent background, and assigns an action handler.
 */
public class ButtonFactory {

    /** Prevent instantiation of utility class. */
    private ButtonFactory() {}

    /**
     * Creates a Button using an image as its graphic.
     * The button will have a transparent background and border.
     *
     * @param imagePath the path to the image resource to use for the button
     * @param buttonAction the EventHandler to execute when the button is clicked
     * @return a JavaFX Button with the specified image and action congfiguration
     */
    public static Button createImageButton(String imagePath, EventHandler<ActionEvent> buttonAction) {
        Image img = new Image(ButtonFactory.class.getResourceAsStream(imagePath));
        ImageView imgView = new ImageView(img);
        Button button = new Button("", imgView);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button.setOnAction(buttonAction); 
        return button;
    }
}
