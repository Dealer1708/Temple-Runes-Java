package project276.group4.integration;

import org.junit.jupiter.api.Test;
import project276.group4.domain.collision.CollisionOutcome;
import project276.group4.domain.collision.CollisionPriority;
import project276.group4.domain.collision.CollisionSystem;
import project276.group4.domain.entities.*;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.ItemType;
import project276.group4.domain.types.Position;
import project276.group4.testutil.TestDoubles.RecordingGameSignals;
import project276.group4.testutil.TestDoubles.RecordingScoreManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link CollisionSystem} across entity types.
 */
public class CollisionDetectionIntegrationTest {

    private final GameMap map = new GameMap(5, 5);
    private final RecordingScoreManager sharedScores = new RecordingScoreManager(100);
    private final RecordingGameSignals sharedSignals = new RecordingGameSignals();

    public CollisionDetectionIntegrationTest() {
        fillWithFloor(map);
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void collisionsResolveInPriorityOrder() {
        Player player = new Player(new Position(2, 2));
        Trap trap = new Trap(new Position(2, 2));
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2));
        Collectible collectible = new Collectible(new Position(2, 2), ItemType.RUNE, 50, "Rune");

        CollisionOutcome outcome = CollisionSystem.resolveCollisions(
                player, List.of(player, trap, guardian, collectible), sharedScores, sharedSignals, map);

        assertEquals(CollisionPriority.TRAP, outcome.getPriority());
        assertTrue(outcome.isGameOver());
        assertEquals(GameOverReason.TRAP_TRIGGERED, sharedSignals.getLastGameOverReason());
    }

    @Test
    void sequentialCollisionsAffectScoreAndRunes() {
        RecordingScoreManager scoreManager = new RecordingScoreManager();
        RecordingGameSignals signals = new RecordingGameSignals();
        Player player = new Player(new Position(0, 0));
        List<Entity> entities = new ArrayList<>(List.of(player,
                new Collectible(new Position(0, 0), ItemType.RUNE, 100, "Rune")));

        CollisionOutcome first = CollisionSystem.resolveCollisions(player, entities, scoreManager, signals, map);
        assertEquals(100, scoreManager.getCurrentScore());
        assertEquals(CollisionPriority.COLLECTIBLE, first.getPriority());

        // Move player onto idol position
        player.setPosition(new Position(0, 1));
        entities.add(new CursedIdol(new Position(0, 1), 50));
        CollisionOutcome idolOutcome = CollisionSystem.resolveCollisions(player, entities, scoreManager, signals, map);

        assertEquals(CollisionPriority.CURSED_IDOL, idolOutcome.getPriority());
        assertEquals(50, scoreManager.getCurrentScore());
        assertTrue(signals.getDamageSources().contains("CursedIdol"));
    }
}

