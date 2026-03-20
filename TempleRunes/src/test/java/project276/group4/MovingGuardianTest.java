package project276.group4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project276.group4.domain.entities.MovingGuardian;
import project276.group4.domain.entities.Player;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Position;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.interfaces.GameSignals;

/**
 * Tests for the MovingGuardian class functionality.
 */
public class MovingGuardianTest {
    
    private GameMap testMap;
    private Player testPlayer;
    private MockGameSignals gameSignals;
    
    @BeforeEach
    void setUp() {
        // Create a simple 10x10 test map with walls around edges and floor inside
        testMap = new GameMap(10, 10);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                CellType type = (x == 0 || y == 0 || x == 9 || y == 9) ? CellType.WALL : CellType.FLOOR;
                testMap.setCell(x, y, new Cell(new Position(x, y), type));
            }
        }
        
        testPlayer = new Player(new Position(5, 5));
        gameSignals = new MockGameSignals();
    }
    
    @Test
    void testGuardianCreation() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        
        assertEquals(5, guardian.getVisionRange());
        assertEquals(1.0, guardian.getSpeed());
        assertEquals(100, guardian.getDamage());
        assertFalse(guardian.isChasing());
        assertTrue(guardian.isActive());
        assertEquals(new Position(2, 2), guardian.getPosition());
    }
    
    @Test
    void testDefaultConstructor() {
        MovingGuardian guardian = new MovingGuardian(new Position(3, 3));
        
        assertEquals(5, guardian.getVisionRange());
        assertEquals(1.0, guardian.getSpeed());
        assertEquals(100, guardian.getDamage());
        assertFalse(guardian.isChasing());
    }
    
    @Test
    void testPlayerInVisionRange() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        Player nearPlayer = new Player(new Position(4, 4)); // Within range
        Player farPlayer = new Player(new Position(8, 8)); // Outside range
        
        assertTrue(guardian.isPlayerInVision(nearPlayer, testMap));
        assertFalse(guardian.isPlayerInVision(farPlayer, testMap));
    }
    
    @Test
    void testPlayerInVisionWithLineOfSight() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        
        // Add a wall that blocks vision
        testMap.setCell(3, 2, new Cell(new Position(3, 2), CellType.WALL));
        Player blockedPlayer = new Player(new Position(4, 2)); // Same line but blocked by wall
        
        assertFalse(guardian.isPlayerInVision(blockedPlayer, testMap));
    }
    
    @Test
    void testPlayerInVisionWithInactiveEntities() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        Player inactivePlayer = new Player(new Position(3, 3));
        inactivePlayer.kill(); // Make player inactive
        
        assertFalse(guardian.isPlayerInVision(inactivePlayer, testMap));
        
        guardian.setActive(false);
        assertTrue(testPlayer.isActive());
        assertFalse(guardian.isPlayerInVision(testPlayer, testMap));
    }
    
    @Test
    void testPerformTurnChasing() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        Player closePlayer = new Player(new Position(3, 3));
        Position originalPosition = guardian.getPosition();
        
        guardian.performTurn(closePlayer, testMap);
        
        assertTrue(guardian.isChasing());
        // Guardian should move towards player (position should change)
        assertNotEquals(originalPosition, guardian.getPosition());
    }
    
    @Test
    void testPerformTurnPatrolling() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 3, 1.0, 100);
        Player farPlayer = new Player(new Position(8, 8)); // Outside vision range
        Position originalPosition = guardian.getPosition();
        
        guardian.performTurn(farPlayer, testMap);
        
        assertFalse(guardian.isChasing());
        // Guardian should still move (patrol behavior) but position might change
    }
    
    @Test
    void testPlayerContactTriggersGameOver() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        Player player = new Player(new Position(3, 3));
        
        assertTrue(player.isAlive());
        guardian.onPlayerContact(player, gameSignals);
        
        assertFalse(player.isAlive());
        assertEquals(GameOverReason.GUARDIAN_CONTACT, gameSignals.getLastGameOverReason());
        assertEquals("MovingGuardian", gameSignals.getLastDamageSource());
    }
    
    @Test
    void testInactiveGuardianNoContact() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        guardian.setActive(false);
        Player player = new Player(new Position(3, 3));
        
        guardian.onPlayerContact(player, gameSignals);
        
        assertTrue(player.isAlive()); // Player should remain alive
        assertNull(gameSignals.getLastGameOverReason()); // No game over triggered
    }
    
    @Test
    void testSpeedModification() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.0, 100);
        
        guardian.setSpeed(2.5);
        assertEquals(2.5, guardian.getSpeed());
    }
    
    @Test
    void testToStringRepresentation() {
        MovingGuardian guardian = new MovingGuardian(new Position(2, 2), 5, 1.5, 100);
        String str = guardian.toString();
        
        assertTrue(str.contains("MovingGuardian"));
        assertTrue(str.contains("pos="));
        assertTrue(str.contains("visionRange=5"));
        assertTrue(str.contains("speed="));
        assertTrue(str.contains("chasing="));
    }
    
    // Mock class for testing
    private static class MockGameSignals implements GameSignals {
        private GameOverReason lastGameOverReason;
        private String lastDamageSource;
        
        @Override
        public void triggerGameOver(GameOverReason reason) {
            this.lastGameOverReason = reason;
        }
        
        @Override
        public void onPlayerDamaged(String source) {
            this.lastDamageSource = source;
        }
        
        public GameOverReason getLastGameOverReason() {
            return lastGameOverReason;
        }
        
        public String getLastDamageSource() {
            return lastDamageSource;
        }
        
        @Override
        public void triggerVictory() {}
        
        @Override
        public void unlockExit() {}
        
        @Override
        public void onItemCollected(String itemName, int scoreValue) {}
        
        @Override
        public void onCollectRune() {}
        
        @Override
        public void setRuneTotals(int total, int collected) {}

        @Override
        public void onTrapSteppedOn() {}

        @Override
        public void onCursedIdolTouched() {}

    }
}