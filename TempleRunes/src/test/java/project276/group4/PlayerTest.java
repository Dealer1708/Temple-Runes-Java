package project276.group4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import project276.group4.domain.entities.Player;
import project276.group4.domain.types.Position;

/**
 * Tests for Player class - core game character.
 */
public class PlayerTest {

    private Player player;
    private Position startPos;

    @BeforeEach
    public void setUp() {
        startPos = new Position(5, 5);
        player = new Player(startPos);
    }

    @Test
    public void testPlayerCreation() {
        assertEquals(startPos, player.getPosition());
        assertEquals(100, player.getHealth());
        assertTrue(player.isAlive());
        assertTrue(player.isActive());
    }

    @Test
    public void testPlayerHealth() {
        player.setHealth(50);
        assertEquals(50, player.getHealth());
        assertTrue(player.isAlive());

        player.setHealth(0);
        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());
        assertFalse(player.isActive());
    }

    @Test
    public void testTakeDamage() {
        player.takeDamage(30);
        assertEquals(70, player.getHealth());
        assertTrue(player.isAlive());

        player.takeDamage(70);
        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());
    }

    @Test
    public void testHeal() {
        player.setHealth(50);
        player.heal(20);
        assertEquals(70, player.getHealth());

        // Test max health cap
        player.heal(50);
        assertEquals(100, player.getHealth());
    }

    @Test
    public void testKill() {
        player.kill();
        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());
        assertFalse(player.isActive());
    }
}