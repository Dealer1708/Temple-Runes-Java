package project276.group4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project276.group4.domain.entities.Collectible;
import project276.group4.domain.types.ItemType;
import project276.group4.domain.types.Position;
import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.interfaces.ScoreManager;

/**
 * Tests for the Collectible class functionality.
 */
public class CollectibleTest {
    
    private Position testPosition;
    private MockScoreManager scoreManager;
    private MockGameSignals gameSignals;
    
    @BeforeEach
    void setUp() {
        testPosition = new Position(5, 10);
        scoreManager = new MockScoreManager();
        gameSignals = new MockGameSignals();
    }
    
    @Test
    void testCollectibleCreation() {
        Collectible rune = new Collectible(testPosition, ItemType.RUNE, 100, "Ancient Rune");
        
        assertEquals(ItemType.RUNE, rune.getType());
        assertEquals(100, rune.getScoreValue());
        assertEquals("Ancient Rune", rune.getName());
        assertFalse(rune.isCollected());
        assertTrue(rune.isActive());
        assertEquals(testPosition, rune.getPosition());
    }
    
    @Test
    void testPickupAddsScore() {
        Collectible coin = new Collectible(testPosition, ItemType.COIN, 50, "Gold Coin");
        
        coin.onPickup(scoreManager, gameSignals);
        
        assertEquals(50, scoreManager.getScore());
        assertTrue(coin.isCollected());
        assertFalse(coin.isActive());
    }
    
    @Test
    void testPickupTriggersSignals() {
        Collectible artifact = new Collectible(testPosition, ItemType.ARTIFACT, 200, "Temple Artifact");
        
        artifact.onPickup(scoreManager, gameSignals);
        
        assertEquals("Temple Artifact", gameSignals.getLastCollectedItemName());
        assertEquals(200, gameSignals.getLastCollectedItemValue());
    }
    
    @Test
    void testZeroScoreValue() {
        Collectible key = new Collectible(testPosition, ItemType.KEY, 0, "Temple Key");
        
        key.onPickup(scoreManager, gameSignals);
        
        assertEquals(0, scoreManager.getScore());
        assertTrue(key.isCollected());
        assertEquals("Temple Key", gameSignals.getLastCollectedItemName());
        assertEquals(0, gameSignals.getLastCollectedItemValue());
    }
    
    @Test
    void testPickupWhenAlreadyCollected() {
        Collectible potion = new Collectible(testPosition, ItemType.HEALTH_POTION, 25, "Health Potion");
        
        // First pickup
        potion.onPickup(scoreManager, gameSignals);
        int firstScore = scoreManager.getScore();
        
        // Second pickup attempt
        potion.onPickup(scoreManager, gameSignals);
        
        assertEquals(firstScore, scoreManager.getScore()); // Score shouldn't change
        assertTrue(potion.isCollected());
        assertFalse(potion.isActive());
    }
    
    @Test
    void testPickupWithNullManagers() {
        Collectible rune = new Collectible(testPosition, ItemType.RUNE, 100, "Ancient Rune");
        
        // Should not crash with null parameters
        rune.onPickup(null, null);
        rune.onPickup(scoreManager, null);
        rune.onPickup(null, gameSignals);
        
        // Should remain unchanged
        assertFalse(rune.isCollected());
        assertTrue(rune.isActive());
    }
    
    @Test
    void testToStringRepresentation() {
        Collectible collectible = new Collectible(testPosition, ItemType.RUNE, 100, "Test Rune");
        String str = collectible.toString();
        
        assertTrue(str.contains("RUNE"));
        assertTrue(str.contains("Test Rune"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("collected=false"));
        assertTrue(str.contains(testPosition.toString()));
    }
    
    // Mock classes for testing
    private static class MockScoreManager implements ScoreManager {
        private int score = 0;
        
        @Override
        public void addScore(int points) {
            score += points;
        }
        
        @Override
        public void deductScore(int points) {
            score -= points;
        }
        
        @Override
        public int getCurrentScore() {
            return score;
        }
        
        public int getScore() {
            return score;
        }
        
        @Override
        public void resetScore() {
            score = 0;
        }
        
        @Override
        public boolean isScoreBelowThreshold() {
            return score < 0;
        }
    }
    
    private static class MockGameSignals implements GameSignals {
        private String lastCollectedItemName;
        private int lastCollectedItemValue;
        
        @Override
        public void onItemCollected(String itemName, int scoreValue) {
            this.lastCollectedItemName = itemName;
            this.lastCollectedItemValue = scoreValue;
        }
        
        public String getLastCollectedItemName() {
            return lastCollectedItemName;
        }
        
        public int getLastCollectedItemValue() {
            return lastCollectedItemValue;
        }
        
        @Override
        public void onPlayerDamaged(String source) {}
        
        @Override
        public void triggerGameOver(project276.group4.domain.types.GameOverReason reason) {}
        
        @Override
        public void triggerVictory() {}
        
        @Override
        public void unlockExit() {}
        
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