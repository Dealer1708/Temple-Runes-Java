package project276.group4.ui.Entity;

import project276.group4.domain.GameConstants;
import project276.group4.ui.*;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.entities.Collectible;
import project276.group4.domain.entities.Player;
import project276.group4.domain.types.*;
import project276.group4.ui.Screens.EntitySpawner;
import project276.group4.domain.*;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Manages all collectibles (runes, coins, artifacts) in the game.
 * Handles spawning, rendering, collision detection, collection logic, 
 * score tracking, and exit unlocking conditions based on collectible collection.
 * 
 * Extends {@link EntityManager}
 */
public class CollectibleManager extends EntityManager<Collectible> {

    private int runesCollected;
    private int collectibleCountTotal;

    private int runeValue = 3;
    private int coinValue = 1;
    private int artifactValue = 5;

    private final DefaultScoreManager scoreManager;
    private final DefaultGameSignals signals;
    private final DefaultExitUnlockPolicy exitPolicy;
    private final HUD hud;
    private final GameMap map;
    private final Pane root;
    private final Player player;
    private final EntitySpawner spawner;
    private final int scoreMultiplier;
    private int totalRunes;

    /**
     * Constructs a new CollectibleManager with the parameters.
     *
     * @param root the root pane where collectible nodes will be added
     * @param map the game map used for positioning and spawning logic
     * @param player the player entity for collision detection
     * @param scoreManager the score manager for tracking collected item scores
     * @param signals the game signals for triggering collection events
     * @param exitPolicy the policy determining when the exit should unlock
     * @param hud the heads-up display for updating objective messages
     * @param totalRunes the total runes the game has
     */
    public CollectibleManager(Pane root, GameMap map, Player player, 
                              DefaultScoreManager scoreManager, DefaultGameSignals signals, 
                              DefaultExitUnlockPolicy exitPolicy, HUD hud, int totalRunes) {
        super(root);
        this.root = root;
        this.map = map;
        this.player = player;
        this.scoreManager = scoreManager;
        this.signals = signals;
        this.exitPolicy = exitPolicy;
        this.hud = hud;
        this.spawner = new EntitySpawner(map, player.getPosition());
        this.totalRunes = totalRunes;

        // Determine score multiplier based on difficulty
        DifficultyLevel difficulty = GameSettings.getCurrentDifficulty();
        switch (difficulty) {
            case HARD:
                this.scoreMultiplier = 2;  // double rewards
                break;
            case EASY:
                this.scoreMultiplier = 1;  // easy rewards
                break;
            default:
                this.scoreMultiplier = 1;  // normal rewards
                break;
        }
    }

    /**
     * Initializes all collectibles in the game by spawning runes, coins, and artifacts
     * at positions determined by the EntitySpawner. 
     * Calculates the total number of collectibles for tracking purposes.
     */
    @Override
    public void initialize() {
        EntitySpawner.SpawnResult spawnResult = spawner.spawnAllEntities();

        // runes
        for (var pos : spawnResult.runePositions) {
            spawnCollectible(pos, ItemType.RUNE, runeValue);
        }

        // coins
        for (var pos : spawnResult.coinPositions) {
            spawnCollectible(pos, ItemType.COIN, coinValue);
        }

        // artifacts
        for (var pos : spawnResult.artifactPositions) {
            spawnCollectible(pos, ItemType.ARTIFACT, artifactValue);
        }

        collectibleCountTotal = spawnResult.runePositions.size() + spawnResult.coinPositions.size() + spawnResult.artifactPositions.size();
    }

    /**
     * Helper method to spawn a collectible of a specific type at a given position.
     *
     * @param pos the position where the collectible should be spawned
     * @param type the type of collectible (RUNE, COIN, or ARTIFACT)
     * @param scoreValue the point value awarded when collecting this item
     */
    private void spawnCollectible(Position pos, ItemType type, int baseValue) {
        Collectible collectible = new Collectible(pos, type, baseValue, type.toString());
        ImageView node = createCollectibleNode(type);

        node.setTranslateX(pos.getX() * GameConstants.TILE_SIZE);
        node.setTranslateY(pos.getY() * GameConstants.TILE_SIZE);

        entities.add(collectible);
        entityNodes.add(node);
        root.getChildren().add(node);
    }

   /**
     * Creates an ImageView for a given collectible type by loading the appropriate
     * asset image based on the item type.
     *
     * @param type the type of collectible to create a visual representation for
     * @return an ImageView containing the loaded collectible image, scaled to tile size
     */
    private ImageView createCollectibleNode(ItemType type) {
        String path;
        switch (type) {
            case RUNE:
                path = "/assets/images/runes.png";
                break;
            case COIN: 
                path = "/assets/images/coin.png";
                break;
            case ARTIFACT:
                path = "/assets/images/artifact.png";
                break;
            default:
                path = null;
                break;
        }
        return ResourceLoader.loadImageView(path, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
    }

    /**
     * Updates collectibles by checking for player collision with each collectible.
     * When a collectible is collected, it updates the score, triggers appropriate
     * signals, checks exit unlock conditions, and removes the collectible from the game.
     */
    public void update() {
        for (int i = 0; i < entities.size(); i++) {
            Collectible c = entities.get(i);

            if (c.getPosition().equals(player.getPosition())) {
                System.out.println("Collectible collected: " + c.getName() + ", score: " + scoreManager.getCurrentScore());

                int totalScore = c.getScoreValue() * scoreMultiplier; // apply multiplier
                scoreManager.addScore(totalScore);
                signals.onItemCollected(c.getName(), totalScore);

                if (c.getType() == ItemType.RUNE) {
                    runesCollected++;
                    hud.updateRunesCollected(getRunesCollected());
                    signals.onCollectRune();
                }

                // Check exit unlock condition
                if (exitPolicy.shouldUnlockExit(runesCollected, EntitySpawner.getRuneCountForDifficulty())) {
                    signals.unlockExit();
                    hud.updateObjective();
                }

                // Remove collectible from scene and lists
                root.getChildren().remove(entityNodes.get(i));
                entities.remove(i);
                entityNodes.remove(i);
                i--;
            }
        }
    }

    /**
     * Returns the number of runes collected by the player
     * @return the current count of collected runes
     */
    public int getRunesCollected() { return runesCollected; }

    /**
     * Returns the total number of collectibles that were initially spawned in the game
     * @return the total count of all collectibles in the game
     */
    public int getTotalCollectibles() { return collectibleCountTotal; }
}
