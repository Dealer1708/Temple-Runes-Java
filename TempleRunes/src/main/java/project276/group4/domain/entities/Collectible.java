package project276.group4.domain.entities;

import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.interfaces.ScoreManager;
import project276.group4.domain.types.ItemType;
import project276.group4.domain.types.Position;

/**
 * Represents a collectible item in the game.
 * Collectibles provide score, unlock game progress, or provide other benefits.
 */
public class Collectible extends Entity {
    private final ItemType type;
    private final int scoreValue;
    private final String name;
    private boolean collected;

    /**
     * Creates a new collectible at the specified position.
     * 
     * @param position the position of the collectible
     * @param type the type of item
     * @param scoreValue the score value when collected
     * @param name the display name of the item
     */
    public Collectible(Position position, ItemType type, int scoreValue, String name) {
        super(position);
        this.type = type;
        this.scoreValue = scoreValue;
        this.name = name;
        this.collected = false;
    }

    /**
     * Gets the type of this collectible.
     * 
     * @return the item type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Gets the score value of this collectible.
     * 
     * @return the score value
     */
    public int getScoreValue() {
        return scoreValue;
    }

    /**
     * Gets the name of this collectible.
     * 
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this collectible has been collected.
     * 
     * @return true if collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Triggers the pickup event for this collectible.
     * Adds score, marks as collected, and deactivates the entity.
     * 
     * @param scoreManager the score manager to update
     * @param signals the game signals to notify
     */
    public void onPickup(ScoreManager scoreManager, GameSignals signals) {
        if (collected || !active || scoreManager == null || signals == null) {
            return;
        }

        collected = true;
        active = false;

        // Add score
        if (scoreValue > 0) {
            scoreManager.addScore(scoreValue);
        }

        // Signal the collection
        signals.onItemCollected(name, scoreValue);

        // Special handling for runes
        if (type == ItemType.RUNE) {
            // The game logic will check if all runes are collected
            // and unlock the exit if necessary
        }
    }

    @Override
    public String toString() {
        return "Collectible{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", scoreValue=" + scoreValue +
                ", collected=" + collected +
                ", pos=" + position +
                '}';
    }
}

