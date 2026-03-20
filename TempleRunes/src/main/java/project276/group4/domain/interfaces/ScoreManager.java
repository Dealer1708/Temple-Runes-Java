package project276.group4.domain.interfaces;

/**
 * Interface for managing game score operations.
 * Implementations should track the player's score and handle modifications.
 */
public interface ScoreManager {
    
    /**
     * Adds points to the current score.
     * 
     * @param points the number of points to add (must be non-negative)
     * @throws IllegalArgumentException if points is negative
     */
    void addScore(int points);
    
    /**
     * Deducts points from the current score.
     * Score can become negative, which may trigger game over conditions.
     * 
     * @param points the number of points to deduct (must be non-negative)
     * @throws IllegalArgumentException if points is negative
     */
    void deductScore(int points);
    
    /**
     * Gets the current score.
     * 
     * @return the current score (may be negative)
     */
    int getCurrentScore();
    
    /**
     * Resets the score to zero.
     */
    void resetScore();
    
    /**
     * Checks if the score has fallen below the game over threshold.
     * 
     * @return true if score is below the threshold (typically negative), false otherwise
     */
    boolean isScoreBelowThreshold();
}

