package project276.group4.ui.Screens;

import project276.group4.domain.GameConstants;
import project276.group4.ui.ButtonFactory;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project276.group4.domain.GameController;

/**
 * Represents the Tutorial screen UI in the game.
 * This screen displays the tutorial background and image explaining how to play the game. 
 * It includes a "Back" button to return to the main menu.
 */
public class Tutorial {
    private Stage stage;
    private Scene scene;
    private StackPane root;
    private GameController gameController;
    private SceneManager sceneManager;

    /**
     * Constructs a Tutorial UI screen
     *
     * @param stage the primary Stage on which the tutorial is displayed
     * @param gameController the GameController managing the current game state
     */
    public Tutorial(Stage stage, GameController gameController) {
        this.stage = stage;
        this.gameController = gameController;
        this.root = new StackPane();
        this.sceneManager = new SceneManager(stage, gameController);
    }

    /**
     * Builds the Tutorial screen UI.
     * 
     * Sets up the background, the tutorial image, and a "Back" button.
     */
    public void buildTutorialMenu() {
        // set background
        Image bgImage = new Image(getClass().getResourceAsStream("/assets/images/gameover_background.png"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(GameConstants.WINDOW_WIDTH);
        bgView.setFitHeight(GameConstants.WINDOW_HEIGHT);

        Image tutorial = new Image(getClass().getResourceAsStream("/assets/images/tutorial.png"));
        ImageView tutorialView = new ImageView(tutorial);

        // set buttons
        Button backButton = ButtonFactory.createImageButton("/assets/images/back.png", e -> sceneManager.displayMenu(stage));

        VBox buttonLayout = new VBox(20, backButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setTranslateY(400);

        root.getChildren().addAll(bgView,tutorialView,buttonLayout);
        
        this.scene = new Scene(root,  stage.getWidth(), stage.getHeight());
    }

    /**
     * Returns the Scene representing the tutorial UI.
     * @return the tutorial Scene
     */
    public Scene getScene() { return scene; }

}
