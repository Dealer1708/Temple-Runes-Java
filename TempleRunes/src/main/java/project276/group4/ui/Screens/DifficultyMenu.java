package project276.group4.ui.Screens;

import project276.group4.domain.GameConstants;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import project276.group4.domain.GameSettings;
import project276.group4.domain.GameController;
import project276.group4.domain.types.DifficultyLevel;
import project276.group4.ui.ButtonFactory;
import project276.group4.ui.ResourceLoader;

/**
 * Represents the Difficulty Selection menu screen where the player
 * can choose between Easy, Normal, or Hard modes. Each option updates
 * the global {@link GameSettings} difficulty and returns the user
 * back to the main menu.
 */
public class DifficultyMenu {
    private final Stage stage;
    private final GameController controller;
    private StackPane root;
    private Scene scene;
    private SceneManager sceneManager;

    /**
     * Constructs a new DifficultyMenu that allows players to choose
     * the game's difficulty level.
     *
     * @param stage the primary Stage where the menu will be displayed
     * @param controller the game controller used to update gameplay state
     */
    public DifficultyMenu(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        this.root = new StackPane();
        this.scene = new Scene(root,  stage.getWidth(), stage.getHeight());
        this.sceneManager = new SceneManager(stage, controller);
    }
 
    /**
     * Builds and displays the difficulty selection menu.  
     */
    public void buildDifficultyMenu() {
        // Background
        Image bg = new Image(getClass().getResourceAsStream("/assets/images/menu_background.png"));
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(GameConstants.WINDOW_WIDTH);
        bgView.setFitHeight(GameConstants.WINDOW_HEIGHT);

        ImageView title = ResourceLoader.loadImageView("/assets/images/selectDifficulty.png", 0, 0);
        Button easy = ButtonFactory.createImageButton("/assets/images/easyButton.png", e -> { GameSettings.setCurrentDifficulty(DifficultyLevel.EASY); sceneManager.displayMenu(stage); } );
        Button normal = ButtonFactory.createImageButton("/assets/images/normalButton.png", e -> { GameSettings.setCurrentDifficulty(DifficultyLevel.NORMAL); sceneManager.displayMenu(stage); });
        Button hard = ButtonFactory.createImageButton("/assets/images/hardButton.png", e -> { GameSettings.setCurrentDifficulty(DifficultyLevel.HARD); sceneManager.displayMenu(stage);} );

        VBox v = new VBox(10, easy, normal, hard);
        v.setTranslateY(200);
        v.setAlignment(Pos.CENTER);
        title.setTranslateY(-150);

        root.getChildren().addAll(bgView, v, title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Returns the Scene representing the Game Difficulty Selection Menu UI.
     *
     * @return the JavaFX Scene for the select game difficulty menu
     */
    public Scene getScene() { return scene; }
}
