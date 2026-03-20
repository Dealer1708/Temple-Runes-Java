package project276.group4.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;
import project276.group4.testutil.TestDoubles.RecordingGameSignals;
import project276.group4.testutil.TestDoubles.RecordingScoreManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CursedIdol}.
 */
public class CursedIdolTest {

    private RecordingScoreManager scoreManager;
    private RecordingGameSignals signals;
    private CursedIdol idol;

    @BeforeEach
    void setUp() {
        scoreManager = new RecordingScoreManager(200);
        signals = new RecordingGameSignals();
        idol = new CursedIdol(new Position(1, 1), 75);
    }

    @Test
    void onTrigger_DeductsScoreAndMarksTouched() {
        idol.onTrigger(scoreManager, signals);

        assertTrue(idol.isTouched());
        assertFalse(idol.isActive());
        assertEquals(125, scoreManager.getCurrentScore());
        assertEquals("CursedIdol", signals.getDamageSources().get(0));
    }

    @Test
    void onTrigger_ScoreDropsBelowThreshold_TriggersGameOver() {
        scoreManager = new RecordingScoreManager(20);
        scoreManager.setThreshold(0);
        idol = new CursedIdol(new Position(0, 0), 50);

        idol.onTrigger(scoreManager, signals);

        assertEquals(GameOverReason.NEGATIVE_SCORE, signals.getLastGameOverReason());
    }

    @Test
    void onTrigger_NullDependencies_IsIgnored() {
        idol.onTrigger(null, null);

        assertFalse(idol.isTouched());
        assertTrue(idol.isActive());
    }
}

