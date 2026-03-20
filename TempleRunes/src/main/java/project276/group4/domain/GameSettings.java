package project276.group4.domain;

import project276.group4.domain.types.DifficultyLevel;

/**
 * Provides global game settings that can be accessed anywhere in the program.
 * Currently stores the selected {@link DifficultyLevel} and exposes helper
 * methods to obtain gameplay multipliers based on that difficulty.
 */
public class GameSettings {
    private static DifficultyLevel currentDifficulty = DifficultyLevel.NORMAL;

     /**
     * Returns the currently active difficulty level.
     *
     * @return the current {@link DifficultyLevel}
     */
    public static DifficultyLevel getCurrentDifficulty() {
        return currentDifficulty;
    }

    /**
     * Updates the current difficulty level for the game.
     *
     * @param difficulty the new {@link DifficultyLevel} to set
     */
    public static void setCurrentDifficulty(DifficultyLevel difficulty) {
        currentDifficulty = difficulty;
    }

    /**
     * Returns the speed multiplier associated with the current difficulty.
     *
     * @return the speed multiplier value
     */
    public static double getSpeedMultiplier() { return currentDifficulty.getSpeedMultiplier(); }
    
    /**
     * Returns the vision multiplier associated with the current difficulty.
     *
     * @return the vision multiplier value
     */
    public static double getVisionMultiplier() { return currentDifficulty.getVisionMultiplier(); }
}
