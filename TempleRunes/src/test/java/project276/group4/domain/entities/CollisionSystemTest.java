package project276.group4.domain.entities;

import org.junit.jupiter.api.Test;
import project276.group4.domain.collision.CollisionOutcome;
import project276.group4.domain.collision.CollisionPriority;
import project276.group4.domain.collision.CollisionSystem;
import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.interfaces.ScoreManager;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.ItemType;
import project276.group4.domain.types.Position;
import project276.group4.testutil.TestDoubles.RecordingGameSignals;
import project276.group4.testutil.TestDoubles.RecordingScoreManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for {@link CollisionSystem}.
 */
public class CollisionSystemTest {

    private final GameMap map = new GameMap(5, 5);
    private final RecordingScoreManager scoreManager = new RecordingScoreManager();
    private final RecordingGameSignals signals = new RecordingGameSignals();

    public CollisionSystemTest() {
        fillWithFloor(map);
        map.setCell(new Position(4, 4), new Cell(new Position(4, 4), CellType.EXIT));
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void resolveCollisions_TrapsOverrideOtherEntities() {
        Player player = new Player(new Position(1, 1));
        Trap trap = new Trap(new Position(1, 1));
        Collectible collectible = new Collectible(new Position(1, 1), ItemType.COIN, 10, "Coin");

        CollisionOutcome outcome = CollisionSystem.resolveCollisions(
                player,
                List.of(player, trap, collectible),
                scoreManager,
                signals,
                map
        );

        assertEquals(CollisionPriority.TRAP, outcome.getPriority());
        assertTrue(outcome.isGameOver());
        assertEquals(GameOverReason.TRAP_TRIGGERED, signals.getLastGameOverReason());
    }

    @Test
    void resolveCollisions_GuardianBeatsIdol() {
        Player player = new Player(new Position(2, 2));
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2));
        CursedIdol idol = new CursedIdol(new Position(2, 2), 30);

        CollisionOutcome outcome = CollisionSystem.resolveCollisions(
                player,
                List.of(player, guardian, idol),
                scoreManager,
                signals,
                map
        );

        assertEquals(CollisionPriority.MOVING_GUARDIAN, outcome.getPriority());
        assertTrue(outcome.isGameOver());
        assertEquals(GameOverReason.GUARDIAN_CONTACT, signals.getLastGameOverReason());
    }

    @Test
    void resolveCollisions_CollectibleUpdatesScoreWhenNoHigherPriority() {
        Player player = new Player(new Position(0, 0));
        Collectible rune = new Collectible(new Position(0, 0), ItemType.RUNE, 200, "Rune");
        Collectible coin = new Collectible(new Position(0, 0), ItemType.COIN, 50, "Coin");

        CollisionOutcome outcome = CollisionSystem.resolveCollisions(
                player,
                List.of(player, rune, coin),
                scoreManager,
                signals,
                map
        );

        assertEquals(CollisionPriority.COLLECTIBLE, outcome.getPriority());
        assertEquals(200, outcome.getScoreChange());
        assertEquals(200, scoreManager.getCurrentScore());
        assertEquals(1, signals.getRuneCollectedCount());
    }

    @Test
    void checkCollision_InactiveEntitiesReturnFalse() {
        Player player = new Player(new Position(0, 0));
        Collectible collectible = new Collectible(new Position(0, 0), ItemType.COIN, 10, "Coin");
        collectible.onPickup(scoreManager, signals);

        boolean collision = CollisionSystem.checkCollision(player, collectible);

        assertFalse(collision);
    }

    @Test
    void checkCollision_DifferentPositionsReturnFalse() {
        Player player = new Player(new Position(0, 0));
        Collectible collectible = new Collectible(new Position(1, 0), ItemType.COIN, 10, "Coin");

        assertFalse(CollisionSystem.checkCollision(player, collectible));
    }
}

