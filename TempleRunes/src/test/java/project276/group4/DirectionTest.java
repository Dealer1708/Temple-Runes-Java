package project276.group4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import project276.group4.domain.types.Direction;
import project276.group4.domain.types.Position;

/**
 * Tests for Direction enum - movement calculations.
 */
public class DirectionTest {

    @Test
    public void testDirectionDeltas() {
        assertEquals(0, Direction.UP.getDx());
        assertEquals(-1, Direction.UP.getDy());
        
        assertEquals(1, Direction.RIGHT.getDx());
        assertEquals(0, Direction.RIGHT.getDy());
    }

    @Test
    public void testDirectionApply() {
        Position start = new Position(5, 5);
        
        Position up = Direction.UP.apply(start);
        assertEquals(new Position(5, 4), up);
        
        Position right = Direction.RIGHT.apply(start);
        assertEquals(new Position(6, 5), right);
    }

    @Test
    public void testAllDirections() {
        Position origin = new Position(0, 0);
        
        assertEquals(new Position(0, -1), Direction.UP.apply(origin));
        assertEquals(new Position(0, 1), Direction.DOWN.apply(origin));
        assertEquals(new Position(-1, 0), Direction.LEFT.apply(origin));
        assertEquals(new Position(1, 0), Direction.RIGHT.apply(origin));
    }
}