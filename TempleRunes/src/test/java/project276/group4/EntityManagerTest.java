package project276.group4;

import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.ui.Entity.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for EntityManager abstract class 
 */
public class EntityManagerTest {

    private TestEntityManager entityManager;
    private Pane root;

    private static class TestEntityManager extends EntityManager<String> {
        public TestEntityManager(Pane root) {
            super(root);
        }

        @Override
        public void initialize() {}

        @Override
        public void update(){}

        //  helper method for testing
        public void testAddEntity(String entity, javafx.scene.image.ImageView node) {
            addEntity(entity, node);
        }

        // helper method to access protected root for testing
        public Pane getRoot() {
            return root;
        }
    }

    @BeforeEach
    public void setUp() {
        root = new Pane();
        entityManager = new TestEntityManager(root);
    }
    
    @Test
    public void testInitialState() {
        assertEquals(0, entityManager.size());
        assertTrue(entityManager.getEntities().isEmpty());
        assertTrue(entityManager.getEntityNodes().isEmpty());
        assertNull(entityManager.getEntityNode());
    }

    @Test
    public void testAddEntity() {
        // a mock ImageView
        javafx.scene.image.ImageView mockNode = new javafx.scene.image.ImageView();
        String testEntity = "Test Entity";

        entityManager.testAddEntity(testEntity, mockNode);

        assertEquals(1, entityManager.size());
        assertEquals(1, entityManager.getEntities().size());
        assertEquals(1, entityManager.getEntityNodes().size());
        assertEquals(testEntity, entityManager.getEntities().get(0));
        assertEquals(mockNode, entityManager.getEntityNodes().get(0));
        assertTrue(entityManager.getRoot().getChildren().contains(mockNode));
    }

    @Test
    public void testRemoveEntityValidIndex() {
        // add entities first
        javafx.scene.image.ImageView node1 = new javafx.scene.image.ImageView();
        javafx.scene.image.ImageView node2 = new javafx.scene.image.ImageView();
        
        entityManager.testAddEntity("Entity1", node1);
        entityManager.testAddEntity("Entity2", node2);

        assertEquals(2, entityManager.size());
        assertTrue(entityManager.getRoot().getChildren().contains(node1));

        // remove first entity
        entityManager.removeEntity(0);

        assertEquals(1, entityManager.size());
        assertEquals("Entity2", entityManager.getEntities().get(0));
        assertEquals(node2, entityManager.getEntityNodes().get(0));
        assertFalse(entityManager.getRoot().getChildren().contains(node1));
        assertTrue(entityManager.getRoot().getChildren().contains(node2));
    }

    @Test
    public void testRemoveEntityInvalidIndex() {

        assertDoesNotThrow(() -> entityManager.removeEntity(-1));
        assertDoesNotThrow(() -> entityManager.removeEntity(0)); // Empty list
        assertDoesNotThrow(() -> entityManager.removeEntity(100));

        // Add one entity and try to remove out of bounds
        javafx.scene.image.ImageView node = new javafx.scene.image.ImageView();
        entityManager.testAddEntity("Test", node);

        assertEquals(1, entityManager.size());
        entityManager.removeEntity(5); // Out of bounds
        assertEquals(1, entityManager.size()); // Should remain unchanged
    }

    @Test
    public void testGetEntityNode() {

        assertNull(entityManager.getEntityNode()); // empty list

        javafx.scene.image.ImageView node = new javafx.scene.image.ImageView();
        entityManager.testAddEntity("Test", node);

        assertEquals(node, entityManager.getEntityNode()); // non-empty list
    }

    @Test
    public void testMultipleEntities() {
        javafx.scene.image.ImageView node1 = new javafx.scene.image.ImageView();
        javafx.scene.image.ImageView node2 = new javafx.scene.image.ImageView();
        javafx.scene.image.ImageView node3 = new javafx.scene.image.ImageView();

        entityManager.testAddEntity("Entity1", node1);
        entityManager.testAddEntity("Entity2", node2);
        entityManager.testAddEntity("Entity3", node3);

        assertEquals(3, entityManager.size());
        assertEquals(3, entityManager.getEntities().size());
        assertEquals(3, entityManager.getEntityNodes().size());
        assertEquals(3, entityManager.getRoot().getChildren().size());

        // order is maintained
        assertEquals("Entity1", entityManager.getEntities().get(0));
        assertEquals("Entity2", entityManager.getEntities().get(1));
        assertEquals("Entity3", entityManager.getEntities().get(2));
    }
}