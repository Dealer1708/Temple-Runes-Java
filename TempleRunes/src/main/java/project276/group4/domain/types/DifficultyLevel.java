package project276.group4.domain.types;

/**
 * Difficulty levels with display names and gameplay multipliers.
 */
public enum DifficultyLevel {
    EASY("Easy", 0.7, 0.8),
    NORMAL("Normal", 1.0, 1.0),
    HARD("Hard", 1.5, 1.3);

    private final String displayName;
    private final double speedMultiplier;
    private final double visionMultiplier;

    /**
     * Creates a difficulty level with corresponding display name and multipliers.
     *
     * @param displayName the user-friendly name shown in the UI
     * @param speedMultiplier how much player speed is scaled at this difficulty
     * @param visionMultiplier how much player vision is scaled at this difficulty
     */
    DifficultyLevel(String displayName, double speedMultiplier, double visionMultiplier) {
        this.displayName = displayName;
        this.speedMultiplier = speedMultiplier;
        this.visionMultiplier = visionMultiplier;
    }

    /**
     * Returns the display name of this difficulty level.
     *
     * @return the readable difficulty name (e.g., "Easy", "Normal", "Hard")
     */
    public String getDisplayName() { return displayName; }

    /**
     * Returns the speed multiplier applied at this difficulty.
     *
     * @return the speed multiplier value
     */
    public double getSpeedMultiplier() { return speedMultiplier; }
   
    /**
     * Returns the vision multiplier applied at this difficulty.
     *
     * @return the vision multiplier value
     */
    public double getVisionMultiplier() { return visionMultiplier; }
}
