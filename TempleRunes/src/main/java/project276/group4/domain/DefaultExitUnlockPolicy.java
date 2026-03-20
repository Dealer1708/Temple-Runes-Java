
package project276.group4.domain;

import project276.group4.domain.interfaces.ExitUnlockPolicy;

/**
 * Standard exit-unlock policy:
 * The exit unlocks only when plauer collects all the runes
 */
public class DefaultExitUnlockPolicy implements ExitUnlockPolicy {

    /**
     * Checks if the exit should be unlocked based on current game state.
     * 
     * @param runesCollected the number of runes collected
     * @param totalRunes the total number of runes in the level
     * @return true if the exit should unlock, false otherwise
     */
    @Override
    public boolean shouldUnlockExit(int runesCollected, int totalRunes) {
        if (totalRunes < 0 || runesCollected < 0) {
            throw new IllegalArgumentException("Rune counts cannot be negative.");
        }
        if (runesCollected > totalRunes) {
            throw new IllegalArgumentException("Collected runes cannot exceed total runes.");
        }
        
        return runesCollected >= totalRunes;
    }
}
