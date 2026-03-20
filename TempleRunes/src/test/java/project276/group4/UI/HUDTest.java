package project276.group4.UI;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import project276.group4.domain.DefaultScoreManager;
import project276.group4.ui.HUD;

import static org.junit.jupiter.api.Assertions.*;

public class HUDTest {

    @BeforeAll
    public static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @Test
    public void testHUDCreation() throws Exception {
        HUD hud = new HUD(0, 3, new DefaultScoreManager(0));
        assertNotNull(hud);
    }

    @Test
    public void testHUDLabels() throws Exception {
        HUD hud = new HUD(0, 3, new DefaultScoreManager(0));
        
        assertEquals("Runes: 0/3", hud.runeLabel.getText());
        assertEquals("Score: 0", hud.scoreLabel.getText());
        assertEquals("Time: 0", hud.timerLabel.getText());
        assertEquals("Objective:\ncollect all the runes!", hud.objectiveLabel.getText());
    }

    @Test
    public void testRuneUpdate() throws Exception {
        HUD hud = new HUD(0, 3, new DefaultScoreManager(0));
        hud.updateRunesCollected(2);
        hud.updateRuneDisplay();
        
        assertEquals("Runes: 2/3", hud.runeLabel.getText());
    }

    @Test
    public void testObjectiveUpdate() throws Exception {
        HUD hud = new HUD(0, 3, new DefaultScoreManager(0));
        hud.updateObjective();
        
        assertEquals("Objective: \nFind the exit of the maze!", hud.objectiveLabel.getText());
    }
}