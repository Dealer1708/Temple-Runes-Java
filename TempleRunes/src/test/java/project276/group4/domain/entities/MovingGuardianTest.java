package project276.group4.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;
import project276.group4.testutil.TestDoubles.RecordingGameSignals;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MovingGuardian}.
 */
public class MovingGuardianTest {

    private GameMap map;
    private RecordingGameSignals signals;

    @BeforeEach
    void setUp() {
        map = new GameMap(6, 6);
        fillWithFloor(map);
        signals = new RecordingGameSignals();
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void isPlayerInVision_WithLineOfSight_ReturnsTrue() {
        MovingGuardian guardian = new MovingGuardian(new Position(0, 0), 10, 1.0, 100);
        Player player = new Player(new Position(4, 4));

        assertTrue(guardian.isPlayerInVision(player, map));
    }

    @Test
    void isPlayerInVision_BlockedByWall_ReturnsFalse() {
        MovingGuardian guardian = new MovingGuardian(new Position(0, 0), 10, 1.0, 100);
        Player player = new Player(new Position(4, 4));
        // place wall on LOS path
        map.setCell(new Position(2, 2), new Cell(new Position(2, 2), CellType.WALL));

        assertFalse(guardian.isPlayerInVision(player, map));
    }

    @Test
    void performTurn_PlayerVisible_MovesTowardsPlayer() {
        MovingGuardian guardian = new MovingGuardian(new Position(0, 0), 10, 1.0, 100);
        Player player = new Player(new Position(4, 4));

        guardian.performTurn(player, map);

        Position newPos = guardian.getPosition();
        assertTrue(newPos.equals(new Position(1, 0)) || newPos.equals(new Position(0, 1)));
        assertTrue(guardian.isChasing());
    }

    @Test
    void performTurn_PlayerOutOfVision_DoesNotMove() {
        MovingGuardian guardian = new MovingGuardian(new Position(0, 0), 1, 1.0, 100);
        Player player = new Player(new Position(4, 4));
        // Block direct path so guardian cannot see player within range
        map.setCell(new Position(1, 0), new Cell(new Position(1, 0), CellType.WALL));
        map.setCell(new Position(0, 1), new Cell(new Position(0, 1), CellType.WALL));

        guardian.performTurn(player, map);

        assertEquals(new Position(0, 0), guardian.getPosition());
        assertFalse(guardian.isChasing());
    }

    @Test
    void onPlayerContact_KillsPlayerAndSignals() {
        MovingGuardian guardian = new MovingGuardian(new Position(0, 0));
        Player player = new Player(new Position(1, 1));

        guardian.onPlayerContact(player, signals);

        assertFalse(player.isAlive());
        assertEquals(GameOverReason.GUARDIAN_CONTACT, signals.getLastGameOverReason());
        assertEquals("MovingGuardian", signals.getDamageSources().get(0));
    }

    @Test
    void performTurn_SpeedBoostNearExit() {
        MovingGuardian guardian = new MovingGuardian(new Position(0, 0), 10, 1.0, 100);
        Player player = new Player(new Position(5, 5));
        Cell exit = new Cell(new Position(5, 5), CellType.EXIT);
        exit.setLocked(false);
        map.setCell(new Position(5, 5), exit);

        guardian.performTurn(player, map);

        assertTrue(guardian.getSpeed() > 1.0);
    }
}

