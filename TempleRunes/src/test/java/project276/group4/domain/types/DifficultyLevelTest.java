package project276.group4.domain.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for DifficultyLevel enum.
 * Tests enum values, display names, and multiplier values.
 */
class DifficultyLevelTest {

    @Test
    void testEasyDifficultyDisplayName() {
        assertEquals("Easy", DifficultyLevel.EASY.getDisplayName(),
                "Easy difficulty should have display name 'Easy'");
    }

    @Test
    void testNormalDifficultyDisplayName() {
        assertEquals("Normal", DifficultyLevel.NORMAL.getDisplayName(),
                "Normal difficulty should have display name 'Normal'");
    }

    @Test
    void testHardDifficultyDisplayName() {
        assertEquals("Hard", DifficultyLevel.HARD.getDisplayName(),
                "Hard difficulty should have display name 'Hard'");
    }

    @Test
    void testEasySpeedMultiplier() {
        assertEquals(0.7, DifficultyLevel.EASY.getSpeedMultiplier(),
                "Easy speed multiplier should be 0.7");
    }

    @Test
    void testNormalSpeedMultiplier() {
        assertEquals(1.0, DifficultyLevel.NORMAL.getSpeedMultiplier(),
                "Normal speed multiplier should be 1.0");
    }

    @Test
    void testHardSpeedMultiplier() {
        assertEquals(1.5, DifficultyLevel.HARD.getSpeedMultiplier(),
                "Hard speed multiplier should be 1.5");
    }

    @Test
    void testEasyVisionMultiplier() {
        assertEquals(0.8, DifficultyLevel.EASY.getVisionMultiplier(),
                "Easy vision multiplier should be 0.8");
    }

    @Test
    void testNormalVisionMultiplier() {
        assertEquals(1.0, DifficultyLevel.NORMAL.getVisionMultiplier(),
                "Normal vision multiplier should be 1.0");
    }

    @Test
    void testHardVisionMultiplier() {
        assertEquals(1.3, DifficultyLevel.HARD.getVisionMultiplier(),
                "Hard vision multiplier should be 1.3");
    }

    @Test
    void testAllDifficultiesExist() {
        // Ensure all three difficulty levels are accessible
        assertNotNull(DifficultyLevel.EASY);
        assertNotNull(DifficultyLevel.NORMAL);
        assertNotNull(DifficultyLevel.HARD);
    }

    @Test
    void testDifficultyEnum() {
        // Test enum values array
        DifficultyLevel[] difficulties = DifficultyLevel.values();
        assertEquals(3, difficulties.length, "Should have exactly 3 difficulty levels");
    }

    @Test
    void testDifficultyEnumValueOf() {
        // Test valueOf method
        assertEquals(DifficultyLevel.EASY, DifficultyLevel.valueOf("EASY"));
        assertEquals(DifficultyLevel.NORMAL, DifficultyLevel.valueOf("NORMAL"));
        assertEquals(DifficultyLevel.HARD, DifficultyLevel.valueOf("HARD"));
    }

    @Test
    void testSpeedMultiplierOrdering() {
        // Easy < Normal < Hard
        assertTrue(DifficultyLevel.EASY.getSpeedMultiplier() < DifficultyLevel.NORMAL.getSpeedMultiplier(),
                "Easy speed should be less than Normal");
        assertTrue(DifficultyLevel.NORMAL.getSpeedMultiplier() < DifficultyLevel.HARD.getSpeedMultiplier(),
                "Normal speed should be less than Hard");
    }

    @Test
    void testVisionMultiplierOrdering() {
        // Easy < Normal < Hard
        assertTrue(DifficultyLevel.EASY.getVisionMultiplier() < DifficultyLevel.NORMAL.getVisionMultiplier(),
                "Easy vision should be less than Normal");
        assertTrue(DifficultyLevel.NORMAL.getVisionMultiplier() < DifficultyLevel.HARD.getVisionMultiplier(),
                "Normal vision should be less than Hard");
    }

    @Test
    void testNormalIsBaselineDifficulty() {
        // Normal should be the default/baseline with 1.0 multipliers
        assertEquals(1.0, DifficultyLevel.NORMAL.getSpeedMultiplier());
        assertEquals(1.0, DifficultyLevel.NORMAL.getVisionMultiplier());
    }

    @Test
    void testEasyIsEasierThanNormal() {
        // All multipliers for Easy should be less than or equal to Normal
        assertTrue(DifficultyLevel.EASY.getSpeedMultiplier() <= DifficultyLevel.NORMAL.getSpeedMultiplier(),
                "Easy speed should be <= Normal");
        assertTrue(DifficultyLevel.EASY.getVisionMultiplier() <= DifficultyLevel.NORMAL.getVisionMultiplier(),
                "Easy vision should be <= Normal");
    }

    @Test
    void testHardIsHarderThanNormal() {
        // All multipliers for Hard should be greater than or equal to Normal
        assertTrue(DifficultyLevel.HARD.getSpeedMultiplier() >= DifficultyLevel.NORMAL.getSpeedMultiplier(),
                "Hard speed should be >= Normal");
        assertTrue(DifficultyLevel.HARD.getVisionMultiplier() >= DifficultyLevel.NORMAL.getVisionMultiplier(),
                "Hard vision should be >= Normal");
    }
}
