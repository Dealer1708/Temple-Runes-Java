package project276.group4.ui.Screens;

import project276.group4.domain.GameSettings;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.DifficultyLevel;
import project276.group4.domain.types.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles spawning logic for game entities, including difficulty-based counts
 * and position collision avoidance to prevent overlapping spawns.
 */
public class EntitySpawner {
    
    private final GameMap map;
    private final Position playerSpawn;
    
    /**
     * Creates an EntitySpawner with the given game map and player spawn position.
     *
     * @param map the game map
     * @param playerSpawn the player spawn position to avoid when placing entities
     */
    public EntitySpawner(GameMap map, Position playerSpawn) {
        this.map = map;
        this.playerSpawn = playerSpawn;
    }
    
    /**
     * Spawns all game entities based on difficulty level, ensuring no overlaps.
     * Returns a SpawnResult containing all spawn positions.
     *
     * @return SpawnResult with all entity positions
     */
    public SpawnResult spawnAllEntities() {
        SpawnResult result = new SpawnResult();
        
        // Spawn obstacles first
        int cursedIdolCount = getCursedIdolCountForDifficulty();
        result.cursedIdolPositions = map.findRandomFreePositions(cursedIdolCount, playerSpawn, 5);
        
        int trapCount = getTrapCountForDifficulty();
        result.trapPositions = map.findRandomFreePositions(trapCount, playerSpawn, 5);
        
        // Build obstacle list to avoid when spawning collectibles
        List<Position> obstaclePositions = new ArrayList<>();
        obstaclePositions.addAll(result.cursedIdolPositions);
        obstaclePositions.addAll(result.trapPositions);
        
        // Spawn collectibles, avoiding obstacles and other collectibles
        int runeCount = getRuneCountForDifficulty();
        result.runePositions = findSafePositions(runeCount, 4, obstaclePositions);
        obstaclePositions.addAll(result.runePositions);
        
        result.coinPositions = findSafePositions(2, 4, obstaclePositions);
        obstaclePositions.addAll(result.coinPositions);
        
        result.artifactPositions = findSafePositions(1, 4, obstaclePositions);
        
        // Spawn guardians near runes
        int guardianCount = getGuardianCountForDifficulty();
        result.guardianPositions = map.findPositionsNearTargets(result.runePositions, guardianCount, playerSpawn, 6);
        
        return result;
    }
    
    /**
     * Finds random free positions that avoid specified obstacle positions.
     *
     * @param count number of positions to find
     * @param minDistance minimum distance from player spawn
     * @param obstacles list of positions to avoid (e.g., traps, cursed idols, other collectibles)
     * @return list of safe positions
     */
    private List<Position> findSafePositions(int count, int minDistance, List<Position> obstacles) {
        List<Position> safePositions = new ArrayList<>();
        int maxAttempts = 100;
        int attempts = 0;
        
        // Use sets for O(1) containment checks to avoid nested loops
        Set<Position> obstacleSet = new HashSet<>(obstacles);
        Set<Position> placedSet = new HashSet<>();

        while (safePositions.size() < count && attempts < maxAttempts) {
            List<Position> candidates = map.findRandomFreePositions(count - safePositions.size(), playerSpawn, minDistance);

            for (Position candidate : candidates) {
                // Skip if collides with known obstacles or already placed positions
                if (obstacleSet.contains(candidate) || placedSet.contains(candidate)) {
                    continue;
                }

                safePositions.add(candidate);
                placedSet.add(candidate);
            }

            attempts++;
        }

        return safePositions;
    }
    
    /**
     * Determines the number of runes based on current difficulty level.
     *
     * @return number of runes to spawn
     */
    public static int getRuneCountForDifficulty() {
        DifficultyLevel difficulty = GameSettings.getCurrentDifficulty();
        switch (difficulty) {
            case EASY:
                return 5;
            case HARD:
                return 10;
            case NORMAL:
            default:
                return 7;
        }
    }
    
    /**
     * Determines the number of guardians based on current difficulty level.
     *
     * @return number of guardians to spawn
     */
    private int getGuardianCountForDifficulty() {
        DifficultyLevel difficulty = GameSettings.getCurrentDifficulty();
        switch (difficulty) {
            case EASY:
                return 1;
            case HARD:
                return 3;
            case NORMAL:
            default:
                return 2;
        }
    }
    
    /**
     * Determines the number of traps based on current difficulty level.
     *
     * @return number of traps to spawn
     */
    private int getTrapCountForDifficulty() {
        DifficultyLevel difficulty = GameSettings.getCurrentDifficulty();
        switch (difficulty) {
            case EASY:
                return 1;
            case HARD:
                return 4;
            case NORMAL:
            default:
                return 2;
        }
    }
    
    /**
     * Determines the number of cursed idols based on current difficulty level.
     *
     * @return number of cursed idols to spawn
     */
    private int getCursedIdolCountForDifficulty() {
        DifficultyLevel difficulty = GameSettings.getCurrentDifficulty();
        switch (difficulty) {
            case EASY:
                return 1;
            case HARD:
                return 3;
            case NORMAL:
            default:
                return 2;
        }
    }
    
    /**
     * Result object containing all spawn positions.
     */
    public static class SpawnResult {
        public List<Position> cursedIdolPositions = new ArrayList<>();
        public List<Position> trapPositions = new ArrayList<>();
        public List<Position> runePositions = new ArrayList<>();
        public List<Position> coinPositions = new ArrayList<>();
        public List<Position> artifactPositions = new ArrayList<>();
        public List<Position> guardianPositions = new ArrayList<>();
    }
}
