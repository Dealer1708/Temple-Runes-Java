package project276.group4.domain;
import project276.group4.ui.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.util.Duration;
import project276.group4.ui.Screens.*;
import project276.group4.domain.types.DifficultyLevel;
import project276.group4.domain.types.GameOverReason;
import javafx.stage.Stage;


/**
 * Controls the overall game flow and state management.
 * 
 * This class monitors the current {@link GameState} at regular intervals,
 * coordinates with {@link GameView} to update the UI, and communicates
 * game events through {@link DefaultGameSignals}. It also manages background
 * music through {@link SoundManager}.
 * 
 */
public class GameController {
    private GameState currentState = GameState.MAINMENU;
    private Timeline stateMonitor;
    private GameState lastCheckedState = null;

    private GameView currentGameView;
    private DefaultGameSignals signals;
    private static SoundManager soundManager = new SoundManager();
    private static SceneManager sceneManager;

    /**
     * Constructs a GameController and starts a state monitor that checks
     * the game state every 500 milliseconds.
     */
    public GameController() {
        // initialize to monitor the state of a game,
        // this state monitor will check every 500 milliseconds and it will continue to do even the game has not yet started to make a menu driven game
        stateMonitor = new Timeline(new KeyFrame(Duration.millis(500), event -> checkGameState()));
        stateMonitor.setCycleCount(Timeline.INDEFINITE);
        stateMonitor.play();
    }       

    /**
     * Returns the current {@link DefaultGameSignals} instance.
     *
     * @return the game signals handler
     */
    public DefaultGameSignals getGameSignals() {
        return signals;
    }

    /**
     * Sets the {@link DefaultGameSignals} object.
     *
     * @param signals the game signals handler
     */
    public void setGameSignals(DefaultGameSignals signals) {
        this.signals = signals;
    }

    /**
     * Sets the current {@link GameView}.
     *
     * @param gameView the active game view
     */
    public void setCurrentGameView(GameView gameView) {
        this.currentGameView = gameView;
    }

    /**
     * Returns the current {@link GameView}.
     *
     * @return the active game view
     */
    public GameView getCurrentGameView() {
        return currentGameView;
    }

    /**
     * Changes the current {@link GameState}.
     *
     * @param newGameState the new game state
     */
    public void changeGameState(GameState newGameState) {
        currentState = newGameState;
    }

    /**
     * Checks the current game state and performs actions based on state changes.
     * <p>
     * This method is called periodically by the state monitor
     * It prints debug information and handles transitions like GAME_OVER, VICTORY, PAUSED, etc.
     * </p>
     */
    public void checkGameState() {

        System.out.println("Checking game state: " + currentState);

        if(lastCheckedState != currentState) {
            
            switch (currentState) {
            
        case MAINMENU:
            // Handle main menu logic
            System.out.println("In main menu state");
            break;
            
        case PLAYING:
            // Game is starting - GameView's AnimationTimer handles the actual gameplay
            System.out.println("Game started - using GameView's AnimationTimer");
            break;
        case PAUSED:
            // GameView handles pause/resume of its AnimationTimer
            System.out.println("Game paused");
            break;
        case GAME_OVER:
            stateMonitor.stop();
            System.out.println("Game over");
            break;

        case RESUME: 
            System.out.println("Game resuming");
            changeGameState(GameState.PLAYING);
            break;
                
        case VICTORY:
            // Handle victory state
            System.out.println("Victory achieved!");
            break;
            } 
        }

        lastCheckedState = currentState;
    }

    /**
     * Returns the current game state.
     *
     * @return the {@link GameState} currently active
     */    
    public GameState getCurrentState() {
        return currentState;
    }
    
    /**
     * Checks whether the game is currently running.
     *
     * @return true if the game state is actively running, false otherwise
     */
    public boolean isGameRunning() {
        return currentState == GameState.PLAYING;
    }
    
    /**
     * Pauses the game by changing the state to Paused.
     */
    public void notifyGamePause() {
        changeGameState(GameState.PAUSED);
    }
    
     /**
     * Resets the GameController for a new game.
     * Resets the game state to PLAYING and restarts the state monitor if it was stopped.
     */
    public void resetForNewGame() {
        // Reset game state
        changeGameState(GameState.PLAYING);
        
        // Restart state monitor if it was stopped
        if (stateMonitor != null && stateMonitor.getStatus() == Timeline.Status.STOPPED) {
            stateMonitor = new Timeline(new KeyFrame(Duration.millis(500), event -> checkGameState()));
            stateMonitor.setCycleCount(Timeline.INDEFINITE);
            stateMonitor.play();
        }
    }

    /**
     * Starts the game by creating a new {@link GameView}, setting the scene, 
     * and playing the gameplay background music.
     *
     * @param stage the stage to display the game scene
     */
    public void startGame(Stage stage) {

        this.resetForNewGame();
        
        GameView gameView = new GameView(stage, this);
        gameView.buildGameView();
        this.setCurrentGameView(gameView);

        stage.setScene(gameView.getScene());

        SoundManager.stopGameBGM();
        SoundManager.startGameBGM();
    }
    
    /**
     * Stops the game by changing the game state to GAME_OVER.
     */
    public void stopGame() {
        this.changeGameState(GameState.GAME_OVER);
    }

    /**
     * Quits the game entirely by closing the javaFX window
     */
    public void quitGame() {
        this.changeGameState(GameState.GAME_OVER);
        javafx.application.Platform.exit();
        System.exit(0);
    }

    /**
     * Pauses the game and shows the pause menu.
     * 
     * @param stage the stage that displays the current game
     */
    public void pauseGame(Stage stage) {
        this.changeGameState(GameState.PAUSED);
        GamePauseMenu pauseMenu = new GamePauseMenu(stage, this);
        pauseMenu.buildPauseMenu();
        stage.setScene(pauseMenu.getScene());
    }

    /**
     * Resumes the game by switching the stage back to the current GameView
     * and changing the game state to PLAYING.
     * 
     * @param stage the stage that displays the current game
     */
    public void resumeGame(Stage stage) {
        GameView gv = this.getCurrentGameView();
        if (gv == null) return;
        this.changeGameState(GameState.PLAYING);
        stage.setScene(gv.getScene());
    }

}