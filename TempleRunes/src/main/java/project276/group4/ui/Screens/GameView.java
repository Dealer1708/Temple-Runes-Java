package project276.group4.ui.Screens;

import project276.group4.domain.GameConstants;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import project276.group4.domain.DefaultExitUnlockPolicy;
import project276.group4.domain.DefaultScoreManager;
import project276.group4.domain.GameController;
import project276.group4.domain.GameState;
import project276.group4.domain.entities.Player;
import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;
import project276.group4.services.GameStatsLogger;
import project276.group4.services.LevelLoader;
import project276.group4.ui.Camera;
import project276.group4.ui.DefaultGameSignals;
import project276.group4.ui.HUD;
import project276.group4.ui.InputHandler;
import project276.group4.ui.SoundManager;
import project276.group4.ui.Entity.CollectibleManager;
import project276.group4.ui.Entity.CursedIdolManager;
import project276.group4.ui.Entity.GuardianManager;
import project276.group4.ui.Entity.PlayerManager;
import project276.group4.ui.Entity.TrapManager;
import project276.group4.ui.Screens.EntitySpawner.SpawnResult;

/**
 * This class is responsible for rendering and managing the
 * main gameplay scene, including the player, map, guardians, traps,
 * cursed idols, collectibles, and the HUD elements such as score and timer.
 * 
 * It handles player input, game updates, collision detection, and
 * transitions to other game states (pause, game over, victory).
 */
public class GameView {
    private Stage stage;
    private Scene scene;
    private final Pane mapRoot = new Pane();
    private final Pane hudRoot = new Pane();
    private final Pane overlayRoot = new Pane(); 
    private final StackPane root = new StackPane();

    private GameMap map;
    private Camera camera;

    private GameController controller;
    private AnimationTimer gameLoop;
    private DefaultGameSignals signals;
    private DefaultExitUnlockPolicy exitPolicy;

    private Player player;
    private Image playerImage;
    private ImageView playerNode;

    private int runesCollected = 0;
    private static int totalRunes = 5;

    private Position previousPlayerPosition = null;
    private Position exitDoorPosition;

    private HUD hud;
    private Timeline timer;
    private InputHandler inputHandler;

    private int currentScore = 0;
    private int seconds = 0;
    
    private PlayerManager playerManager;
    private GuardianManager guardianManager;
    private CursedIdolManager cursedIdolManager;
    private TrapManager trapManager; 
    private CollectibleManager collectibleManager;
    private DefaultScoreManager scoreManager;

    /**
     * Constructs the main GameView object
     *
     * @param stage the primary stage used for displaying scenes
     * @param controller the shared game controller managing state and logic
     */
    public GameView(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        controller.setCurrentGameView(this);

        this.exitPolicy = new DefaultExitUnlockPolicy();
        this.signals = controller.getGameSignals(); // already set by controller

        setTotalRunesForDifficulty(); // Set total runes based on difficulty
        
        this.scoreManager = new DefaultScoreManager(totalRunes);

        hud = new HUD(runesCollected, totalRunes, scoreManager);

        DefaultGameSignals defaultSignals = new DefaultGameSignals(this, controller, stage);
        controller.setGameSignals(defaultSignals);
        this.setGameSignals(defaultSignals);

        root.setStyle("-fx-background-color: black;");

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            hud.timerLabel.setText("Time: " + seconds);
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        SoundManager.stopMenuBGM();
        SoundManager.stopVictoryBGM();
        SoundManager.startGameBGM();
    }

