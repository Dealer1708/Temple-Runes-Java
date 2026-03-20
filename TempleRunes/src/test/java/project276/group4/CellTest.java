package project276.group4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.types.Position;

/**
 * Tests for Cell class - map building blocks.
 */
public class CellTest {

    @Test
    public void testCellCreation() {
        Position pos = new Position(3, 4);
        Cell cell = new Cell(pos, CellType.FLOOR);
        
        assertEquals(pos, cell.getPosition());
        assertEquals(CellType.FLOOR, cell.getType());
    }

    @Test
    public void testCellTypeChange() {
        Cell cell = new Cell(new Position(0, 0), CellType.TRAP_HIDDEN);
        cell.setType(CellType.TRAP_REVEALED);
        
        assertEquals(CellType.TRAP_REVEALED, cell.getType());
    }

    @Test
    public void testWallBlocksMovement() {
        Cell wallCell = new Cell(new Position(0, 0), CellType.WALL);
        assertTrue(wallCell.blocksMovement());
        assertFalse(wallCell.isWalkable());
    }

    @Test
    public void testFloorIsWalkable() {
        Cell floorCell = new Cell(new Position(0, 0), CellType.FLOOR);
        assertFalse(floorCell.blocksMovement());
        assertTrue(floorCell.isWalkable());
    }

    @Test
    public void testLockedDoorBlocks() {
        Cell doorCell = new Cell(new Position(0, 0), CellType.DOOR);
        doorCell.setLocked(true);
        
        assertTrue(doorCell.blocksMovement());
        assertFalse(doorCell.isWalkable());
    }

    @Test
    public void testUnlockedDoorIsWalkable() {
        Cell doorCell = new Cell(new Position(0, 0), CellType.DOOR);
        doorCell.setLocked(false);
        
        assertFalse(doorCell.blocksMovement());
        assertTrue(doorCell.isWalkable());
    }

    @Test
    public void testExitStartsLocked() {
        Cell exitCell = new Cell(new Position(0, 0), CellType.EXIT);
        assertTrue(exitCell.isLocked());
    }
}