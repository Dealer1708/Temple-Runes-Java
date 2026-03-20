package project276.group4.domain.map;

/**
 * Enumeration of all possible cell types in the game map.
 * Each cell type has different gameplay implications.
 */
public enum CellType {
    /** Empty walkable floor tile */
    FLOOR,
    
    /** Solid wall that blocks movement */
    WALL,
    
    /** Door that can be locked or unlocked */
    DOOR,
    
    /** Locked exit door - requires all runes to unlock */
    EXIT,
    
    /** Starting position for the player */
    PLAYER_SPAWN,
    
    /** Starting position for an enemy */
    ENEMY_SPAWN,
    
    /** Hidden trap location (visually appears as floor) */
    TRAP_HIDDEN,
    
    /** Revealed trap location */
    TRAP_REVEALED
}

