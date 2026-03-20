package project276.group4.domain.map;

import project276.group4.domain.types.Position;

/**
 * Represents the game map as a 2D grid of cells.
 * Provides methods for querying cell properties and checking walkability.
 */
public class GameMap {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    /**
     * Creates a new game map with the specified dimensions.
     * All cells are initialized as FLOOR type by default.
     * 
     * @param width the width of the map (number of columns)
     * @param height the height of the map (number of rows)
     * @throws IllegalArgumentException if width or height is less than 1
     */
    public GameMap(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Map dimensions must be at least 1x1");
        }
        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];
        
        /* 
        // Initialize all cells as floor
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new Cell(new Position(x, y), CellType.FLOOR);
            }
        }
         */
    }

    /**
     * Gets the width of the map.
     * 
     * @return the map width in cells
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the map.
     * 
     * @return the map height in cells
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the cell at the specified position.
     * 
     * @param position the position to query
     * @return the cell at that position, or null if out of bounds
     */
    public Cell getCell(Position position) {
        return getCell(position.getX(), position.getY());
    }

    /**
     * Gets the cell at the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the cell at those coordinates, or null if out of bounds
     */
    public Cell getCell(int x, int y) {
        if (!isInBounds(x, y)) {
            return null;
        }
        return cells[y][x];
    }

    /**
     * Sets the cell at the specified position.
     * 
     * @param position the position where to place the cell
     * @param cell the cell to place
     * @throws IllegalArgumentException if position or cell is null
     */
    public void setCell(Position position, Cell cell) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null");
        }
        setCell(position.getX(), position.getY(), cell);
    }

    /**
     * Sets the cell at the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param cell the cell to place
     * @throws IllegalArgumentException if coordinates are out of bounds or cell is null
     */
    public void setCell(int x, int y, Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null");
        }
        if (!isInBounds(x, y)) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        cells[y][x] = cell;
    }

    /**
     * Checks if the specified position is within the map bounds.
     * 
     * @param position the position to check
     * @return true if in bounds, false otherwise
     */
    public boolean isInBounds(Position position) {
        return isInBounds(position.getX(), position.getY());
    }

    /**
     * Checks if the specified coordinates are within the map bounds.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if in bounds, false otherwise
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Checks if the specified position is walkable.
     * A position is walkable if it's in bounds and the cell doesn't block movement.
     * 
     * @param position the position to check
     * @return true if walkable, false otherwise
     */
    public boolean isWalkable(Position position) {
        return isWalkable(position.getX(), position.getY());
    }

    /**
     * Checks if the specified coordinates are walkable.
     * A position is walkable if it's in bounds and the cell doesn't block movement.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if walkable, false otherwise
     */
    public boolean isWalkable(int x, int y) {
        if (!isInBounds(x, y)) {
            return false;
        }
        Cell cell = cells[y][x];
        return cell != null && cell.isWalkable();
    }

    /**
     * Finds and returns the position of the first cell of the specified type.
     * Useful for finding spawn points or exits.
     * 
     * @param type the cell type to search for
     * @return the position of the first matching cell, or null if not found
     */
    public Position findCellOfType(CellType type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x].getType() == type) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Checks if there's a clear line of sight between two positions.
     * Uses a simple ray-casting algorithm that checks if any walls block the path.
     * 
     * @param from the starting position
     * @param to the target position
     * @return true if there's a clear line of sight, false otherwise
     */
    public boolean hasLineOfSight(Position from, Position to) {
        if (!isInBounds(from) || !isInBounds(to)) {
            return false;
        }

        // Bresenham's line algorithm for line of sight
        int x0 = from.getX();
        int y0 = from.getY();
        int x1 = to.getX();
        int y1 = to.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x0 == x1 && y0 == y1) {
                return true;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }

            if (x0 == x1 && y0 == y1) {
                return true;
            }

            Cell cell = getCell(x0, y0);
            if (cell == null) {
                return false;
            }
            if (cell.blocksMovement()) {
                return false;
            }
        }
    }

    /**
     * Creates a GameMap instance from a 2D character matrix layout.
     * Each character in the matrix corresponds to a specific Cell Type
     * @param layout a 2D array of characters representing the map layout
     * @return a new GameMap with cells initialized according to the layout
     */
    public static GameMap fromMatrix(char[][] layout) {
        int height = layout.length;
        int width = layout[0].length;
        GameMap map = new GameMap(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = layout[y][x];
                CellType type;

                switch (c) {
                    case '#': type = CellType.WALL; break;
                    case 'D': type = CellType.DOOR; break;
                    case 'S': type = CellType.PLAYER_SPAWN; break;
                    case 'E': type = CellType.ENEMY_SPAWN; break;
                    default:  type = CellType.FLOOR; break;
                }

                map.setCell(x, y, new Cell(new Position(x, y), type));
            }
        }

        return map;
    }

    /**
     * Retrieves the type of the cell at the specified coordinates.
     *
     * @param x the x-coordinate (column index) of the cell
     * @param y the y-coordinate (row index) of the cell
     * @return the CellType of the cell at (x, y), or null if the coordinates are out of bounds
     */
    public CellType getCellType(int x, int y) {
        if (!isInBounds(x, y)) {
            return null;
        }
        return cells[y][x].getType();
    }

    /**
     * Finds random free positions on the map that are walkable and at a minimum distance from an exclusion point.
     * Useful for spawning entities randomly while avoiding the player spawn area.
     *
     * @param count the maximum number of positions to return
     * @param avoidPosition a position to stay away from (typically player spawn)
     * @param minDistance the minimum Manhattan distance from the avoidPosition
     * @return a list of random free positions (may be fewer than count if insufficient positions exist)
     */
    public java.util.List<Position> findRandomFreePositions(int count, Position avoidPosition, int minDistance) {
        java.util.List<Position> free = new java.util.ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CellType t = getCellType(x, y);
                if (t == CellType.FLOOR || t == CellType.ENEMY_SPAWN) {
                    Position p = new Position(x, y);
                    int dist = Math.abs(x - avoidPosition.getX()) + Math.abs(y - avoidPosition.getY());
                    if (dist >= minDistance) {
                        free.add(p);
                    }
                }
            }
        }
        java.util.Collections.shuffle(free);
        return free.subList(0, Math.min(count, free.size()));
    }

    /**
     * Finds positions that are closest to a set of target positions.
     * Useful for spawning guardians near collectibles.
     *
     * @param targets list of target positions to be near (e.g., rune locations)
     * @param count the maximum number of positions to return
     * @param avoidPosition a position to stay away from (typically player spawn)
     * @param minDistance the minimum Manhattan distance from the avoidPosition
     * @return a list of positions sorted by proximity to targets (may be fewer than count)
     */
    public java.util.List<Position> findPositionsNearTargets(java.util.List<Position> targets, int count, 
                                                              Position avoidPosition, int minDistance) {
        java.util.List<Position> free = new java.util.ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CellType t = getCellType(x, y);
                if (t == CellType.FLOOR || t == CellType.ENEMY_SPAWN) {
                    Position p = new Position(x, y);
                    int dist = Math.abs(x - avoidPosition.getX()) + Math.abs(y - avoidPosition.getY());
                    if (dist >= minDistance) {
                        free.add(p);
                    }
                }
            }
        }
        // Sort by minimum distance to any target
        free.sort((a, b) -> {
            int da = targets.stream()
                .mapToInt(t -> Math.abs(a.getX() - t.getX()) + Math.abs(a.getY() - t.getY()))
                .min()
                .orElse(999);
            int db = targets.stream()
                .mapToInt(t -> Math.abs(b.getX() - t.getX()) + Math.abs(b.getY() - t.getY()))
                .min()
                .orElse(999);
            return Integer.compare(da, db);
        });
        return free.subList(0, Math.min(count, free.size()));
    }

}

