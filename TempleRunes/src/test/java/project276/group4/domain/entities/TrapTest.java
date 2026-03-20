package project276.group4.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;
import project276.group4.testutil.TestDoubles.RecordingGameSignals;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Trap} behavior.
 */
public class TrapTest {

    private Trap trap;
    private Player player;
    private RecordingGameSignals signals;

    @BeforeEach
    void setUp() {
        trap = new Trap(new Position(0, 0));
        player = new Player(new Position(0, 0));
        signals = new RecordingGameSignals();
    }

    @Test
    void onTrigger_KillsPlayerAndSignalsGameOver() {
        trap.onTrigger(player, signals);

        assertTrue(trap.isTriggered());
        assertTrue(trap.isRevealed());
        assertFalse(trap.isActive());
        assertFalse(player.isAlive());
        assertEquals(GameOverReason.TRAP_TRIGGERED, signals.getLastGameOverReason());
    }

    @Test
    void onTrigger_AlreadyTriggered_NoAdditionalEffect() {
        trap.onTrigger(player, signals);
        player = new Player(new Position(0, 0)); // fresh player

        trap.onTrigger(player, signals);

        assertTrue(player.isAlive());
    }

    @Test
    void reveal_SetsVisibleFlag() {
        assertFalse(trap.isRevealed());

        trap.reveal();

        assertTrue(trap.isRevealed());
    }
}

