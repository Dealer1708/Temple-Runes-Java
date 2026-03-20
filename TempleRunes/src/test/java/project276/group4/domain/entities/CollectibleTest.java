package project276.group4.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.types.ItemType;
import project276.group4.domain.types.Position;
import project276.group4.testutil.TestDoubles.RecordingGameSignals;
import project276.group4.testutil.TestDoubles.RecordingScoreManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Collectible} behavior.
 */
public class CollectibleTest {

    private RecordingScoreManager scoreManager;
    private RecordingGameSignals signals;
    private Position defaultPosition;
    private Collectible runeCollectible;

    @BeforeEach
    void setUp() {
        scoreManager = new RecordingScoreManager();
        signals = new RecordingGameSignals();
        defaultPosition = new Position(1, 1);
        runeCollectible = new Collectible(defaultPosition, ItemType.RUNE, 100, "Ancient Rune");
    }

    @Test
    void constructor_PreservesMetadata() {
        Collectible artifact = new Collectible(defaultPosition, ItemType.ARTIFACT, 250, "Relic");

        assertEquals(ItemType.ARTIFACT, artifact.getType());
        assertEquals(250, artifact.getScoreValue());
        assertEquals("Relic", artifact.getName());
        assertEquals(defaultPosition, artifact.getPosition());
        assertFalse(artifact.isCollected());
        assertTrue(artifact.isActive());
    }

    @Test
    void onPickup_AddsScoreAndMarksCollected() {
        runeCollectible.onPickup(scoreManager, signals);

        assertTrue(runeCollectible.isCollected());
        assertFalse(runeCollectible.isActive());
        assertEquals(100, scoreManager.getCurrentScore());
        assertEquals("Ancient Rune", signals.getCollectedItems().get(0));
    }

    @Test
    void onPickup_AlreadyCollected_IsIgnored() {
        runeCollectible.onPickup(scoreManager, signals);

        scoreManager.resetScore();
        runeCollectible.onPickup(scoreManager, signals);

        assertEquals(0, scoreManager.getCurrentScore());
        assertEquals(1, signals.getCollectedItems().size());
    }

    @Test
    void onPickup_NullDependencies_SafelyIgnored() {
        Collectible collectible = new Collectible(new Position(0, 0), ItemType.COIN, 25, "Coin");

        collectible.onPickup(null, null);

        assertFalse(collectible.isCollected());
        assertTrue(collectible.isActive());
    }

    @Test
    void onPickup_ZeroScoreStillCollects() {
        Collectible key = new Collectible(defaultPosition, ItemType.KEY, 0, "Key");

        key.onPickup(scoreManager, signals);

        assertEquals(0, scoreManager.getCurrentScore());
        assertEquals("Key", signals.getCollectedItems().get(0));
        assertTrue(key.isCollected());
    }

    @Test
    void onPickup_RecordsSignalDetails() {
        Collectible artifact = new Collectible(defaultPosition, ItemType.ARTIFACT, 200, "Relic");

        artifact.onPickup(scoreManager, signals);

        assertEquals("Relic", signals.getCollectedItems().get(0));
        assertEquals(Integer.valueOf(200), signals.getCollectedScores().get(0));
    }

    @Test
    void toString_IncludesTypeAndName() {
        String representation = runeCollectible.toString();

        assertTrue(representation.contains("Ancient Rune"));
        assertTrue(representation.contains("RUNE"));
        assertTrue(representation.contains(defaultPosition.toString()));
    }
}

