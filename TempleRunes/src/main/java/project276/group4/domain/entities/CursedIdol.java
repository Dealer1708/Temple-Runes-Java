package project276.group4.domain.entities;

import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.interfaces.ScoreManager;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;

/**
 * Represents a cursed idol in the game.
 * Touching a cursed idol deducts points from the player's score.
 * If the score falls below zero, the game is over.
 */
public class CursedIdol extends Entity {
    private final int scoreDeduction;
    private boolean touched;

    /**
     * Creates a new cursed idol at the specified position.
     * 
     * @param position the position of the idol
     * @param scoreDeduction the amount of score to deduct (must be positive)
     * @throws IllegalArgumentException if scoreDeduction is negative
     */
    public CursedIdol(Position position, int scoreDeduction) {
        super(position);
        if (scoreDeduction < 0) {
            throw new IllegalArgumentException("Score deduction must be non-negative");
        }
        this.scoreDeduction = scoreDeduction;
        this.touched = false;
    }

    /**
     * Creates a new cursed idol with default score deduction of 50.
     * 
     * @param position the position of the idol
     */
    public CursedIdol(Position position) {
        this(position, 50);
    }

    /**
     * Gets the score deduction value of this idol.
     * 
     * @return the score deduction amount
     */
    public int getScoreDeduction() {
        return scoreDeduction;
    }

    /**
     * Checks if this idol has been touched.
     * 
     * @return true if touched, false otherwise
     */
    public boolean isTouched() {
        return touched;
    }

    /**
     * Triggers the idol's curse when touched by the player.
     * Deducts score and may cause game over if score becomes negative.
     * 
     * @param scoreManager the score manager to update
     * @param signals the game signals to notify
     */
    public void onTrigger(ScoreManager scoreManager, GameSignals signals) {
        if (touched || !active || scoreManager == null || signals == null) {
            return;
        }

        touched = true;
        active = false;

        // Deduct score
        scoreManager.deductScore(scoreDeduction);

        // Signal the damage
        signals.onPlayerDamaged("CursedIdol");

        // Check if score went below threshold
        if (scoreManager.isScoreBelowThreshold()) {
            signals.triggerGameOver(GameOverReason.NEGATIVE_SCORE);
        }
    }

    @Override
    public String toString() {
        return "CursedIdol{" +
                "pos=" + position +
                ", scoreDeduction=" + scoreDeduction +
                ", touched=" + touched +
                '}';
    }
}

