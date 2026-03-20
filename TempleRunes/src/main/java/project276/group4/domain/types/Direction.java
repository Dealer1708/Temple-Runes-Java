package project276.group4.domain.types;

/**
 * Represents the four cardinal directions for movement in the game.
 * Each direction provides its corresponding delta values for position changes.
 */
public enum Direction {
    /** Move upward (decreasing y) */
    UP(0, -1),
    
    /** Move downward (increasing y) */
    DOWN(0, 1),
    
    /** Move left (decreasing x) */
    LEFT(-1, 0),
    
    /** Move right (increasing x) */
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    /**
     * Creates a direction with the specified delta values.
     * 
     * @param dx the change in x-coordinate
     * @param dy the change in y-coordinate
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Gets the x-component of this direction.
     * 
     * @return the delta-x value
     */
    public int getDx() {
        return dx;
    }

    /**
     * Gets the y-component of this direction.
     * 
     * @return the delta-y value
     */
    public int getDy() {
        return dy;
    }

    /**
     * Applies this direction to a position, creating a new position.
     * 
     * @param position the starting position
     * @return a new position offset by this direction
     */
    public Position apply(Position position) {
        return position.offset(dx, dy);
    }
}

