package project276.group4.UI;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.entities.Player;
import project276.group4.domain.types.Position;
import project276.group4.domain.types.GameOverReason;
import project276.group4.ui.DefaultGameSignals;
import project276.group4.ui.Entity.GuardianManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GuardianManager.
 */
public class GuardianManagerTest {

    @BeforeAll
    public static void initJavaFX() {
        JavaFXInitializer.initialize();
    }

    // Simple mock signals class to track calls
    public static class TestSignals extends DefaultGameSignals {

        public boolean playerDamagedCalled = false;
        public boolean gameOverTriggered = false;
        public GameOverReason lastReason = null; // <-- store last reason

        public TestSignals() {
            super(null, null, null);  
        }

        @Override
        public void onPlayerDamaged(String source) {
            playerDamagedCalled = true;
        }

        @Override
        public void triggerGameOver(GameOverReason reason) {
            gameOverTriggered = true;
            lastReason = reason; 
        }
    }

    @Test
    public void testPlayerCollisionTriggersDamageAndGameOver() throws Exception {
        Pane root = new Pane();
        Player player = new Player(new Position(1, 1));
        GameMap map = new GameMap(5, 5);
        TestSignals signals = new TestSignals();
        List<Position> guardianPositions = Arrays.asList(new Position(1, 1));

        GuardianManager gm = new GuardianManager(root, player, map, signals, guardianPositions);

        CountDownLatch initLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gm.initialize();
            initLatch.countDown();
        });
        initLatch.await();

        // wait for JavaFX thread
        Thread.sleep(200);

        CountDownLatch updateLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gm.update();
            updateLatch.countDown();
        });
        updateLatch.await();

        assertTrue(signals.playerDamagedCalled, "Player should be damaged");
        assertTrue(signals.gameOverTriggered, "Game over should be triggered");
        assertEquals(GameOverReason.GUARDIAN_CONTACT, signals.lastReason);
    }

    @Test
    public void testGuardianMovesWithoutColliding() throws Exception {
        Pane root = new Pane();
        Player player = new Player(new Position(0, 0));
        GameMap map = new GameMap(5, 5);
        TestSignals signals = new TestSignals();
        List<Position> guardianPositions = Arrays.asList(new Position(3, 3)); // guardian away from player

        GuardianManager gm = new GuardianManager(root, player, map, signals, guardianPositions);

        CountDownLatch initLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gm.initialize();
            initLatch.countDown();
        });
        initLatch.await();

        Thread.sleep(200);

        CountDownLatch updateLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gm.update();
            updateLatch.countDown();
        });
        updateLatch.await();

        assertFalse(signals.playerDamagedCalled, "Player should not be damaged");
        assertFalse(signals.gameOverTriggered, "Game over should not trigger");
    }
}