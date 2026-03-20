package project276.group4.domain;

import project276.group4.domain.types.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for saving and loading game state.
 * Contains all necessary information to restore a game session.
 */
public class GameSaveData {
    
    /** The player's X-coordinate on the map grid. */
    public int playerX;

    /** The player's Y-coordinate on the map grid. */
    public int playerY;
    
    /** The player's current score at the time of saving. */
    public int currentScore;

    /** Total elapsed play time in seconds. */
    public long elapsedTimeSeconds;
    
    /** Difficulty setting stored as the enum name (e.g., "EASY", "NORMAL", "HARD"). */
    public String difficulty; // DifficultyLevel enum name
    
    /** List of all collectible items and their saved states. */
    public List<CollectibleData> collectibles = new ArrayList<>();
    
    /** List of all guardians and their saved states. */
    public List<GuardianData> guardians = new ArrayList<>();
    
    /** List of all traps and their saved states. */
    public List<TrapData> traps = new ArrayList<>();
    
    /** List of all cursed idols and their saved states. */
    public List<CursedIdolData> cursedIdols = new ArrayList<>();
    
    /** Width of the map to validate load compatibility. */
    public int mapWidth;

    /** Height of the map to validate load compatibility. */
    public int mapHeight;
    
    /**
     * Empty constructor for Jackson deserialization.
     */
    public GameSaveData() {}
    
    /**
     * Nested class to store collectible state.
     */
    public static class CollectibleData {
        public int x;
        public int y;
        public String type; // RUNE, COIN, ARTIFACT
        public int value;
        public boolean collected;
        
        /**
         * Represents the serialized state of a collectible item.
         */
        public CollectibleData() {}
        

        /**
         * Constructs a new collectible save record.
         *
         * @param x X-coordinate
         * @param y Y-coordinate
         * @param type type of collectible
         * @param value score value of the collectible
         * @param collected true if already collected
         */
        public CollectibleData(int x, int y, String type, int value, boolean collected) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.value = value;
            this.collected = collected;
        }
    }
    
    /**
     * Nested class to store guardian state.
     */
    public static class GuardianData {
        public int x;
        public int y;
        public double speed;
        public int visionRange;
        
        public GuardianData() {}
        
        public GuardianData(int x, int y, double speed, int visionRange) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.visionRange = visionRange;
        }
    }
    
    /**
     * Nested class to store trap state.
     */
    public static class TrapData {
        public int x;
        public int y;
        public boolean revealed;
        
        public TrapData() {}
        
        public TrapData(int x, int y, boolean revealed) {
            this.x = x;
            this.y = y;
            this.revealed = revealed;
        }
    }
    
    /**
     * Nested class to store cursed idol state.
     */
    public static class CursedIdolData {
        public int x;
        public int y;
        public boolean activated;
        
        public CursedIdolData() {}
        
        public CursedIdolData(int x, int y, boolean activated) {
            this.x = x;
            this.y = y;
            this.activated = activated;
        }
    }
}
