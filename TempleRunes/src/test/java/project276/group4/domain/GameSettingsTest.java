package project276.group4.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.types.DifficultyLevel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for GameSettings class.
 * Tests difficulty level management and multiplier retrieval.
 */
class GameSettingsTest {

    @BeforeEach
    void setUp() {
        // Reset to default difficulty before each test
        GameSettings.setCurrentDifficulty(DifficultyLevel.NORMAL);
    }

    @Test
    void testDefaultDifficultyIsNormal() {
        // Reset to ensure default state
        GameSettings.setCurrentDifficulty(DifficultyLevel.NORMAL);
        assertEquals(DifficultyLevel.NORMAL, GameSettings.getCurrentDifficulty(),
                "Default difficulty should be NORMAL");
    }

    @Test
    void testSetCurrentDifficultyToEasy() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.EASY);
        assertEquals(DifficultyLevel.EASY, GameSettings.getCurrentDifficulty(),
                "Difficulty should be set to EASY");
    }

    @Test
    void testSetCurrentDifficultyToHard() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.HARD);
        assertEquals(DifficultyLevel.HARD, GameSettings.getCurrentDifficulty(),
                "Difficulty should be set to HARD");
    }

    @Test
    void testSetCurrentDifficultyMultipleTimes() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.EASY);
        assertEquals(DifficultyLevel.EASY, GameSettings.getCurrentDifficulty());

        GameSettings.setCurrentDifficulty(DifficultyLevel.HARD);
        assertEquals(DifficultyLevel.HARD, GameSettings.getCurrentDifficulty());

        GameSettings.setCurrentDifficulty(DifficultyLevel.NORMAL);
        assertEquals(DifficultyLevel.NORMAL, GameSettings.getCurrentDifficulty());
    }

    @Test
    void testGetSpeedMultiplierForEasy() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.EASY);
        assertEquals(0.7, GameSettings.getSpeedMultiplier(),
                "Easy mode should have 0.7 speed multiplier");
    }

    @Test
    void testGetSpeedMultiplierForNormal() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.NORMAL);
        assertEquals(1.0, GameSettings.getSpeedMultiplier(),
                "Normal mode should have 1.0 speed multiplier");
    }

    @Test
    void testGetSpeedMultiplierForHard() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.HARD);
        assertEquals(1.5, GameSettings.getSpeedMultiplier(),
                "Hard mode should have 1.5 speed multiplier");
    }

    @Test
    void testGetVisionMultiplierForEasy() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.EASY);
        assertEquals(0.8, GameSettings.getVisionMultiplier(),
                "Easy mode should have 0.8 vision multiplier");
    }

    @Test
    void testGetVisionMultiplierForNormal() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.NORMAL);
        assertEquals(1.0, GameSettings.getVisionMultiplier(),
                "Normal mode should have 1.0 vision multiplier");
    }

    @Test
    void testGetVisionMultiplierForHard() {
        GameSettings.setCurrentDifficulty(DifficultyLevel.HARD);
        assertEquals(1.3, GameSettings.getVisionMultiplier(),
                "Hard mode should have 1.3 vision multiplier");
    }

    @Test
    void testMultipliersChangeWithDifficulty() {
        // Easy
        GameSettings.setCurrentDifficulty(DifficultyLevel.EASY);
        double easySpeed = GameSettings.getSpeedMultiplier();
        double easyVision = GameSettings.getVisionMultiplier();

        // Normal
        GameSettings.setCurrentDifficulty(DifficultyLevel.NORMAL);
        double normalSpeed = GameSettings.getSpeedMultiplier();
        double normalVision = GameSettings.getVisionMultiplier();

        // Hard
        GameSettings.setCurrentDifficulty(DifficultyLevel.HARD);
        double hardSpeed = GameSettings.getSpeedMultiplier();
        double hardVision = GameSettings.getVisionMultiplier();

        // Verify progression
        assertTrue(easySpeed < normalSpeed && normalSpeed < hardSpeed,
                "Speed multiplier should increase: easy < normal < hard");
        assertTrue(easyVision < normalVision && normalVision < hardVision,
                "Vision multiplier should increase: easy < normal < hard");
    }
}
