package project276.group4.domain;
import project276.group4.domain.interfaces.ScoreManager;

/**
 * Basic implementation of ScoreManager.
 * Tracks the player's score and enforces a negative threshold for game over.
 */
public class DefaultScoreManager implements ScoreManager {

    private int score;
    private final int gameOverThreshold;

    /**
     * Constructs a ScoreManager with a specified game over threshold.
     * 
     * @param gameOverThreshold the score below which the game should end
     */
    public DefaultScoreManager(int gameOverThreshold) {
        this.score = 0;
        this.gameOverThreshold = gameOverThreshold;
    }

    /**
     * Adds points to the current score.
     * @param points the number of points to add (non-negative)
     */
    @Override
    public void addScore(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to add must be non-negative");
        }
        score += points;
    }

    /**
     * Deducts points from the current score.
     * @param points the number of points to deduct (non-negative)
     */
    @Override
    public void deductScore(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to deduct must be non-negative");
        }
        score -= points;
    }

    /**
     * Gets the current score.
     * @return the current score
     */
    @Override
    public int getCurrentScore() {
        return score;
    }

    /**
     * Resets the score to zero.
     */
    @Override
    public void resetScore() {
        score = 0;
    }

    /**
     * Checks if the score is below the game over threshold.
     * @return true if score is less than threshold, false otherwise
     */
    @Override
    public boolean isScoreBelowThreshold() {
        return score < gameOverThreshold;
    }
}
