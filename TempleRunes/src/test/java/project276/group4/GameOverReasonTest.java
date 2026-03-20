package project276.group4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import project276.group4.domain.types.GameOverReason;

/**
 * Tests for GameOverReason enum - game ending messages.
 */
public class GameOverReasonTest {

    @Test
    public void testGameOverMessages() {
        assertEquals("A hidden trap snaps shut — your journey ends here.", 
                    GameOverReason.TRAP_TRIGGERED.getMessage());
        
        assertEquals("A guardian lunges from the shadows and claims you.", 
                    GameOverReason.GUARDIAN_CONTACT.getMessage());
        
        assertEquals("The cursed idol drains your final strength… you collapse.", 
                    GameOverReason.NEGATIVE_SCORE.getMessage());
        
        assertEquals("Your adventure fades into darkness...", 
                    GameOverReason.PLAYER_DEATH.getMessage());
    }

    @Test
    public void testGameOverReasonCount() {
        GameOverReason[] reasons = GameOverReason.values();
        assertEquals(5, reasons.length);
    }

    @Test
    public void testValueOfMethod() {
        assertEquals(GameOverReason.TRAP_TRIGGERED, GameOverReason.valueOf("TRAP_TRIGGERED"));
        assertEquals(GameOverReason.OUT_OF_TIME, GameOverReason.valueOf("OUT_OF_TIME"));
    }
}