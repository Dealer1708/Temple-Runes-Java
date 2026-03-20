package project276.group4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project276.group4.domain.map.GameMap;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.types.Position;

/**
 * Tests for the GameMap class functionality.
 */
public class GameMapTest {
    
    private GameMap testMap;
    
    @BeforeEach
    void setUp() {
        testMap = new GameMap(5, 5);
        // Initialize all cells as floor for basic testing
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                testMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }
    
    @Test
    void testMapCreation() {
        GameMap map = new GameMap(10, 15);
        
        assertEquals(10, map.getWidth());
        assertEquals(15, map.getHeight());
    }
    
    @Test
    void testInvalidMapDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new GameMap(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new GameMap(5, 0));
        assertThrows(IllegalArgumentException.class, () -> new GameMap(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> new GameMap(5, -1));
    }
    
    @Test
    void testSetAndGetCell() {
        Position pos = new Position(2, 3);
        Cell wallCell = new Cell(pos, CellType.WALL);
        
        testMap.setCell(pos, wallCell);
        Cell retrieved = testMap.getCell(pos);
        
        assertEquals(wallCell, retrieved);
        assertEquals(CellType.WALL, retrieved.getType());
    }
    
    @Test
    void testSetCellWithCoordinates() {
        Cell doorCell = new Cell(new Position(1, 2), CellType.DOOR);
        
        testMap.setCell(1, 2, doorCell);
        Cell retrieved = testMap.getCell(1, 2);
        
        assertEquals(doorCell, retrieved);
        assertEquals(CellType.DOOR, retrieved.getType());
    }
    
    @Test
    void testSetCellOutOfBounds() {
        Cell cell = new Cell(new Position(0, 0), CellType.WALL);
        
        assertThrows(IllegalArgumentException.class, () -> testMap.setCell(10, 10, cell));
        assertThrows(IllegalArgumentException.class, () -> testMap.setCell(-1, 0, cell));
        assertThrows(IllegalArgumentException.class, () -> testMap.setCell(0, -1, cell));
    }
    
    @Test
    void testSetNullCell() {
        assertThrows(IllegalArgumentException.class, () -> testMap.setCell(0, 0, null));
        assertThrows(IllegalArgumentException.class, () -> testMap.setCell(new Position(0, 0), null));
    }
    
    @Test
    void testSetCellWithNullPosition() {
        Cell cell = new Cell(new Position(0, 0), CellType.WALL);
        assertThrows(IllegalArgumentException.class, () -> testMap.setCell(null, cell));
    }
    
    @Test
    void testIsInBounds() {
        assertTrue(testMap.isInBounds(0, 0));
        assertTrue(testMap.isInBounds(4, 4));
        assertTrue(testMap.isInBounds(new Position(2, 2)));
        
        assertFalse(testMap.isInBounds(-1, 0));
        assertFalse(testMap.isInBounds(0, -1));
        assertFalse(testMap.isInBounds(5, 0));
        assertFalse(testMap.isInBounds(0, 5));
        assertFalse(testMap.isInBounds(new Position(10, 10)));
    }
    
    @Test
    void testIsWalkableFloorCell() {
        // Floor cells should be walkable
        assertTrue(testMap.isWalkable(new Position(1, 1)));
        assertTrue(testMap.isWalkable(2, 2));
    }
    
    @Test
    void testIsWalkableWallCell() {
        // Add a wall cell
        testMap.setCell(1, 1, new Cell(new Position(1, 1), CellType.WALL));
        
        assertFalse(testMap.isWalkable(new Position(1, 1)));
        assertFalse(testMap.isWalkable(1, 1));
    }
    
    @Test
    void testIsWalkableOutOfBounds() {
        assertFalse(testMap.isWalkable(new Position(-1, 0)));
        assertFalse(testMap.isWalkable(10, 10));
        assertFalse(testMap.isWalkable(-1, -1));
    }
    
    @Test
    void testFindCellOfType() {
        // Set a specific cell type
        testMap.setCell(2, 3, new Cell(new Position(2, 3), CellType.EXIT));
        
        Position exitPos = testMap.findCellOfType(CellType.EXIT);
        assertNotNull(exitPos);
        assertEquals(2, exitPos.getX());
        assertEquals(3, exitPos.getY());
        
        // Search for non-existent type
        Position nonExistent = testMap.findCellOfType(CellType.PLAYER_SPAWN);
        assertNull(nonExistent);
    }
    
    @Test
    void testGetCellOutOfBounds() {
        assertNull(testMap.getCell(new Position(-1, 0)));
        assertNull(testMap.getCell(10, 10));
        assertNull(testMap.getCell(-5, -5));
    }
    
    @Test
    void testGetCellType() {
        // Set a wall cell
        testMap.setCell(1, 1, new Cell(new Position(1, 1), CellType.WALL));
        
        assertEquals(CellType.WALL, testMap.getCellType(1, 1));
        assertEquals(CellType.FLOOR, testMap.getCellType(0, 0)); // Default floor
        assertNull(testMap.getCellType(-1, -1)); // Out of bounds
    }
    
    @Test
    void testFromMatrix() {
        char[][] layout = {
            {'#', '#', '#', '#', '#'},
            {'#', '.', '.', '.', '#'},
            {'#', '.', 'S', '.', '#'},
            {'#', '.', '.', 'E', '#'},
            {'#', '#', 'D', '#', '#'}
        };
        
        GameMap map = GameMap.fromMatrix(layout);
        
        assertEquals(5, map.getWidth());
        assertEquals(5, map.getHeight());
        assertEquals(CellType.WALL, map.getCellType(0, 0));
        assertEquals(CellType.FLOOR, map.getCellType(1, 1));
        assertEquals(CellType.PLAYER_SPAWN, map.getCellType(2, 2));
        assertEquals(CellType.ENEMY_SPAWN, map.getCellType(3, 3));
        assertEquals(CellType.DOOR, map.getCellType(2, 4));
    }
    
    @Test
    void testHasLineOfSightClear() {
        Position from = new Position(1, 1);
        Position to = new Position(3, 3);
        
        // All floor tiles - should have clear line of sight
        assertTrue(testMap.hasLineOfSight(from, to));
    }
    
    @Test
    void testHasLineOfSightBlocked() {
        Position from = new Position(1, 1);
        Position to = new Position(3, 1);
        
        // Add a wall in the middle
        testMap.setCell(2, 1, new Cell(new Position(2, 1), CellType.WALL));
        
        assertFalse(testMap.hasLineOfSight(from, to));
    }
    
    @Test
    void testHasLineOfSightOutOfBounds() {
        Position from = new Position(1, 1);
        Position outOfBounds = new Position(10, 10);
        
        assertFalse(testMap.hasLineOfSight(from, outOfBounds));
        assertFalse(testMap.hasLineOfSight(outOfBounds, from));
    }
    
    @Test
    void testHasLineOfSightSamePosition() {
        Position pos = new Position(2, 2);
        assertTrue(testMap.hasLineOfSight(pos, pos));
    }
}