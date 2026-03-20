package project276.group4;

import project276.group4.domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultScoreManagerTest {

    private DefaultScoreManager scoreManager;

    @BeforeEach
    public void setUp() {
        scoreManager = new DefaultScoreManager(-10); // Game over threshold at -10
    }

    @Test
    public void testInitialScoreIsZero() {
        assertEquals(0, scoreManager.getCurrentScore());
    }

    @Test
    public void testAddScoreIncreasesScore() {
        scoreManager.addScore(5);
        assertEquals(5, scoreManager.getCurrentScore());
        
        scoreManager.addScore(10);
        assertEquals(15, scoreManager.getCurrentScore());
    }

    @Test
    public void testDeductScoreDecreasesScore() {
        scoreManager.addScore(20); // Start with some points
        scoreManager.deductScore(5);
        assertEquals(15, scoreManager.getCurrentScore());
        
        scoreManager.deductScore(10);
        assertEquals(5, scoreManager.getCurrentScore());
    }

    @Test
    public void testAddScoreWithNegativePointsThrowsException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> scoreManager.addScore(-5)
        );
        assertEquals("Points to add must be non-negative", exception.getMessage());
    }

    @Test
    public void testDeductScoreWithNegativePointsThrowsException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> scoreManager.deductScore(-3)
        );
        assertEquals("Points to deduct must be non-negative", exception.getMessage());
    }

    @Test
    public void testResetScoreSetsToZero() {
        scoreManager.addScore(25);
        assertEquals(25, scoreManager.getCurrentScore());
        
        scoreManager.resetScore();
        assertEquals(0, scoreManager.getCurrentScore());
    }

    @Test
    public void testIsScoreBelowThreshold() {
        // test when score is above threshold
        scoreManager.addScore(5);
        assertFalse(scoreManager.isScoreBelowThreshold());
        
        // test when score is at threshold
        scoreManager.resetScore();
        assertFalse(scoreManager.isScoreBelowThreshold()); // 0 > -10
        
        // test when score is below threshold
        scoreManager.deductScore(15); // 0 - 15 = -15
        assertTrue(scoreManager.isScoreBelowThreshold()); // -15 < -10
    }

    @Test
    public void testScoreCanGoNegative() {
        scoreManager.deductScore(5);
        assertEquals(-5, scoreManager.getCurrentScore());
        
        scoreManager.deductScore(10);
        assertEquals(-15, scoreManager.getCurrentScore());
    }

    @Test
    public void testMultipleOperations() {
        scoreManager.addScore(10);  // 10
        scoreManager.deductScore(3); // 7
        scoreManager.addScore(5);   // 12
        scoreManager.deductScore(8); // 4
        scoreManager.resetScore(); // 0
        scoreManager.addScore(2); // 2
        
        assertEquals(2, scoreManager.getCurrentScore());
        assertFalse(scoreManager.isScoreBelowThreshold());
    }
}