package project276.group4.domain.types;

/**
 * Enumeration of reasons why a game can end in failure.
 * Used to provide specific feedback to the player about how they lost.
 */
public enum GameOverReason {
    /** Player stepped on a hidden trap */
    TRAP_TRIGGERED("A hidden trap snaps shut — your journey ends here."),
    
    /** Player was caught by a moving guardian */
    GUARDIAN_CONTACT("A guardian lunges from the shadows and claims you."),
    
    /** Player's score fell below zero due to cursed idols */
    NEGATIVE_SCORE("The cursed idol drains your final strength… you collapse."),
    
    /** Player ran out of time */
    OUT_OF_TIME("The sands of time run out, and your quest is lost to shadow."),

    /** Generic death reason */
    PLAYER_DEATH("Your adventure fades into darkness...");

    private final String message;

    /**
     * Creates a game over reason with the specified message.
     * 
     * @param message the message to display to the player
     */
    GameOverReason(String message) {
        this.message = message;
    }

    /**
     * Gets the message associated with this game over reason.
     * 
     * @return the display message
     */
    public String getMessage() {
        return message;
    }
}