    /**
     * Builds the entire game scene: renders map layout, initializes entityManagers, sets up camera,
     * input handler, and starts the main game loop.
     */
    public void buildGameView() {
                
        // Load map from JSON file using LevelLoader
        this.map = LevelLoader.loadFromJson("/maps/level1.json");
        renderMap(map, mapRoot);

        hudRoot.getChildren().addAll(
            hud.objectiveLabel,
            hud.runeLabel,
            hud.scoreLabel,
            hud.timerLabel
        );

        root.getChildren().addAll(mapRoot, hudRoot, overlayRoot);

        this.scene = new Scene(root, stage.getWidth(), stage.getHeight());

        //pass scenes and root for scene managers
        SceneManager sm = new SceneManager(stage, controller);
        sm.setScene(this.scene); 

        Position playerSpawn = map.findCellOfType(CellType.PLAYER_SPAWN);
        player = new Player(playerSpawn);
        playerManager = new PlayerManager(mapRoot, player, playerSpawn, map, signals);
        playerManager.initialize();
        this.playerNode = playerManager.getPlayerNode();
        
        // Use EntitySpawner to handle all entity spawning with collision avoidance
        EntitySpawner spawner = new EntitySpawner(map, playerSpawn);
        SpawnResult spawns = spawner.spawnAllEntities();
        
        guardianManager = new GuardianManager(mapRoot, player, map, signals, spawns.guardianPositions);
        guardianManager.initialize();
        cursedIdolManager = new CursedIdolManager(mapRoot, player, signals, scoreManager, spawns.cursedIdolPositions, hud);
        cursedIdolManager.initialize();
        trapManager = new TrapManager(mapRoot, player, map, signals, spawns.trapPositions);
        trapManager.initialize();
        collectibleManager = new CollectibleManager(mapRoot, map, player, scoreManager, signals, exitPolicy, hud, totalRunes);
        collectibleManager.initialize();

        double mapWidth = map.getWidth() * GameConstants.TILE_SIZE;
        double mapHeight = map.getHeight() * GameConstants.TILE_SIZE;

        inputHandler = new InputHandler(player, map, playerNode, controller, stage);
        inputHandler.setupInputHandlers(scene);

        camera = new Camera(
            playerNode, 
            mapRoot, 
            scene.getWidth(), 
            scene.getHeight(), 
            mapWidth, 
            mapHeight
        );
        camera.setZoom(1.9); 

        startGameLoop();
    }   

    /**
     * Starts the loop responsible for updating
     * all dynamic game entities (player, guardians, traps, collectibles, etc.).
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

            // if game is pause, pause the updates
            if (controller.getCurrentState() != GameState.PLAYING) {return;}
                
                camera.update();    
                guardianManager.update();
                trapManager.update();
                cursedIdolManager.update();
                playerManager.update();
                
                collectibleManager.update();
                updateExitDoor();
                
                // negative score means game over
                if (currentScore < 0) signals.triggerGameOver(GameOverReason.NEGATIVE_SCORE);

                // players have 3 minutes to complete objective
                if (seconds >= 180) {
                    signals.triggerGameOver(GameOverReason.OUT_OF_TIME);
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Checks whether the exit door should be unlocked and if the player has reached it.
     *
     * If the exit conditions are met (based on collected runes and total runes), this
     * method triggers victory by showing the victory menu, stopping the game loop, and
     * signaling the {@link GameSignals} handler.
     * </p>
     */
    private void updateExitDoor() {
        exitDoorPosition = map.findCellOfType(CellType.DOOR);
        if (exitPolicy.shouldUnlockExit(collectibleManager.getRunesCollected(), totalRunes) && player.getPosition().equals(exitDoorPosition)) {
            System.out.println("Player reached the exit door! Victory!");
            
            // Log victory stats
            try {
                GameStatsLogger.logGameStats(currentScore, seconds, "NORMAL", true);
            } catch (Exception e) {
                System.err.println("Failed to log game stats: " + e.getMessage());
            }
            
            gameLoop.stop();
            signals.triggerVictory();
            if (timer != null) timer.stop();
        }
    }

    /**
     * Opens the exit door in the game.
     * This method updates the exit door's visual representation on the map by
     * placing an "opened door" image at the door's position
     */
    public void openExitDoor() {
        System.out.println("Exit door opened!");
        exitDoorPosition = map.findCellOfType(CellType.DOOR);
        // update the displayed tile image
        ImageView door = new ImageView(new Image(getClass().getResourceAsStream("/assets/images/doorOpened.png")));
        door.setFitWidth(GameConstants.TILE_SIZE);
        door.setFitHeight(GameConstants.TILE_SIZE);
        door.setTranslateX(exitDoorPosition.getX() * GameConstants.TILE_SIZE);
        door.setTranslateY(exitDoorPosition.getY() * GameConstants.TILE_SIZE);
        mapRoot.getChildren().add(door);
        return; 
    }

