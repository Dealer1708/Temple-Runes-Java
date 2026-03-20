package project276.group4.domain.entities;

import org.junit.jupiter.api.Test;
import project276.group4.domain.types.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the shared {@link Entity} behaviors.
 */
public class EntityTest {

    @Test
    void constructor_NullPosition_Throws() {
        assertThrows(IllegalArgumentException.class, () -> new TestEntity(null));
    }

    @Test
    void constructor_ValidPosition_SetsActive() {
        Position pos = new Position(1, 2);
        TestEntity entity = new TestEntity(pos);

        assertEquals(pos, entity.getPosition());
        assertTrue(entity.isActive());
    }

    @Test
    void setPosition_UpdatesCoordinates() {
        TestEntity entity = new TestEntity(new Position(1, 1));
        Position next = new Position(2, 3);

        entity.setPosition(next);

        assertEquals(next, entity.getPosition());
    }

    @Test
    void setPosition_Null_Throws() {
        TestEntity entity = new TestEntity(new Position(0, 0));

        assertThrows(IllegalArgumentException.class, () -> entity.setPosition(null));
    }

    @Test
    void overlaps_SameCoordinates_ReturnsTrue() {
        TestEntity first = new TestEntity(new Position(2, 2));
        TestEntity second = new TestEntity(new Position(2, 2));

        assertTrue(first.overlaps(second));
    }

    @Test
    void overlaps_DifferentCoordinates_ReturnsFalse() {
        TestEntity first = new TestEntity(new Position(2, 2));
        TestEntity second = new TestEntity(new Position(3, 2));

        assertFalse(first.overlaps(second));
    }

    @Test
    void overlaps_NullEntity_ReturnsFalse() {
        TestEntity first = new TestEntity(new Position(2, 2));

        assertFalse(first.overlaps(null));
    }

    @Test
    void setActive_TogglesState() {
        TestEntity entity = new TestEntity(new Position(0, 0));

        entity.setActive(false);
        assertFalse(entity.isActive());

        entity.setActive(true);
        assertTrue(entity.isActive());
    }

    private static final class TestEntity extends Entity {
        TestEntity(Position position) {
            super(position);
        }
    }
}

