package project276.group4.UI;

import javafx.application.Platform;

/**
 * Shared JavaFX toolkit initializer for UI tests.
 * Ensures Platform.startup() is only called once across all test classes.
 */
public class JavaFXInitializer {
    private static boolean initialized = false;

    /**
     * Initializes the JavaFX toolkit if not already initialized.
     * Thread-safe and idempotent.
     */
    public static synchronized void initialize() {
        if (!initialized) {
            try {
                Platform.startup(() -> {});
                initialized = true;
            } catch (IllegalStateException e) {
                // Toolkit already initialized by another test runner
                initialized = true;
            }
        }
    }
}

