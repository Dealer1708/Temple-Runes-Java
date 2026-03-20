package project276.group4.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.entities.MovingGuardian;
import project276.group4.domain.entities.Player;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for guardian AI pathfinding across the map.
 */
public class EnemyAIIntegrationTest {

    private GameMap map;
    private Player player;
    private MovingGuardian guardian;

    @BeforeEach
    void setUp() {
        map = new GameMap(5, 5);
        fillWithFloor(map);
        player = new Player(new Position(4, 4));
        guardian = new MovingGuardian(new Position(0, 0), 10, 1.0, 100);
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void guardianChasesPlayerOverMultipleTurns() {
        int turns = 0;
        while (!guardian.getPosition().equals(player.getPosition()) && turns < 10) {
            guardian.performTurn(player, map);
            turns++;
        }

        assertEquals(player.getPosition(), guardian.getPosition());
        assertTrue(turns > 0 && turns <= 10);
    }
}

