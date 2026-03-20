package project276.group4.domain.collision;

/**
 * Defines the priority order for collision resolution.
 * Higher priority collisions are processed first.
 */
public enum CollisionPriority {
    /** Highest priority - instant death */
    TRAP(4),
    
    /** High priority - instant death */
    MOVING_GUARDIAN(3),
    
    /** Medium priority - score deduction */
    CURSED_IDOL(2),
    
    /** Low priority - score addition */
    COLLECTIBLE(1),
    
    /** No collision detected */
    NONE(0);

    private final int priority;

    CollisionPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the numeric priority value.
     * 
     * @return the priority value (higher is more important)
     */
    public int getPriority() {
        return priority;
    }
}

