package project276.group4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import project276.group4.domain.entities.Trap;
import project276.group4.domain.entities.Player;
import project276.group4.domain.types.Position;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.interfaces.GameSignals;

/**
 * Tests for Trap class - deadly game mechanics.
 */
public class TrapTest {

    private Trap trap;
    private Player player;

    // Mock GameSignals for testing
    private static class MockGameSignals implements GameSignals {
        private GameOverReason lastReason;
        private boolean gameOverTriggered = false;
        
        @Override
        public void triggerGameOver(GameOverReason reason) {
            this.lastReason = reason;
            this.gameOverTriggered = true;
        }
        
        @Override
        public void triggerVictory() {}
        @Override
        public void unlockExit() {}
        @Override
        public void onItemCollected(String itemName, int scoreValue) {}
        @Override
        public void onPlayerDamaged(String damageSource) {}
        @Override
        public void onCollectRune() {}
        @Override
        public void setRuneTotals(int total, int collected) {}

        @Override
        public void onTrapSteppedOn() {}

        @Override
        public void onCursedIdolTouched() {}
    }

    @BeforeEach
    public void setUp() {
        trap = new Trap(new Position(3, 4));
        player = new Player(new Position(2, 3));
    }

    @Test
    public void testTrapCreation() {
        assertFalse(trap.isTriggered());
        assertFalse(trap.isRevealed());
        assertTrue(trap.isActive());
    }

    @Test
    public void testRevealTrap() {
        trap.reveal();
        assertTrue(trap.isRevealed());
        assertFalse(trap.isTriggered());
    }

    @Test
    public void testTriggerTrap() {
        MockGameSignals signals = new MockGameSignals();
        
        assertTrue(player.isAlive());
        
        trap.onTrigger(player, signals);
        
        assertTrue(trap.isTriggered());
        assertTrue(trap.isRevealed());
        assertFalse(trap.isActive());
        assertFalse(player.isAlive());
        assertTrue(signals.gameOverTriggered);
        assertEquals(GameOverReason.TRAP_TRIGGERED, signals.lastReason);
    }

    @Test
    public void testTriggerInactiveTrap() {
        MockGameSignals signals = new MockGameSignals();
        
        trap.setActive(false);
        trap.onTrigger(player, signals);
        
        assertFalse(trap.isTriggered());
        assertTrue(player.isAlive());
        assertFalse(signals.gameOverTriggered);
    }
}