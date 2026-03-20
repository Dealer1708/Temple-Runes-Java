package project276.group4.ui.Screens;

import project276.group4.domain.GameConstants;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project276.group4.domain.GameController;
import project276.group4.ui.ButtonFactory;

/**
 * Represents the Game Pause Menu UI displayed when the game is paused.
 * It includes buttons to resume the game or quit. Handles transitioning back to the current
 * game or stopping the game entirely.
 */
public class GamePauseMenu {
    private Stage stage;
    private Scene scene;
    private StackPane root;
    private GameController controller;

    /**
     * Constructs a GamePauseMenu instance.
     * Initializes the root pane and keeps a reference to the stage and GameController.
     *
     * @param stage the primary JavaFX Stage where the pause menu will be displayed
     * @param controller the GameController instance managing game logic
     */
    public GamePauseMenu(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        this.root = new StackPane();
    }

    /**
     * Builds the pause menu UI.
     * Adds a background image, a "Game Paused" title, and buttons to resume
     * or quit the game. 
     */
    public void buildPauseMenu() {
        // set background
        Image bgImage = new Image(getClass().getResourceAsStream("/assets/images/gameover_background.png"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(GameConstants.WINDOW_WIDTH);
        bgView.setFitHeight(GameConstants.WINDOW_HEIGHT);

        Image gamePausedImage = new Image(getClass().getResourceAsStream("/assets/images/icon_GamePaused.png"));
        ImageView gamePausedTitle = new ImageView(gamePausedImage);
        gamePausedTitle.setTranslateY(-100);

        // set buttons
        Button retryButton = ButtonFactory.createImageButton("/assets/images/button_resume.png", e -> controller.resumeGame(stage));
        Button quitButton = ButtonFactory.createImageButton("/assets/images/button_Quit.png", e -> controller.quitGame());

        VBox buttonLayout = new VBox(20, retryButton, quitButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setTranslateY(200);

        root.getChildren().addAll(bgView,gamePausedTitle,buttonLayout);
        
        scene = new Scene(root,  stage.getWidth(), stage.getHeight());
    }

    /**
     * Returns the Scene representing the Game Pause Menu UI.
     *
     * @return the JavaFX Scene for the pause menu
     */
    public Scene getScene() { return scene; }
}

