package project276.group4.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.entities.Player;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Direction;
import project276.group4.domain.types.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link Player} movement interacting with {@link GameMap}.
 */
public class PlayerMovementIntegrationTest {

    private GameMap map;
    private Player player;

    @BeforeEach
    void setUp() {
        map = new GameMap(4, 4);
        fillWithFloor(map);
        player = new Player(new Position(0, 0));

        // Build a simple maze: place a blocking wall at (1,0)
        map.setCell(new Position(1, 0), new Cell(new Position(1, 0), CellType.WALL));
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void movementBlockedByWallsAndBounds() {
        boolean movedRight = player.move(Direction.RIGHT, map);
        boolean movedLeft = player.move(Direction.LEFT, map);

        assertFalse(movedRight, "wall should block movement");
        assertFalse(movedLeft, "out of bounds should fail");
        assertEquals(new Position(0, 0), player.getPosition());
    }

    @Test
    void navigateOpenPath_ReachesDestination() {
        // down, down, right, right
        assertTrue(player.move(Direction.DOWN, map));
        assertTrue(player.move(Direction.DOWN, map));
        assertTrue(player.move(Direction.RIGHT, map));
        assertTrue(player.move(Direction.RIGHT, map));

        assertEquals(new Position(2, 2), player.getPosition());
    }
}

