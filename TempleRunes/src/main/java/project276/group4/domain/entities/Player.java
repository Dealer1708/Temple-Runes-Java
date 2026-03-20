package project276.group4.domain.entities;

import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Direction;
import project276.group4.domain.types.Position;

/**
 * Represents the player character in the game.
 * The player can move in four directions and must respect map boundaries and walls.
 */
public class Player extends Entity {
    /** Maximum health value for the player. */
    private static final int MAX_HEALTH = 100;
    
    private int health;
    private boolean alive;

    /**
     * Creates a new player at the specified position.
     * Player starts with full health and alive status.
     * 
     * @param position the initial position of the player
     */
    public Player(Position position) {
        super(position);
        this.health = MAX_HEALTH;
        this.alive = true;
    }

    /**
     * Creates a new player at the specified position with custom health.
     * 
     * @param position the initial position of the player
     * @param health the initial health value
     */
    public Player(Position position, int health) {
        super(position);
        this.health = health;
        this.alive = health > 0;
    }

    /**
     * Gets the player's current health.
     * 
     * @return the health value
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the player's health.
     * Automatically updates alive status if health drops to zero or below.
     * 
     * @param health the new health value
     */
    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            this.alive = false;
            this.active = false;
        }
    }

    /**
     * Checks if the player is alive.
     * 
     * @return true if alive, false if dead
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Kills the player instantly.
     * Sets health to 0, alive to false, and deactivates the entity.
     */
    public void kill() {
        this.health = 0;
        this.alive = false;
        this.active = false;
    }

    /**
     * Attempts to move the player in the specified direction.
     * Movement is only successful if the target position is walkable on the map.
     * 
     * @param direction the direction to move
     * @param map the game map to check walkability
     * @return true if movement was successful, false if blocked
     */
    public boolean move(Direction direction, GameMap map) {
        if (!alive || !active) {
            return false;
        }

        Position newPosition = direction.apply(position);
        
        // Check if the new position is walkable
        if (map.isWalkable(newPosition)) {
            this.position = newPosition;
            return true;
        }
        
        return false;
    }

    /**
     * Attempts to move the player to a specific position.
     * This is useful for teleportation or direct positioning.
     * 
     * @param newPosition the target position
     * @param map the game map to check walkability
     * @return true if movement was successful, false if blocked
     */
    public boolean moveTo(Position newPosition, GameMap map) {
        if (!alive || !active) {
            return false;
        }

        if (map.isWalkable(newPosition)) {
            this.position = newPosition;
            return true;
        }
        
        return false;
    }

    /**
     * Damages the player by the specified amount.
     * 
     * @param damage the amount of damage to apply (must be non-negative)
     * @throws IllegalArgumentException if damage is negative
     */
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        setHealth(health - damage);
    }

    /**
     * Heals the player by the specified amount.
     * Health cannot exceed the maximum value.
     * 
     * @param amount the amount to heal (must be non-negative)
     * @throws IllegalArgumentException if amount is negative
     */
    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Heal amount cannot be negative");
        }
        setHealth(Math.min(MAX_HEALTH, health + amount));
    }

    @Override
    public String toString() {
        return "Player{" +
                "pos=" + position +
                ", health=" + health +
                ", alive=" + alive +
                '}';
    }
}

