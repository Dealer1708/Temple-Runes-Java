package project276.group4.domain.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.types.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GameMap}.
 */
public class GameMapTest {

    private GameMap map;

    @BeforeEach
    void setUp() {
        map = new GameMap(4, 3);
        fillWithFloor(map);
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void constructor_InvalidDimensions_Throws() {
        assertThrows(IllegalArgumentException.class, () -> new GameMap(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new GameMap(5, 0));
    }

    @Test
    void getCell_OutOfBoundsReturnsNull() {
        assertNull(map.getCell(-1, 0));
        assertNull(map.getCell(4, 1));
        assertNull(map.getCell(new Position(1, 3)));
    }

    @Test
    void setCell_UpdatesCellData() {
        Position pos = new Position(1, 1);
        Cell door = new Cell(pos, CellType.DOOR);

        map.setCell(pos, door);

        assertEquals(door, map.getCell(pos));
    }

    @Test
    void isWalkable_RespectsCellBlocking() {
        Position wallPos = new Position(2, 0);
        map.setCell(wallPos, new Cell(wallPos, CellType.WALL));

        assertFalse(map.isWalkable(wallPos));
        assertTrue(map.isWalkable(new Position(0, 0)));
    }

    @Test
    void findCellOfType_ReturnsFirstMatch() {
        Position exitPos = new Position(3, 2);
        map.setCell(exitPos, new Cell(exitPos, CellType.EXIT));

        assertEquals(exitPos, map.findCellOfType(CellType.EXIT));
    }

    @Test
    void hasLineOfSight_BlockedByWalls() {
        Position from = new Position(0, 0);
        Position to = new Position(3, 0);
        // place wall in the middle
        map.setCell(new Position(1, 0), new Cell(new Position(1, 0), CellType.WALL));

        assertFalse(map.hasLineOfSight(from, to));
    }

    @Test
    void hasLineOfSight_ClearPath() {
        Position from = new Position(0, 0);
        Position to = new Position(3, 2);

        assertTrue(map.hasLineOfSight(from, to));
    }
}

