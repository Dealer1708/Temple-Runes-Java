package project276.group4.ui.Screens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import project276.group4.domain.GameController;
import project276.group4.domain.types.GameOverReason;
import project276.group4.services.GameStatsLogger;

/**
 * Manages scene transitions in the game, including:
 * <ul>
 *     <li>Main Menu</li>
 *     <li>Difficulty Menu</li>
 *     <li>Tutorial</li>
 *     <li>Gameplay Scene (GameView)</li>
 *     <li>Victory and Game Over screens</li>
 * </ul>
 *
 * SceneManager also provides utility functionality such as screen flashing,
 * and switching to UI screens like Game Over or Victory
 */
public class SceneManager {

    private final Stage stage;
    private final GameController controller;
    private GameView gameView;
    private AnimationTimer gameLoop;
    private Timeline timer;
    private Scene gameScene;  // The scene from GameView


    /**
     * Constructs a SceneManager responsible for controlling all UI transitions.
     *
     * @param stage the primary stage of the application
     * @param controller the game controller containing core game logic
     */
    public SceneManager(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    /**
     * Sets the active gameplay scene and retrieves the associated
     * {@link GameView}, {@link AnimationTimer}, and {@link Timeline}.
     *
     * This method must be called by {@link GameView} after it finishes
     * constructing the gameplay scene
     *
     * @param scene the scene created by GameView
     */
    public void setScene(Scene scene) {
        this.gameScene = scene;

        // GameView might not exist the first time (menu first)
        this.gameView = controller.getCurrentGameView();
        if (gameView != null) {
            this.gameLoop = gameView.getGameLoop();
            this.timer    = gameView.getCurrentTimer();
        }
    }


    /* Menu Display -------- */

    /**
     * Displays the main menu on the primary stage.
     *
     * @param stage the application stage
     */
    public void displayMenu(Stage stage) {
        Menu menu = new Menu(stage, controller);
        menu.buildMenu();
        stage.setScene(menu.getScene());
    }

    /**
     * Displays the difficulty selection menu.
     *
     * @param stage the application stage
     */
    public void displayDifficultyMenu(Stage stage) {
        DifficultyMenu dm = new DifficultyMenu(stage, controller);
        dm.buildDifficultyMenu();
        stage.setScene(dm.getScene());
    }


    /**
     * Displays the tutorial screen.
     *
     * @param stage the application stage
     */
    public void displayTutorial(Stage stage) {
        Tutorial tut = new Tutorial(stage, controller);
        tut.buildTutorialMenu();
        stage.setScene(tut.getScene());
    }


    /* Game Over / Victory -------- */

    /**
     * Transitions to the Game Over menu. This method stops any running
     * animations, attempts to log player statistics, and switches to the
     * Game Over UI.
     *
     * @param reason the reason game ended
     */
    public void showGameOverMenu(GameOverReason reason) {

        GameView gv = controller.getCurrentGameView(); // ALWAYS fetch latest

        if (gv != null) {
            try {
                GameStatsLogger.logGameStats(
                    gv.getCurrentScore(),
                    gv.getSeconds(),
                    "NORMAL",
                    false
                );
            } catch (Exception e) {
                System.err.println("Failed to log game stats: " + e.getMessage());
            }

            if (gv.getGameLoop() != null)
                gv.getGameLoop().stop();

            if (gv.getCurrentTimer() != null)
                gv.getCurrentTimer().stop();
        }

        GameOverMenu gameOverMenu = new GameOverMenu(stage, controller);
        gameOverMenu.buildGameOverMenu(reason);
        stage.setScene(gameOverMenu.getScene());
    }

    /**
     * Shows the victory menu after the player wins. This stops gameplay
     * animations (if active) and displays the victory UI.
     */
    public void showVictoryMenu() {
        if (gameLoop != null) gameLoop.stop();
        if (timer != null)    timer.stop();

        VictoryMenu victoryMenu = new VictoryMenu(stage, controller);
        stage.setScene(victoryMenu.getScene());
    }

    /* Flash Effect -------- */

    /**
     * Displays a temporary flashing overlay on the gameplay scene.
     * This is typically used to indicate that the player took damage.
     *
     * @param colour the overlay color
     * @param durationSeconds how long the flash should last
     */
    public void showFlash(Color colour, double durationSeconds) {
        GameView gv = controller.getCurrentGameView();
        if (gv == null) {
            System.err.println("Flash failed: GameView is null.");
            return;
        }

        Pane overlay = gv.getOverlayRoot();
        if (overlay == null) {
            System.err.println("Flash failed: overlayRoot is null.");
            return;
        }

        Rectangle flash = new Rectangle();
        flash.setFill(colour);
        flash.setMouseTransparent(true);

        // Bind to overlay size instead of scene size
        flash.widthProperty().bind(overlay.widthProperty());
        flash.heightProperty().bind(overlay.heightProperty());

        Platform.runLater(() -> {
            overlay.getChildren().add(flash);

            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(durationSeconds),
                    e -> overlay.getChildren().remove(flash))
            );
            timeline.setCycleCount(1);
            timeline.play();
        });
    }


}
