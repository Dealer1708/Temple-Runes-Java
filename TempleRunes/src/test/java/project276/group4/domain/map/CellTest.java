package project276.group4.domain.map;

import org.junit.jupiter.api.Test;
import project276.group4.domain.types.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Cell}.
 */
public class CellTest {

    @Test
    void constructor_NullPositionThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(null, CellType.FLOOR));
    }

    @Test
    void constructor_NullTypeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(new Position(0, 0), null));
    }

    @Test
    void exitStartsLocked() {
        Cell cell = new Cell(new Position(1, 1), CellType.EXIT);

        assertTrue(cell.isLocked());
        assertTrue(cell.blocksMovement());
    }

    @Test
    void setType_UpdatesBehavior() {
        Cell cell = new Cell(new Position(0, 0), CellType.FLOOR);

        cell.setType(CellType.WALL);

        assertEquals(CellType.WALL, cell.getType());
        assertTrue(cell.blocksMovement());
    }

    @Test
    void lockStateAffectsWalkability() {
        Cell cell = new Cell(new Position(0, 0), CellType.DOOR);

        cell.setLocked(true);
        assertTrue(cell.blocksMovement());

        cell.setLocked(false);
        assertFalse(cell.blocksMovement());
        assertTrue(cell.isWalkable());
    }
}

