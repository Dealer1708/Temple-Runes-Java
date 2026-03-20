package project276.group4.ui;
import project276.group4.domain.types.Direction;
import project276.group4.ui.Screens.GamePauseMenu;
import project276.group4.domain.GameController;
import project276.group4.domain.GameState;
import project276.group4.domain.entities.Player;
import project276.group4.domain.map.GameMap;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Handles player input and maps it to movement and animations.
 * Listens for keyboard events and updates the player's position and sprite animation
 * accordingly. It also supports pausing the game via the ESCAPE key.
 *
 */
public class InputHandler {

    private final Player player;
    private final GameMap map;
    private final ImageView playerNode;
    private final GameController controller;
    private final Stage stage;
    private Image playerImage;

    private PlayerAnimation walkLeft;
    private PlayerAnimation walkRight;
    private PlayerAnimation walkUp;
    private PlayerAnimation walkDown;
    private PlayerAnimation currentAnimation;


    /**
     * Constructs an InputHandler.
     *
     * @param player the player entity to control.
     * @param map the game map for movement logic.
     * @param playerNode the ImageView representing the player on screen.
     * @param controller the game controller to manage pause/resume.
     * @param stage the stage where the game scene is displayed.
     */
    public InputHandler(Player player, 
                        GameMap map,
                        ImageView playerNode,
                        GameController controller,
                        Stage stage) {
        this.player = player;
        this.map = map;
        this.playerNode = playerNode;
        this.controller = controller;
        this.stage = stage;

        // initialize animations
        walkLeft = new PlayerAnimation(playerNode, loadFrames("/assets/images/player_left", 3), 200);
        walkRight = new PlayerAnimation(playerNode, loadFrames("/assets/images/player_right", 3), 200);
        walkUp = new PlayerAnimation(playerNode, loadFrames("/assets/images/player_back", 3), 200);
        walkDown = new PlayerAnimation(playerNode, loadFrames("/assets/images/player_front", 3), 200);
    }

    /**
     * Sets up keyboard input handlers for the provided scene.
     *
     * @param scene The JavaFX scene to attach keyboard listeners to.
     */
    public void setupInputHandlers(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(e -> {
            if (currentAnimation != null) {
                currentAnimation.stop();
            }
        });
    }

    /**
     * Handles key press events and triggers player movement and animations.
     *
     * @param event The key event detected.
     */
    public void handleKeyPress(KeyEvent event) {
        PlayerAnimation nextAnimation = null;

        switch (event.getCode()) {
            case LEFT:
            case A:
                player.move(Direction.LEFT, map);
                nextAnimation = walkLeft;
                break;

            case RIGHT:
            case D:
                player.move(Direction.RIGHT, map);
                nextAnimation = walkRight;
                break;

            case UP:
            case W:
                player.move(Direction.UP, map);
                nextAnimation = walkUp;
                break;

            case DOWN:
            case S:
                player.move(Direction.DOWN, map);
                nextAnimation = walkDown;
                break;

            case ESCAPE:
                controller.pauseGame(stage);
                return;
        }

        if (nextAnimation != null) {
            if (currentAnimation != null) {
                currentAnimation.stop();
            }
            currentAnimation = nextAnimation;
            currentAnimation.play();
        }
    }


    /**
     * Loads an array of images from the resources folder as animation frames.
     *
     * @param basePath the base path of the images, without frame number or extension.
     * @param count the number of frames to load.
     * @return array of Image objects representing animation frames.
     */
    private Image[] loadFrames(String basePath, int count) {
        Image[] frames = new Image[count];
        for (int i = 0; i < count; i++) {
            frames[i] = new Image(
                getClass().getResourceAsStream(basePath + (i + 1) + ".png")
            );
        }
        return frames;
    }
}
