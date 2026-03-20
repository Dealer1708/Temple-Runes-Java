package project276.group4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import project276.group4.domain.types.Position;

/**
 * Tests for the Position class - core coordinate handling.
 */
public class PositionTest {

    @Test
    public void testCreatePosition() {
        Position pos = new Position(5, 10);
        assertEquals(5, pos.getX());
        assertEquals(10, pos.getY());
    }

    @Test
    public void testPositionOffset() {
        Position pos = new Position(3, 4);
        Position newPos = pos.offset(2, -1);
        
        assertEquals(5, newPos.getX());
        assertEquals(3, newPos.getY());
        // Original unchanged
        assertEquals(3, pos.getX());
        assertEquals(4, pos.getY());
    }

    @Test
    public void testPositionEquality() {
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(2, 3);
        Position pos3 = new Position(2, 4);
        
        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
    }

    @Test
    public void testManhattanDistance() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3, 4);
        
        assertEquals(7, pos1.manhattanDistance(pos2));
    }
}