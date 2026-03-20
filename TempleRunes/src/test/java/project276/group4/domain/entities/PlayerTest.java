package project276.group4.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Direction;
import project276.group4.domain.types.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests covering {@link Player} movement, health, and state transitions.
 */
public class PlayerTest {

    private GameMap map;
    private Player player;

    @BeforeEach
    void setUp() {
        map = new GameMap(5, 5);
        fillWithFloor(map);
        player = new Player(new Position(2, 2));
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void move_WalkableCell_UpdatesPosition() {
        boolean moved = player.move(Direction.RIGHT, map);

        assertTrue(moved);
        assertEquals(new Position(3, 2), player.getPosition());
    }

    @Test
    void move_BlockedCell_DoesNotMove() {
        map.setCell(3, 2, new Cell(new Position(3, 2), CellType.WALL));

        boolean moved = player.move(Direction.RIGHT, map);

        assertFalse(moved);
        assertEquals(new Position(2, 2), player.getPosition());
    }

    @Test
    void move_OutOfBoundsCell_DoesNotMove() {
        player.setPosition(new Position(0, 0));

        boolean moved = player.move(Direction.LEFT, map);

        assertFalse(moved);
        assertEquals(new Position(0, 0), player.getPosition());
    }

    @Test
    void move_PlayerInactive_ReturnsFalse() {
        player.kill();

        boolean moved = player.move(Direction.RIGHT, map);

        assertFalse(moved);
        assertEquals(new Position(2, 2), player.getPosition());
    }

    @Test
    void moveTo_WalkablePositionRelocatesPlayer() {
        Position destination = new Position(1, 1);

        boolean moved = player.moveTo(destination, map);

        assertTrue(moved);
        assertEquals(destination, player.getPosition());
    }

    @Test
    void moveTo_BlockedPositionFails() {
        Position destination = new Position(4, 4);
        map.setCell(destination, new Cell(destination, CellType.WALL));

        boolean moved = player.moveTo(destination, map);

        assertFalse(moved);
        assertNotEquals(destination, player.getPosition());
    }

    @Test
    void takeDamage_LethalDamageKillsPlayer() {
        player.takeDamage(150);

        assertFalse(player.isAlive());
        assertFalse(player.isActive());
        assertTrue(player.getHealth() <= 0);
    }

    @Test
    void takeDamage_NegativeAmountThrows() {
        assertThrows(IllegalArgumentException.class, () -> player.takeDamage(-1));
    }

    @Test
    void heal_NegativeAmountThrows() {
        assertThrows(IllegalArgumentException.class, () -> player.heal(-5));
    }

    @Test
    void heal_DoesNotExceedMaxHealth() {
        player.takeDamage(60);

        player.heal(100);

        assertEquals(100, player.getHealth());
    }

    @Test
    void constructor_SetsDefaultStats() {
        Player fresh = new Player(new Position(1, 1));

        assertEquals(100, fresh.getHealth());
        assertTrue(fresh.isAlive());
        assertTrue(fresh.isActive());
    }

    @Test
    void kill_ImmediatelyStopsPlayer() {
        player.kill();

        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());
        assertFalse(player.isActive());
    }
}

