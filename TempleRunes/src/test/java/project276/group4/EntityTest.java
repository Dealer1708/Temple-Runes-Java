package project276.group4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import project276.group4.domain.entities.Entity;
import project276.group4.domain.types.Position;

/**
 * Tests for Entity base class.
 */
public class EntityTest {

    // Test implementation for abstract Entity
    private static class TestEntity extends Entity {
        public TestEntity(Position position) {
            super(position);
        }
    }

    @Test
    public void testEntityCreation() {
        Position pos = new Position(3, 4);
        TestEntity entity = new TestEntity(pos);
        
        assertEquals(pos, entity.getPosition());
        assertTrue(entity.isActive());
    }

    @Test
    public void testSetPosition() {
        TestEntity entity = new TestEntity(new Position(1, 1));
        Position newPos = new Position(5, 6);
        
        entity.setPosition(newPos);
        assertEquals(newPos, entity.getPosition());
    }

    @Test
    public void testActiveState() {
        TestEntity entity = new TestEntity(new Position(0, 0));
        
        assertTrue(entity.isActive());
        
        entity.setActive(false);
        assertFalse(entity.isActive());
    }

    @Test
    public void testOverlaps() {
        Position samePos = new Position(2, 3);
        TestEntity entity1 = new TestEntity(samePos);
        TestEntity entity2 = new TestEntity(samePos);
        TestEntity entity3 = new TestEntity(new Position(4, 5));
        
        assertTrue(entity1.overlaps(entity2));
        assertFalse(entity1.overlaps(entity3));
        assertFalse(entity1.overlaps(null));
    }
}