    /**
     * Renders the map layout into visible tiles.
     *
     * @param map the map to render
     * @param root the parent pane for tile nodes
     */
    private void renderMap(GameMap map, Pane root) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                CellType type = map.getCellType(x, y);

                Image tileImage = null;
                switch (type) {
                    case WALL:
                        tileImage = new Image(getClass().getResourceAsStream("/assets/images/wall.png"));
                        break;
                    case DOOR:
                        tileImage = new Image(getClass().getResourceAsStream("/assets/images/doorClosed.png"));
                        break;
                    case FLOOR:
                        tileImage = new Image(getClass().getResourceAsStream("/assets/images/path.png"));
                        break;
                    case PLAYER_SPAWN:
                        tileImage = new Image(getClass().getResourceAsStream("/assets/images/path.png"));
                        break;
                    case ENEMY_SPAWN:
                        tileImage = new Image(getClass().getResourceAsStream("/assets/images/path.png"));
                        break;
                }

                ImageView tileNode = new ImageView(tileImage);
                tileNode.setFitWidth(GameConstants.TILE_SIZE);
                tileNode.setFitHeight(GameConstants.TILE_SIZE);

                tileNode.setTranslateX(x * GameConstants.TILE_SIZE);
                tileNode.setTranslateY(y * GameConstants.TILE_SIZE);

                root.getChildren().add(tileNode);
            }
        }
    }

    /**
     * Sets the {@link GameSignals} handler for this GameView.
     * This allows the GameView to communicate events such as player death,
     * victory, or item collection back to the controller.
     * 
     * @param signals the {@link GameSignals} instance to handle game events
     */
    public void setGameSignals(DefaultGameSignals signals) {
        this.signals = signals;
    }

    /**
     * Updates the internal rune counters for the game.
     * 
     * This method sets the total number of runes in the level and the number
     * of runes collected so far, displayed in the UI.
     * 
     *
     * @param total he total number of runes in the level
     * @param collected the number of runes currently collected
     */
    public void updateRuneVariables(int total, int collected) {
        this.totalRunes = total;
        this.runesCollected = collectibleManager.getRunesCollected();
    }


    /* Getter Methods ---------- */

    /**
     * @return scene the active scene representing the game view
     */
    public Scene getScene() { return scene; }

    /**
     * Determines the number of runes based on current difficulty level.
     * Delegates to EntitySpawner.
     *
     * @return number of runes to spawn
     */
    private int getRuneCountForDifficulty() { return EntitySpawner.getRuneCountForDifficulty();}

    /**
     * Returns the current gameloop of the game
     * @return the gameloop of the game
     */
    public AnimationTimer getGameLoop() {return this.gameLoop;}

    /**
     * Returns the current score of the game
     * @return the current score of the game
     */
    public int getCurrentScore() { return scoreManager.getCurrentScore(); }

    /**
     * Returns the current game timer timeline
     * @return The current Timeline object used for game timing
     */
    public Timeline getCurrentTimer() {return timer;}

    /**
     * Returns the Heads-Up Display (HUD) component
     * 
     * @return The HUD object containing game interface elements
     */
    public HUD getHUD() { return hud; }

    /**
     * Returns the main root pane of the game scene
     * 
     * @return The root Pane containing all game elements
     */
    public Pane getRoot() { return root; }
    
    /**
     * Returns the current game time in seconds
     * 
     * @return The number of seconds elapsed in the current game session
     */
    public int getSeconds() {return seconds;}

    /**
     * Returns the overlay root pane for visual effects (e.g. flashes)
     * 
     * @return The overlay Pane containing visual effects
     */
    public Pane getOverlayRoot() { return overlayRoot;}

    /**
     * Returns the time elapsed since the start of the game, in seconds
     * 
     * @return time elapsed since the start of the game, in seconds
     */
    public int getTimeElapsed() {
        return seconds;
    }

    /**
     * Sets the total number of runes in the level based on difficulty.
     */
    private void setTotalRunesForDifficulty() {
        totalRunes = EntitySpawner.getRuneCountForDifficulty();
    }
}