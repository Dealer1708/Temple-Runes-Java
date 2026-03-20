package project276.group4.UI;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import project276.group4.domain.DefaultScoreManager;
import project276.group4.domain.entities.Player;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;
import project276.group4.ui.DefaultGameSignals;
import project276.group4.ui.Entity.CursedIdolManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CursedIdolManager.
 */
public class CursedIdolManagerTest {

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX if needed
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already started, ignore
        }
    }

    /**
     * Test signals to track if methods are called.
     */
    public static class TestSignals extends DefaultGameSignals {
        public boolean playerDamagedCalled = false;
        public boolean idolTouchedCalled = false;
        public boolean gameOverTriggered = false;
        public GameOverReason lastReason = null;

        public TestSignals() {
            super(null, null, null);
        }

        @Override
        public void onPlayerDamaged(String source) {
            playerDamagedCalled = true;
        }

        @Override
        public void onCursedIdolTouched() {
            idolTouchedCalled = true;
        }

        @Override
        public void triggerGameOver(GameOverReason reason) {
            gameOverTriggered = true;
            lastReason = reason;
        }
    }

    @Test
    public void testPlayerTouchesCursedIdol() throws InterruptedException {
        Pane root = new Pane();
        Player player = new Player(new Position(0, 0));
        DefaultScoreManager scoreManager = new DefaultScoreManager(0);
        TestSignals signals = new TestSignals();

        // create spawn positions list
        List<Position> spawnPositions = Arrays.asList(new Position(1, 1));

        CursedIdolManager manager = new CursedIdolManager(root, player, signals, scoreManager, spawnPositions, null);

        // initialize on JavaFX thread
        java.util.concurrent.CountDownLatch initLatch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(() -> {
            manager.initialize();
            initLatch.countDown();
        });
        initLatch.await();

        // move player to idol position and update
        player.setPosition(new Position(1, 1));

        java.util.concurrent.CountDownLatch updateLatch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(() -> {
            manager.update();
            updateLatch.countDown();
        });
        updateLatch.await();

        assertTrue(signals.playerDamagedCalled, "Player should be damaged");
        assertTrue(signals.idolTouchedCalled, "Idol touched should be called");
        assertEquals(-3, scoreManager.getCurrentScore(), "Score should be deducted by 3");
    }

    @Test
    public void testGameOverTriggeredWhenScoreNegative() throws InterruptedException {
        Pane root = new Pane();
        Player player = new Player(new Position(0, 0));
        DefaultScoreManager scoreManager = new DefaultScoreManager(0);
        TestSignals signals = new TestSignals();

        // create spawn positions list
        List<Position> spawnPositions = Arrays.asList(new Position(1, 1));

        CursedIdolManager manager = new CursedIdolManager(root, player, signals, scoreManager, spawnPositions, null);
        manager.setScoreDeduction(10); // ensure score goes negative

        // initialize on JavaFX thread
        java.util.concurrent.CountDownLatch initLatch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(() -> {
            manager.initialize();
            initLatch.countDown();
        });
        initLatch.await();

        // move player to idol position and update
        player.setPosition(new Position(1, 1));

        java.util.concurrent.CountDownLatch updateLatch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(() -> {
            manager.update();
            updateLatch.countDown();
        });
        updateLatch.await();

        assertTrue(signals.gameOverTriggered, "Game over should be triggered");
        assertEquals(GameOverReason.NEGATIVE_SCORE, signals.lastReason);
    }

    @Test
    public void testPlayerDoesNotTouchIdol_NoAction() throws InterruptedException {
        Pane root = new Pane();
        Player player = new Player(new Position(0, 0));
        DefaultScoreManager scoreManager = new DefaultScoreManager(0);
        TestSignals signals = new TestSignals();

        // create spawn positions list
        List<Position> spawnPositions = Arrays.asList(new Position(2, 2));

        CursedIdolManager manager = new CursedIdolManager(root, player, signals, scoreManager, spawnPositions, null);

        // initialize on JavaFX thread
        java.util.concurrent.CountDownLatch initLatch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(() -> {
            manager.initialize();
            initLatch.countDown();
        });
        initLatch.await();

        // move player to different position (not on idol)
        player.setPosition(new Position(1, 1));

        java.util.concurrent.CountDownLatch updateLatch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(() -> {
            manager.update();
            updateLatch.countDown();
        });
        updateLatch.await();

        assertFalse(signals.playerDamagedCalled, "Player should not be damaged");
        assertFalse(signals.idolTouchedCalled, "Idol touched should not be called");
        assertEquals(0, scoreManager.getCurrentScore(), "Score should not be deducted");
    }
}