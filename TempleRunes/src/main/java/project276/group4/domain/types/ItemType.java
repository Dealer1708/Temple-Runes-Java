package project276.group4.domain.types;

/**
 * Enumeration of collectible item types in the game.
 * Each type has different effects when collected.
 */
public enum ItemType {
    /** Rune - required to unlock the exit */
    RUNE,
    
    /** Artifact - provides bonus score */
    ARTIFACT,
    
    /** Coin - provides small score bonus */
    COIN,
    
    /** Key - unlocks doors */
    KEY,
    
    /** Health potion - restores player health */
    HEALTH_POTION
}

