package project276.group4.domain;

/**
 * Central repository for all game-wide constants.
 * This class eliminates magic numbers and provides a single source of truth
 * for configuration values used across multiple classes.
 */
public final class GameConstants {
    
    // Prevent instantiation
    private GameConstants() {
        throw new AssertionError("GameConstants should not be instantiated");
    }
    
    // ========== Window Dimensions ==========
    /**
     * Default window width in pixels
     */
    public static final int WINDOW_WIDTH = 1920;
    
    /**
     * Default window height in pixels
     */
    public static final int WINDOW_HEIGHT = 1080;
    
    // ========== Game Grid ==========
    /**
     * Size of each tile in pixels (both width and height)
     */
    public static final int TILE_SIZE = 32;
    
    // ========== Guardian Defaults ==========
    /** Default guardian vision (in tiles) */
    public static final int GUARDIAN_DEFAULT_VISION = 8;

    /** Default guardian movement speed (tiles per move or movement factor) */
    public static final double GUARDIAN_DEFAULT_SPEED = 0.3;

    /** Default guardian damage inflicted on player contact */
    public static final int GUARDIAN_DEFAULT_DAMAGE = 100;

    /** Default guardian move delay in frames */
    public static final int GUARDIAN_MOVE_DELAY_FRAMES = 25;
}
