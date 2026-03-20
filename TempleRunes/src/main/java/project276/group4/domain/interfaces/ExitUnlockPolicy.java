package project276.group4.domain.interfaces;

/**
 * Interface for determining when the exit should be unlocked.
 * Implementations define the conditions required to unlock the exit (e.g., collect all runes).
 */
public interface ExitUnlockPolicy {
    
    /**
     * Checks if the exit should be unlocked based on current game state.
     * 
     * @param runesCollected the number of runes collected
     * @param totalRunes the total number of runes in the level
     * @return true if the exit should unlock, false otherwise
     */
    boolean shouldUnlockExit(int runesCollected, int totalRunes);
    
    /**
     * Gets the number of runes still needed to unlock the exit.
     * 
     * @param runesCollected the number of runes collected
     * @param totalRunes the total number of runes in the level
     * @return the number of runes remaining
     */
    default int getRunesRemaining(int runesCollected, int totalRunes) {
        return Math.max(0, totalRunes - runesCollected);
    }
}

