package project276.group4.ui.Entity;
import project276.group4.domain.types.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic manager for game entities with corresponding ImageView nodes.
 * Handles creation, removal, and updating of entities in the game scene.
 *
 * @param <T> Type of entity being managed
 */
public abstract class EntityManager<T> {

    /** List of logical entities */
    protected List<T> entities = new ArrayList<>();

    /** List of ImageView nodes corresponding to entities */
    protected List<ImageView> entityNodes = new ArrayList<>();

    /** Parent Pane where the ImageViews are added */
    protected Pane root;

    /**
     * Constructs an EntityManager with the given root pane.
     * @param root the Pane to which all entity nodes will be added
     */
    public EntityManager(Pane root) {
        this.root = root;
    }

    /**
     * Initializes an entity at a given position.
     * Each subclass must implement this according to its entity type.
     */
    public abstract void initialize();

    /**
     * Updates all entities. Usually called each frame.
     * Subclasses define the update logic per entity type.
     */
    public abstract void update();

    /**
     * Adds a new entity and its corresponding ImageView to the manager.
     *
     * @param entity the logical entity to add
     * @param node the ImageView representing the entity visually
     */
    protected void addEntity(T entity, ImageView node) {
        entities.add(entity);
        entityNodes.add(node);
        root.getChildren().add(node);
    }

    /**
     * Removes an entity and its node at the specified index.
     *
     * @param index the index of the entity to remove
     */
    public void removeEntity(int index) {
        if (index >= 0 && index < entities.size()) {
            root.getChildren().remove(entityNodes.get(index));
            entities.remove(index);
            entityNodes.remove(index);
        }
    }

    /**
     * Returns the list of entities.
     * @return list of entities
     */
    public List<T> getEntities() {
        return entities;
    }

    /**
     * Returns the list of ImageView nodes.
     * @return list of nodes
     */
    public List<ImageView> getEntityNodes() {
        return entityNodes;
    }

    /**
     * Returns the number of entities managed.
     * @return entity count
     */
    public int size() {
        return entities.size();
    }

    /**
     * Returns the ImageView node of the entity created.
     * @return node of the entity
     */
    public Node getEntityNode() {
        if (entityNodes.isEmpty()) { return null;}
        return entityNodes.get(0);
    }
}
