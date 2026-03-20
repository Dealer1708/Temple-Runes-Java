package project276.group4.domain.map;

import project276.group4.domain.types.Position;

/**
 * Represents a single cell in the game map grid.
 * Each cell has a position, type, and can be locked/unlocked.
 */
public class Cell {
    private final Position position;
    private CellType type;
    private boolean locked;

    /**
     * Creates a new cell at the specified position with the given type.
     * 
     * @param position the position of this cell in the grid
     * @param type the type of cell
     * @throws IllegalArgumentException if position or type is null
     */
    public Cell(Position position, CellType type) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("CellType cannot be null");
        }
        this.position = position;
        this.type = type;
        this.locked = (type == CellType.EXIT); // Exits start locked by default
    }

    /**
     * Gets the position of this cell.
     * 
     * @return the cell's position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the type of this cell.
     * 
     * @return the cell type
     */
    public CellType getType() {
        return type;
    }

    /**
     * Sets the type of this cell.
     * Useful for revealing hidden traps or changing cell states.
     * 
     * @param type the new cell type
     * @throws IllegalArgumentException if type is null
     */
    public void setType(CellType type) {
        if (type == null) {
            throw new IllegalArgumentException("CellType cannot be null");
        }
        this.type = type;
    }

    /**
     * Checks if this cell is locked.
     * Locked cells (like exit doors) cannot be entered.
     * 
     * @return true if locked, false otherwise
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the locked state of this cell.
     * 
     * @param locked the new locked state
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Checks if this cell blocks movement.
     * Walls and locked doors/exits block movement.
     * 
     * @return true if this cell blocks movement, false otherwise
     */
    public boolean blocksMovement() {
        if (type == CellType.WALL) {
            return true;
        }
        if ((type == CellType.DOOR || type == CellType.EXIT) && locked) {
            return true;
        }
        return false;
    }

    /**
     * Checks if this cell is walkable (does not block movement).
     * 
     * @return true if walkable, false otherwise
     */
    public boolean isWalkable() {
        return !blocksMovement();
    }

    @Override
    public String toString() {
        return "Cell{" +
                "pos=" + position +
                ", type=" + type +
                ", locked=" + locked +
                '}';
    }
}

