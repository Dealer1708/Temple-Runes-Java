package project276.group4.domain.collision;

import project276.group4.domain.entities.Entity;

/**
 * Represents the outcome of a collision detection.
 * Contains information about what was hit and what action should be taken.
 */
public class CollisionOutcome {
    private final CollisionPriority priority;
    private final Entity collidedEntity;
    private final boolean gameOver;
    private final int scoreChange;
    private final boolean isRune;

    /**
     * Creates a new collision outcome.
     * 
     * @param priority the priority of this collision
     * @param collidedEntity the entity that was collided with
     * @param gameOver whether this collision ends the game
     * @param scoreChange the score change from this collision (positive or negative)
     * @param isRune whether this collision was with a rune
     */
    public CollisionOutcome(CollisionPriority priority, Entity collidedEntity, 
                           boolean gameOver, int scoreChange, boolean isRune) {
        this.priority = priority;
        this.collidedEntity = collidedEntity;
        this.gameOver = gameOver;
        this.scoreChange = scoreChange;
        this.isRune = isRune;
    }

    /**
     * Creates a no-collision outcome.
     * 
     * @return a collision outcome representing no collision
     */
    public static CollisionOutcome none() {
        return new CollisionOutcome(CollisionPriority.NONE, null, false, 0, false);
    }

    /**
     * Gets the priority of this collision.
     * 
     * @return the collision priority
     */
    public CollisionPriority getPriority() {
        return priority;
    }

    /**
     * Gets the entity that was collided with.
     * 
     * @return the collided entity, or null if no collision
     */
    public Entity getCollidedEntity() {
        return collidedEntity;
    }

    /**
     * Checks if this collision should end the game.
     * 
     * @return true if game over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the score change from this collision.
     * 
     * @return the score change (positive for gain, negative for loss)
     */
    public int getScoreChange() {
        return scoreChange;
    }

    /**
     * Checks if this collision was with a rune.
     * 
     * @return true if rune collision, false otherwise
     */
    public boolean isRune() {
        return isRune;
    }

    /**
     * Checks if there was no collision.
     * 
     * @return true if no collision occurred
     */
    public boolean isNone() {
        return priority == CollisionPriority.NONE;
    }

    @Override
    public String toString() {
        return "CollisionOutcome{" +
                "priority=" + priority +
                ", gameOver=" + gameOver +
                ", scoreChange=" + scoreChange +
                ", isRune=" + isRune +
                '}';
    }
}

