package project276.group4.UI;

import javafx.stage.Stage;
import javafx.scene.Scene;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import project276.group4.domain.GameController;
import project276.group4.domain.types.GameOverReason;
import project276.group4.ui.Screens.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * UI tests for JavaFX menus
 */
public class MenuUITest extends ApplicationTest {

    private Stage testStage;
    private GameController controller;

    @Override
    public void start(Stage stage) {
        this.testStage = stage;
        this.controller = new GameController();
        stage.show();
    }

    @Test
    public void MenuTest() {
        Menu menu = new Menu(testStage, controller);
        Scene scene = menu.getScene();
        assertNotNull(scene, "Menu scene should not be null");
    }

    @Test
    public void TutorialTest() {
        Tutorial menu = new Tutorial(testStage, controller);
        menu.buildTutorialMenu();
        Scene scene = menu.getScene();
        assertNotNull(scene, "Tutorial scene should not be null");
    }

    @Test
    public void GameOverMenuTest() {
        GameOverReason reason = GameOverReason.OUT_OF_TIME; // or any valid reason
        GameOverMenu menu = new GameOverMenu(testStage, controller);
        menu.buildGameOverMenu(reason);
        Scene scene = menu.getScene();
        assertNotNull(scene, "GameOverMenu scene should not be null");
    }

    @Test
    public void VictoryMenuTest() {
        VictoryMenu menu = new VictoryMenu(testStage, controller);
        Scene scene = menu.getScene();
        assertNotNull(scene, "VictoryMenu scene should not be null");
    }

    @Test
    public void testPauseMenuTest() {
        GamePauseMenu menu = new GamePauseMenu(testStage, controller);
        menu.buildPauseMenu();
        Scene scene = menu.getScene();
        assertNotNull(scene, "GamePauseMenu scene should not be null");
    }
}
