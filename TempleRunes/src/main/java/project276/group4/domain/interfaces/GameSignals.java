package project276.group4.domain.interfaces;

import project276.group4.domain.types.GameOverReason;

/**
 * Interface for signaling important game events from the domain layer to the controller/UI layer.
 * This allows the core game logic to remain decoupled from presentation concerns.
 */
public interface GameSignals {
    
    /**
     * Signals that the game is over due to the specified reason.
     * 
     * @param reason the reason for game over (death, negative score, etc.)
     */
    void triggerGameOver(GameOverReason reason);
    
    /**
     * Signals that the player has achieved victory conditions.
     * This typically occurs when all runes are collected and the exit is reached.
     */
    void triggerVictory();
    
    /**
     * Signals that the exit door should be unlocked.
     * This usually happens when all required runes have been collected.
     */
    void unlockExit();
    
    /**
     * Signals that a collectible item has been picked up.
     * 
     * @param itemName the name or type of the item collected
     * @param scoreValue the score value of the item
     */
    void onItemCollected(String itemName, int scoreValue);
    
    /**
     * Signals that the player has taken damage or triggered a trap.
     * 
     * @param damageSource the source of the damage (e.g., "MovingGuardian", "Trap")
     */
    void onPlayerDamaged(String damageSource);
    
    /**
     * Signals that a rune has been collected.
     * This is called separately to track rune collection progress.
     */
    void onCollectRune();

    /**
     * Signals that player has stepped on or triggered a trap.
     */
    void onTrapSteppedOn();


    /**
     * Signals that player touched the cursed idol.
     */
    void onCursedIdolTouched();
    
    /**
     * Sets the rune collection totals.
     * 
     * @param total the total number of runes in the level
     * @param collected the number of runes currently collected
     */
    void setRuneTotals(int total, int collected);
}

