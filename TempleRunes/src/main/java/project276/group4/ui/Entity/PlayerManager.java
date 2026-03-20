package project276.group4.ui.Entity;

import project276.group4.domain.GameConstants;
import project276.group4.domain.types.Direction;
import project276.group4.domain.entities.Player;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Position;
import project276.group4.ui.DefaultGameSignals;
import project276.group4.ui.ResourceLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Manages the player entity: initialization, movement, and UI updates.
 */
public class PlayerManager extends EntityManager<Player> {

    private final Player player;
    private Position playerSpawn;
    private ImageView playerNode;
    private final GameMap map;
    private final DefaultGameSignals signals;

    /**
     * Creates a new {@code PlayerManager} that controls the given player,
     * spawning them at a specified position and linking them to the UI root and map.
     *
     * @param root the JavaFX root Pane where the player's ImageView will be added
     * @param player the player entity being managed
     * @param pos the player's spawn position on the map
     * @param map the game map used for validating movement and collisions
     * @param signals the signal handler for communicating player events
     */
    public PlayerManager(Pane root, Player player, Position pos, GameMap map, DefaultGameSignals signals) {
        super(root);
        this.player = player;
        this.map = map;
        this.signals = signals;
        this.playerSpawn = pos;
    }

    /**
     * Initializes the player in the UI by setting their starting position,
     * loading their sprite, and adding their ImageView to the scene graph.
     * <p>
     * This method is automatically called by the {@code EntityManager}
     * during entity setup.
     */
    @Override
    public void initialize() {
        // Set player's initial position
        player.setPosition(playerSpawn);

        // create a player ImageView using ResourceLoader
        playerNode = ResourceLoader.loadImageView("/assets/images/player_front2.png", GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
        playerNode.setTranslateX(playerSpawn.getX() * GameConstants.TILE_SIZE);
        playerNode.setTranslateY(playerSpawn.getY() * GameConstants.TILE_SIZE);

        entities.add(player); // store the player entity
        entityNodes.add(playerNode); // store its node
        root.getChildren().add(playerNode);
    }


    /**
     * Updates the player ImageView's position based on the player's
     * current logical position in the game model.
     * <p>
     * This method should be called once per frame in the game loop.
     */
    @Override
    public void update() {
        if (entities.isEmpty()) return;

        Player playerEntity = entities.get(0);
        ImageView playerNode = entityNodes.get(0);

        // Update player node position
        playerNode.setTranslateX(playerEntity.getPosition().getX() * GameConstants.TILE_SIZE);
        playerNode.setTranslateY(playerEntity.getPosition().getY() * GameConstants.TILE_SIZE);
    }

    /**
     * Moves the player in the given direction and updates the player's sprite image.
     * <p>
     * This method performs both the logical movement on the map and
     * the corresponding visual update in the UI.
     *
     * @param direction the direction to move the player (e.g., UP, DOWN)
     * @param imagePath the image file path to update the player's sprite with
     */
    public void movePlayer(Direction direction, String imagePath) {
        player.move(direction, map);
        ImageView node = entityNodes.get(0);
        node.setImage(ResourceLoader.loadImage(imagePath));

        node.setTranslateX(player.getPosition().getX() * GameConstants.TILE_SIZE);
        node.setTranslateY(player.getPosition().getY() * GameConstants.TILE_SIZE);
    }

    /**
     * Returns the JavaFX ImageView representing the player sprite.
     *
     * @return the player's ImageView node
     */
    public ImageView getPlayerNode() { return playerNode; }
}
