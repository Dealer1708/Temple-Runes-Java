package project276.group4.domain.entities;

import project276.group4.domain.types.Position;

/**
 * Abstract base class for all game entities that exist on the map.
 * Entities have a position and can be active or inactive.
 */
public abstract class Entity {
    protected Position position;
    protected boolean active;

    /**
     * Creates a new entity at the specified position.
     * The entity is active by default.
     * 
     * @param position the initial position of the entity
     * @throws IllegalArgumentException if position is null
     */
    protected Entity(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = position;
        this.active = true;
    }

    /**
     * Gets the current position of this entity.
     * 
     * @return the entity's position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this entity.
     * 
     * @param position the new position
     * @throws IllegalArgumentException if position is null
     */
    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = position;
    }

    /**
     * Checks if this entity is currently active.
     * Inactive entities should not participate in game logic.
     * 
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active state of this entity.
     * 
     * @param active the new active state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if this entity overlaps with another entity.
     * Two entities overlap if they occupy the same position.
     * 
     * @param other the other entity
     * @return true if positions are equal, false otherwise
     */
    public boolean overlaps(Entity other) {
        if (other == null) {
            return false;
        }
        return this.position.equals(other.position);
    }
}

