package project276.group4.ui.Entity;

import project276.group4.domain.GameConstants;
import project276.group4.domain.entities.CursedIdol;
import project276.group4.domain.entities.Player;
import project276.group4.domain.types.GameOverReason;
import project276.group4.ui.DefaultGameSignals;
import project276.group4.domain.DefaultScoreManager;
import project276.group4.domain.types.Position;
import project276.group4.ui.ResourceLoader;
import project276.group4.ui.HUD;
// import project276.group4.ui.Screens.EntitySpawner.SpawnResult; // removed unused import
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.List;

/**
 * Manages all cursed idol entities in the game.
 * 
 * The CursedIdolManager handles spawning cursed idols on the game map,
 * detecting collisions with the player, deducting points from the shared score,
 * and triggering game-over events if necessary.
 * 
 */
public class CursedIdolManager extends EntityManager<CursedIdol> {

    private final Player player;
    private final DefaultGameSignals signals;
    private final DefaultScoreManager scoreManager; // shared score
    private Position previousPlayerPosition;
    private int scoreDeduction = 3; // default points deducted
    private Runnable onScoreChanged;
    private HUD hud;
    private final List<Position> spawnPositions; // provided by EntitySpawner

    /**
     * Constructs a new CursedIdolManager.
     *
     * @param root the root pane to which idol ImageViews will be added
     * @param player the player instance to detect collisions with
     * @param signals the game signals handler for triggering events
     * @param scoreManager shared score manager for deducting points
     * @param cursedIdolPositions list of positions where idols should spawn
     * @param hud HUD instance for score display updates
     */
    public CursedIdolManager(Pane root,
                            Player player, 
                            DefaultGameSignals signals, 
                            DefaultScoreManager scoreManager,
                            List<Position> cursedIdolPositions,
                            HUD hud) {
        super(root);
        this.player = player;
        this.signals = signals;
        this.scoreManager = scoreManager; 
        this.hud = hud;
        spawnPositions = cursedIdolPositions;
        this.previousPlayerPosition = new Position(player.getPosition().getX(), player.getPosition().getY());

    }

    /**
     * Initializes and spawns all cursed idols on the game map.
     * Each idol is represented with an ImageView added to the root pane.
     */
    @Override
    public void initialize() {
        for (Position pos : spawnPositions) {
            CursedIdol idol = new CursedIdol(pos);
            ImageView idolView = ResourceLoader.loadImageView("/assets/images/cursedIdol.png", GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
            idolView.setTranslateX(pos.getX() * GameConstants.TILE_SIZE);
            idolView.setTranslateY(pos.getY() * GameConstants.TILE_SIZE);
            entities.add(idol);
            entityNodes.add(idolView);
            root.getChildren().add(idolView);
        }
    }

    /**
     * Updates the state of the cursed idols.
     * 
     * Checks if the player has moved onto a cursed idol and:
     *  - Deducts points from the shared score
     *  - Triggers the onScoreChanged callback
     *  - Signals damage and potentially game over if score goes negative
     */

    @Override
    public void update() {
        for (int i = 0; i < entities.size(); i++) {
            CursedIdol cursedIdol = entities.get(i);

            if (!player.getPosition().equals(previousPlayerPosition) 
                    && cursedIdol.getPosition().equals(player.getPosition())) {

                System.out.println("Player touched cursed idol!");
                signals.onPlayerDamaged("Cursed Idol");
                scoreManager.deductScore(scoreDeduction);
                    
                signals.onCursedIdolTouched();

                if (scoreManager.getCurrentScore() < 0) {
                    signals.triggerGameOver(GameOverReason.NEGATIVE_SCORE);
                }
            }
        }

    // Update previous position at the end of update
    previousPlayerPosition = new Position(player.getPosition().getX(), player.getPosition().getY());
    }

    /**
     * Sets the amount of score to deduct when the player touches a cursed idol.
     *
     * @param deduction number of points to deduct
     */
    public void setScoreDeduction(int deduction) {
        this.scoreDeduction = deduction;
    }
}
