package project276.group4.domain.types;

import java.util.Objects;

/**
 * Immutable position representation in a 2D grid.
 * Uses integer coordinates where (0,0) is typically the top-left corner.
 */
public final class Position {
    private final int x;
    private final int y;

    /**
     * Creates a new position at the specified coordinates.
     * 
     * @param x the x-coordinate (horizontal position)
     * @param y the y-coordinate (vertical position)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of this position.
     * 
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this position.
     * 
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Creates a new position offset by the given deltas.
     * 
     * @param dx the change in x
     * @param dy the change in y
     * @return a new Position instance
     */
    public Position offset(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    /**
     * Calculates the Manhattan distance to another position.
     * 
     * @param other the other position
     * @return the Manhattan distance (|x1-x2| + |y1-y2|)
     */
    public int manhattanDistance(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    /**
     * Calculates the Euclidean distance to another position.
     * 
     * @param other the other position
     * @return the Euclidean distance
     */
    public double euclideanDistance(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

