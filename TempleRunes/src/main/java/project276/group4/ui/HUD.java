package project276.group4.ui;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import project276.group4.domain.interfaces.ScoreManager;

/**
 * The HUD (Heads-Up Display) class is responsible for creating and managing
 * all on-screen informational labels during gameplay, including the objective,
 * rune counter, score counter, and timer.
 * 
 * It loads a pixel-style font and positions each HUD element
 * at a fixed location on the screen. The HUD labels can be updated externally
 * by the GameController or game loop to reflect current game state.
 */
public class HUD {

     /** Label displaying the current objective text. */
    public Label objectiveLabel;

    /** Label showing the player's collected runes versus total required runes. */
    public Label runeLabel;

    /** Label showing the player's current score. */
    public Label scoreLabel;

    /** Label showing the active game timer. */
    public Label timerLabel;

    private ScoreManager scoreManager;
    private int totalRunes;
    private int runesCollected;

    /**
     * Constructs the HUD and initializes all on-screen labels, including
     * objective text, rune counter, score counter, and timer.
     *
     * @param runesCollected total number of runes player collected
     * @param totalRunes total number of runes required to complete the game
     * @param scoreManager the score manager for the current game
     */    
    public HUD(int runesCollected, int totalRunes, ScoreManager scoreManager) {

        this.scoreManager = scoreManager;
        this.runesCollected = runesCollected;
        this.totalRunes = totalRunes;

        objectiveLabel = createGameHUD(
            "Objective:\ncollect all the runes!",
            20, 15
        );
        runeLabel = createGameHUD("Runes: 0/" + totalRunes, 770, 15);
        scoreLabel = createGameHUD("Score: 0", 770, 50);
        timerLabel = createGameHUD("Time: 0", 1520, 15);
    }
    
    /**
     * Creates a pixel-styled HUD label using the pixel font and default HUD colors.
     * Each label is positioned absolutely on the screen at the given (x, y)
     * coordinates.
     *
     * @param text message to display in the label
     * @param x horizontal position on the screen
     * @param y vertical position on the screen
     * @return a fully constructed and styled Label instance
     */
    public Label createGameHUD(String text, double x, double y) {
        Label label = new Label(text);
        Font pixelFont = Font.loadFont(     
            getClass().getResourceAsStream("/fonts/PixelOperator-Bold.ttf"),
            30
        );
        label.setFont(pixelFont);
        label.setStyle("-fx-text-fill: #F2F2F2;" + "-fx-font-smoothing-type: gray;");
        label.setLayoutX(x);
        label.setLayoutY(y);
        return label;
    }
    
    /**
     * Updates the Rune Label UI to the correct number of runes player collected
     */
    public void updateRuneDisplay() {
        runeLabel.setText("Runes: " + runesCollected + "/" + totalRunes);
    }

    /**
     * Updates the Score Label UI to the current score of the game
     */
    public void updateScoreDisplay() {
        scoreLabel.setText("Score: " + scoreManager.getCurrentScore());
    }

    /**
     * Updates the current runesCollected to a new runesCollected
     * @param newRuneCollected the new amount of runes collected
     */
    public void updateRunesCollected(int newRuneCollected) {
        runesCollected = newRuneCollected;
    }

    /**
     * Updates the current object label UI of the game
     */
    public void updateObjective() {
        objectiveLabel.setText("Objective: \nFind the exit of the maze!");
    }
}
