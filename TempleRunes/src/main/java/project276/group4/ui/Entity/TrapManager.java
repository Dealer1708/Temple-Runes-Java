package project276.group4.ui.Entity;

import project276.group4.domain.GameConstants;
import project276.group4.domain.entities.*;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.*;
import project276.group4.ui.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Manages all trap entities in the game, including spawning, visual representation,
 * collision detection, and state updates.
 * 
 * Extends {@link EntityManager}
 */
public class TrapManager extends EntityManager<Trap> {

    private final GameMap map;
    private DefaultGameSignals signals;
    private Player player;
    private final Pane root;
    private Position previousPlayerPosition;

    private List<Position> spawnPositions; // provided by EntitySpawner


    /**
     * Constructs a new TrapManager with the parameters.
     *
     * @param root the root pane where trap nodes will be added for rendering
     * @param player the player entity
     * @param map the game map for position validation
     * @param signals the game signals handler for triggering audio/visual feedback
     * @param trapPositions the pre-generated list of positions for the trap
     */
    public TrapManager(Pane root,
                        Player player, 
                        GameMap map, 
                        DefaultGameSignals signals,
                        List<Position> trapPositions) {
        super(root);
        this.root = root;
        this.player = player;
        this.map = map;
        this.signals = signals;
        this.spawnPositions = trapPositions;

    }

    /**
     * Initializes all traps at their spawn positions.
     * This method is called during game setup and creates trap entities at each position specified in the spawn result. 
     * Traps are initially hidden and displayed as regular path tiles.
     */
    @Override
    public void initialize() {
        for (Position pos : spawnPositions) {
            spawnTrap(pos);
        }
    }

    /**
     * Spawns a single trap at the specified position.
     * 
     * Creates a new {@link Trap} entity and its corresponding visual representation.
     * The trap starts in a hidden state, displayed as a path tile, and becomes
     * visible when activated by player contact.
     *
     * @param pos the grid position of the map where the trap should be spawned
     */
    private void spawnTrap(Position pos) {
        ImageView trapNode = ResourceLoader.loadImageView("/assets/images/path.png", GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
        trapNode.setTranslateX(pos.getX() * GameConstants.TILE_SIZE);
        trapNode.setTranslateY(pos.getY() * GameConstants.TILE_SIZE);

        Trap trap = new Trap(pos);
        
        entities.add(trap);        
        entityNodes.add(trapNode);   
        root.getChildren().add(trapNode);
    }

    /**
     * Updates all traps, handling player collision and state changes.
     * 
     * This method is called every frame and performs the following operations:
     *   - Checks if the player is standing on any trap
     *   - For hidden traps: reveals the trap and plays activation sound
     *   - For revealed traps: damages the player if they just moved onto the trap
     *   - Updates the previous player position for movement detection
     */
    @Override
    public void update() {
        for(int i = 0; i < entities.size(); i++) {
            Trap trap = entities.get(i);
            ImageView trapNode = entityNodes.get(i);

            // check if player is on the trap
            if (trap.getPosition().equals(player.getPosition())) {
                if (trap.isRevealed()) {
                    // check if trap is already revealed - kill player if they just moved onto it
                    if (!player.getPosition().equals(previousPlayerPosition)) {
                        signals.onPlayerDamaged("trap");
                        signals.triggerGameOver(GameOverReason.TRAP_TRIGGERED);
                    }
                } else {
                    // if first time stepping on trap - reveal it
                    trap.reveal();
                    trapNode.setImage(ResourceLoader.loadImage("/assets/images/trap.png"));
                }
                SoundManager.playTrapSound();
            }
        }
        
        // uppdate previous position for next frame
        this.previousPlayerPosition = new Position(player.getPosition().getX(), player.getPosition().getY());
    }

    /**
     * Sets the previous player position for movement detection.
     * 
     * @param position the player's position from the previous game frame
     */
    public void setPreviousPlayerPosition(Position position) {
        this.previousPlayerPosition = position;
    }
}