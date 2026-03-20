package project276.group4.ui;
import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.types.GameOverReason;
import project276.group4.ui.Screens.GameView;
import project276.group4.ui.Screens.SceneManager;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project276.group4.domain.*;

/**
 * Basic implementation of GameSignals.
 * 
 * In the real game, each method should be connected to UI updates,
 * scene transitions, animations, and sound.
 */
public class DefaultGameSignals implements GameSignals {

    private GameView view;
    private HUD hud;
    private GameController controller;
    private Stage stage;
    private SceneManager sceneManager;

    /**
     * Constructs a DefaultGameSignals object with a given view and controller.
     *
     * @param view {@link GameView} object to update UI elements
     * @param controller {@link GameController} object to manage game state
     * @param stage the {@link Stage} object of the Game
     */
    public DefaultGameSignals(GameView view, GameController controller, Stage stage) {
        this.view = view;
        this.controller = controller;
        this.stage = stage;
        this.hud = (view != null) ? view.getHUD() : null;  // safe null check
        this.sceneManager = new SceneManager(stage, controller);
    }

    /**
     * Triggers the game over screen.
     *
     * Changes the game state to game over, updates the game view
     * to show the game over menu, stops the current game background music, and 
     * starts the menu background music.
     *
     * @param reason the {@link GameOverReason} indicating why the game ended
     */
    @Override
    public void triggerGameOver(GameOverReason reason) {
        if (controller != null) {
            controller.changeGameState(GameState.GAME_OVER);
        }

        if (controller != null && controller.getCurrentGameView() != null && stage != null) {
            stage.setFullScreen(true);
            if (sceneManager != null) {
                sceneManager.showGameOverMenu(reason);
            }
            SoundManager.stopGameBGM();
            SoundManager.startMenuBGM();
        }
    }

    /**
     * Triggers the victory screen.
     *
     * Changes the game state to Victory, updates the game view
     * to show the victory menu, stops the current game background music, and
     * starts the victory music.
     */
    @Override
    public void triggerVictory() {
        if (controller != null) {
            controller.changeGameState(GameState.VICTORY);
        }

        if (controller != null && controller.getCurrentGameView() != null && sceneManager != null) {
            sceneManager.showVictoryMenu();
            SoundManager.stopGameBGM();
            SoundManager.startVictoryBGM(); 
        }
    }

    /**
     * Unlocks the exit door in the game.
     * Plays the door opening sound and updates the game view to visually open
     * the exit door.
     */
    @Override
    public void unlockExit() {
        if (controller != null && controller.getCurrentGameView() != null && view != null) {
            SoundManager.playDoorOpen();
            view.openExitDoor();
        }
    }

    /**
     * Called when a player collects an item.
     * Plays the collectible pickup sound, updates the score display, and 
     * shows a visual flash to indicate gold collection.
     *
     * @param name the name of the collected item
     * @param scoreValue the score value awarded for collecting the item
     */
    public void onItemCollected(String name, int scoreValue) {
        if (controller != null && controller.getCurrentGameView() != null) {
            SoundManager.playCollectiblePickup();
            if (hud != null) {
                hud.updateScoreDisplay();
            }
            if (sceneManager != null) {
                sceneManager.showFlash(Color.rgb(225, 215, 0, 0.2), 0.1); 
            }
        }
    }
    
    /**
     * Called when the player touches Cursed Idol
     * Shows a visual flash as indication
     */
    @Override
    public void onCursedIdolTouched() {
        if (hud != null) {  // Add null check
            hud.updateScoreDisplay();
        }
        if (sceneManager != null) {  // Add null check
            sceneManager.showFlash(Color.rgb(26,26,26, 0.2), 0.2);
        }
    }

    /**
     * Called when the player takes damage.
     * Plays the player damaged sound and triggers a visual damage flash on the UI.
     * @param source the source of the damage
     */
    @Override
    public void onPlayerDamaged(String source) {
        if (controller != null && controller.getCurrentGameView() != null) {
            SoundManager.playPlayerDamaged();
            if (sceneManager != null) {
                sceneManager.showFlash(Color.rgb(138, 3, 3, 0.2), 0.2);  
            }
        }
    }

    /**
     * Called when the player collects a rune.
     * Updates the rune UI to show the updated amount of collected rune.
     */
    @Override
    public void onCollectRune() {
        if (controller != null && controller.getCurrentGameView() != null && hud != null) {
            hud.updateRuneDisplay();
        }
    }

    /**
     * Called when the player steps on trap.
     * Plays the floor thump sound
     */
    @Override
    public void onTrapSteppedOn() {
        SoundManager.playTrapSound();
    }

    /**
     * Sets the total number of runes and the number collected so far.
     * Updates the internal UI variables to reflect the current rune counts.
     * @param total the total number of runes in the level
     * @param collected the number of runes collected by the player
     */
    @Override
    public void setRuneTotals(int total, int collected) {
        if (controller != null && controller.getCurrentGameView() != null && view != null) {
            view.updateRuneVariables(total, collected);
        }
    }
}
