package project276.group4.ui.Screens;
import project276.group4.domain.*;
import project276.group4.ui.ButtonFactory;
import project276.group4.ui.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import project276.group4.ui.Screens.*;


/**
 * Represents the main menu UI of the game. 
 * This class handles creating and displaying the menu screen,
 * including background, title, and buttons for starting the game, 
 * accessing the tutorial, and quitting the game.
 */
public class Menu {

    private final Stage stage;
    private final GameController controller;
    private Scene scene; 
    private StackPane root;
    private SceneManager sceneManager;
    
    /**
     * Constructs a Menu instance.
     *
     * @param stage The main stage of the JavaFX window.
     * @param controller The GameController instance managing game state and logic.
     */
    public Menu(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        this.root = new StackPane();
        this.scene = new Scene(root,  stage.getWidth(), stage.getHeight());
        this.sceneManager = new SceneManager(stage, controller);

    }

    /**
     * Builds and displays the main menu UI.
     *  Includes background image, game title, and start game, tutorial, and quit button.
     */
    public void buildMenu() {

        SoundManager.startMenuBGM();

        // load images assets
        Image bgImage = new Image(getClass().getResourceAsStream("/assets/images/menu_background.png"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(1920);
        bgView.setFitHeight(1080);
        Image titleImage = new Image(getClass().getResourceAsStream("/assets/images/game_title.png"));
        ImageView titleView = new ImageView(titleImage);
       
        // make the buttons using a button factory
        Button startButton = ButtonFactory.createImageButton("/assets/images/button_play.png", e -> controller.startGame(stage));

        // Difficulty button: use the project's settings image resource as requested
        Button difficultyButton = ButtonFactory.createImageButton("/assets/images/button_Settings.png", e -> sceneManager.displayDifficultyMenu(stage));
        Button settingsButton = ButtonFactory.createImageButton("/assets/images/button_tutorial.png", e -> sceneManager.displayTutorial(stage) );
        Button quitButton = ButtonFactory.createImageButton("/assets/images/button_Quit.png", e -> { controller.quitGame(); });

        // layout for buttons and label
        VBox buttonLayout = new VBox(5);
        buttonLayout.getChildren().addAll(startButton, difficultyButton, settingsButton, quitButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setTranslateY(250); // move the buttons down 100px

        // StackPane to overlay layout on background
        root.getChildren().addAll(bgView, titleView, buttonLayout);

        StackPane.setAlignment(titleView, Pos.TOP_CENTER);
        StackPane.setMargin(titleView, new Insets(250, 0, 0, 0)); // 50px from top

        // add all nodes to the pane
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Returns the Scene representing the menu UI.
     *
     * @return the menu Scene
     */
    public Scene getScene() {
        return scene;
    }
}