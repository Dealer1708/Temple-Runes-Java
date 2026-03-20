package project276.group4.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.types.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Extended unit tests for Entity and subclasses.
 */
public class ExtendedEntityTest {

    private Player player;
    private Position startPosition;

    @BeforeEach
    public void setUp() {
        startPosition = new Position(5, 5);
        player = new Player(startPosition);
    }

    @Test
    public void testPlayerInitialization() {
        assertEquals(startPosition, player.getPosition());
        assertTrue(player.isAlive());
    }

    @Test
    public void testPlayerPositionUpdate() {
        Position newPosition = new Position(6, 6);
        player.setPosition(newPosition);
        assertEquals(newPosition, player.getPosition());
    }

    @Test
    public void testPlayerMultipleUpdates() {
        player.setPosition(new Position(7, 7));
        assertEquals(7, player.getPosition().getX());
        assertEquals(7, player.getPosition().getY());
        
        player.setPosition(new Position(10, 10));
        assertEquals(10, player.getPosition().getX());
        assertEquals(10, player.getPosition().getY());
    }

    @Test
    public void testPlayerEquality() {
        Player player2 = new Player(new Position(5, 5));
        Player player3 = new Player(new Position(6, 6));
        
        assertNotEquals(player2, player3);
    }

    @Test
    public void testPlayerAliveStatus() {
        assertTrue(player.isAlive());
        assertFalse(!player.isAlive());
    }

    @Test
    public void testMultipleEntityInstances() {
        Player player2 = new Player(new Position(10, 10));
        
        assertNotEquals(player.getPosition(), player2.getPosition());
        assertTrue(player.isAlive());
        assertTrue(player2.isAlive());
    }

    @Test
    public void testPlayerBoundaryPositions() {
        player.setPosition(new Position(0, 0));
        assertEquals(0, player.getPosition().getX());
        assertEquals(0, player.getPosition().getY());
        
        player.setPosition(new Position(100, 100));
        assertEquals(100, player.getPosition().getX());
        assertEquals(100, player.getPosition().getY());
    }

    @Test
    public void testPlayerNegativePositions() {
        // Test that negative positions are handled (may be invalid depending on rules)
        player.setPosition(new Position(-5, -5));
        assertEquals(-5, player.getPosition().getX());
        assertEquals(-5, player.getPosition().getY());
    }
}
