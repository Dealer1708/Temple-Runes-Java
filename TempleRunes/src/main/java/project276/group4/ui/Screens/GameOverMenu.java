package project276.group4.ui.Screens;

import project276.group4.domain.GameConstants;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import project276.group4.domain.GameController;
import project276.group4.domain.types.GameOverReason;
import project276.group4.ui.ButtonFactory;

/**
 * Represents the Game Over Menu UI displayed when the player loses the game.
 * The menu includes a background, a Game Over title, and buttons to either
 * retry the game or quit. Handles restarting the game or changing the game state.
 */
public class GameOverMenu {
    private Stage stage;
    private GameController controller;
    private StackPane root;
    private Scene scene;
    private SceneManager sceneManager;

    /**
     * Constructs a GameOverMenu object.
     * Initializes the root pane and scene for the menu.
     *
     * @param stage the primary JavaFX Stage where the menu will be displayed
     * @param controller the GameController instance managing game logic
     */
    public GameOverMenu(Stage stage, GameController controller){
        this.stage = stage;
        this.controller = controller;
        this.root = new StackPane();
        this.scene = new Scene(root,  stage.getWidth(), stage.getHeight());
        this.sceneManager = new SceneManager(stage, controller);
    }

    /**
     * Builds and displays the Game Over menu UI.
     * Adds a background, title, and buttons for retrying or quitting the game.
     * @param reason the reason that caused gameover
     */
    public void buildGameOverMenu(GameOverReason reason) {
        // set background

        Image bgImage = new Image(getClass().getResourceAsStream("/assets/images/gameover_background.png"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(GameConstants.WINDOW_WIDTH);
        bgView.setFitHeight(GameConstants.WINDOW_HEIGHT);

        // set title
        Image gameOver = new Image(getClass().getResourceAsStream("/assets/images/icon_GameOver.png"));
        ImageView gameOverTitle = new ImageView(gameOver);
        gameOverTitle.setTranslateY(-100); //move image up by 100
    
        Label gameOverMessage = createGameOverReasonLabel(reason);

        // set buttons
        Button retryButton = ButtonFactory.createImageButton("/assets/images/button_playagain.png", e -> {controller.startGame(stage);} );
        Button settingsButton = ButtonFactory.createImageButton("/assets/images/button_Settings.png", e -> { sceneManager.displayDifficultyMenu(stage); });
        Button quitButton = ButtonFactory.createImageButton("/assets/images/button_Quit.png", e -> {controller.quitGame();} );
        
        VBox buttonLayout = new VBox(20, retryButton, settingsButton, quitButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setTranslateY(200);

        root.getChildren().addAll(bgView, gameOverTitle, buttonLayout, gameOverMessage);
    }

    /**
     * Creates a JavaFX Label to display the reason for a game over.
     *
     * This method sets a custom pixel-style font, applies text styling including 
     * color, stroke, and weight, and centers the label within the current scene.
     *
     * @param reason the {@link GameOverReason} containing the message to display
     * @return a pixel-styled {@link Label} centered in the scene with the game over reason text
     */
    public Label createGameOverReasonLabel(GameOverReason reason) {
        Label label = new Label();
        Font pixelFont = Font.loadFont(     
            getClass().getResourceAsStream("/fonts/PixelOperator8.ttf"),
            21
        );
        label.setFont(pixelFont);
        // label.setStyle("-fx-text-fill: #F2F2F2;" + "-fx-font-smoothing-type: gray;");
        
        label.setStyle(
            "-fx-text-fill: #F2F2F2;" +
            "-fx-stroke: black;" +
            "-fx-stroke-width: 5px;" +
            "-fx-font-weight: bold;"
        );

        label.setLayoutX((scene.getWidth() - label.getWidth()) / 2);
        label.setLayoutY((scene.getHeight() - label.getHeight()) / 2);
        label.setText(reason.getMessage());
        return label;
    }   

    /**
     * Returns the Scene representing the Game Over menu UI.
     *
     * @return the JavaFX Scene for the Game Over menu
     */
    public Scene getScene() {
        return scene;
    }
}

