package project276.group4.domain.entities;

import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;

/**
 * Represents a hidden trap in the game.
 * When triggered by the player, the trap causes instant death.
 */
public class Trap extends Entity {
    private boolean triggered;
    private boolean revealed;

    /**
     * Creates a new trap at the specified position.
     * Traps start hidden and untriggered.
     * 
     * @param position the position of the trap
     */
    public Trap(Position position) {
        super(position);
        this.triggered = false;
        this.revealed = false;
    }

    /**
     * Checks if the trap has been triggered.
     * 
     * @return true if triggered, false otherwise
     */
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * Checks if the trap is revealed (visible to the player).
     * 
     * @return true if revealed, false otherwise
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Reveals the trap, making it visible to the player.
     * This could happen through special items or abilities.
     */
    public void reveal() {
        this.revealed = true;
    }

    /**
     * Triggers the trap, causing instant player death.
     * Once triggered, the trap becomes inactive.
     * 
     * @param player the player who stepped on the trap
     * @param signals the game signals to notify of death
     */
    public void onTrigger(Player player, GameSignals signals) {
        if (triggered || !active) {
            return;
        }

        triggered = true;
        revealed = true;
        active = false;

        // Kill the player instantly
        player.kill();

        // Signal game over
        signals.triggerGameOver(GameOverReason.TRAP_TRIGGERED);
    }

    @Override
    public String toString() {
        return "Trap{" +
                "pos=" + position +
                ", triggered=" + triggered +
                ", revealed=" + revealed +
                '}';
    }
}

