package project276.group4;

import javafx.application.Application;
import javafx.stage.Stage;
import project276.group4.domain.*;
import project276.group4.ui.Screens.Menu;


/**
 * this class initializes the JavaFX window and sets up the main
 * menu as the first screen shown to the player.
 */
public class App extends Application {

    /**
     * Creates a new App instance. This constructor initializes the JavaFX
     * application framework by delegating to the superclass constructor.
     */
    public App() {
        super();
    }
    
    /**
     * Starts the JavaFX application by creating the main Stage and
     * displaying the main menu scene.
     *
     * @param stage the primary Stage for this application
     */
    @Override
    public void start(Stage stage) {
        GameController gameController1 = new GameController();
        Menu menu = new Menu(stage, gameController1);
        menu.buildMenu(); // Show menu instead of auto-starting
        stage.setWidth(GameConstants.WINDOW_WIDTH);
        stage.setHeight(GameConstants.WINDOW_HEIGHT);
        stage.setScene(menu.getScene());
        stage.setTitle("RuneEscape");
        stage.show();
    }

    /**
     * The main entry point for launching the JavaFX application.
     * @param args command-line arguments passed to the program
     */

    public static void main(String[] args) {
        launch(args);  // This starts JavaFX
    }    
}
