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
import project276.group4.ui.ButtonFactory;


/**
 * Represents the Victory Menu UI displayed when the player wins the game.
 * It includes a background, a victory icon, and buttons to either
 * start a new game or quit. Handles transitioning to the game or
 * changing the game state on quit.
 */
public class VictoryMenu {
    private Stage stage;
    private GameController controller;
    private StackPane root;
    private Scene scene;
    private SceneManager sceneManager;

    /**
     * Constructs a VictoryMenu object and initializes the UI.
     * @param stage the stage of the javafx application
     * @param controller the main Game Controller of the game
     */
    public VictoryMenu(Stage stage, GameController controller) {
        
        this.stage = stage;
        this.controller = controller;
        this.root = new StackPane();
        this.sceneManager = new SceneManager(stage, controller);

        this.scene = new Scene(root,  stage.getWidth(), stage.getHeight());
        Image bgImage = new Image(getClass().getResourceAsStream("/assets/images/gameover_background.png"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(GameConstants.WINDOW_WIDTH);
        bgView.setFitHeight(GameConstants.WINDOW_HEIGHT);

         // set title
        Image victory = new Image(getClass().getResourceAsStream("/assets/images/victory.png"));
        ImageView victoryIcon = new ImageView(victory);
        victoryIcon.setTranslateY(-100); //move image up by 100px

        //set label
        Label victoryMessage = createVictoryLabel();

        // set buttons
        Button startButton = ButtonFactory.createImageButton("/assets/images/button_playagain.png", e -> controller.startGame(stage));
        Button settingsButton = ButtonFactory.createImageButton("/assets/images/button_Settings.png", e -> { sceneManager.displayDifficultyMenu(stage); });
        Button quitButton = ButtonFactory.createImageButton("/assets/images/button_Quit.png", e -> { controller.quitGame(); });

        VBox buttonLayout = new VBox(20, startButton, settingsButton, quitButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setTranslateY(200);

        root.getChildren().addAll(bgView, victoryIcon, buttonLayout, victoryMessage);
    }


    /**
     * Returns the Scene representing the Victory Menu UI.
     *
     * @return the JavaFX Scene for the Victory Menu
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Creates and returns a styled victory message label using the game's pixel font.
     * 
     * The label uses a custom pixel font, light text color, black outline stroke,
     * and bold weight to ensure readability. Its position is centered based on the
     * current scene dimensions.
     * @return a pixel-styled Label instance that displays a victory message
     */
    public Label createVictoryLabel() {
        Label label = new Label();
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PixelOperator8.ttf"), 23);
        label.setFont(pixelFont);
        label.setStyle(
            "-fx-text-fill: #F2F2F2;" +
            "-fx-stroke: black;" +
            "-fx-stroke-width: 5px;" +
            "-fx-font-weight: bold;"
        );

        label.setText("The spirits of the temple acknowledge your triumph.");
        
        // Let the StackPane handle centering
        StackPane.setAlignment(label, Pos.CENTER);
        
        return label;
    }

}