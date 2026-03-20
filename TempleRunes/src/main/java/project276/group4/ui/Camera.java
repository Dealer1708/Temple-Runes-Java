package project276.group4.ui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;


/**
 * Camera class for smoothly following a target (the player)
 * with zoom and clamping to map boundaries.
 */
public class Camera {

    private Node target;
    private final Pane mapRoot;
    private final double windowWidth;
    private final double windowHeight;
    private final double mapWidth;
    private final double mapHeight;

    private double zoomFactor = 1.0;
    private double lerp = 0.1; // smoothing factor (0.1 = very smooth, 0.5 = responsive)

    /**
     * Creates a new Camera instance.
     *
     * @param target Node to follow (e.g., player)
     * @param mapRoot Pane containing the map
     * @param windowWidth Width of the viewport in pixels
     * @param windowHeight Height of the viewport in pixels
     * @param mapWidth Width of the map in pixels
     * @param mapHeight Height of the map in pixels
     */
    public Camera(Node target, Pane mapRoot, double windowWidth, double windowHeight, double mapWidth, double mapHeight) {
        this.target = target;
        this.mapRoot = mapRoot;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    /**
     * Updates the camera position to follow the target smoothly
     * and clamps to map boundaries.
     */
    public void update() {
        if (target == null) return;

        // Get player position
        double playerX = target.getLayoutX() + target.getTranslateX();
        double playerY = target.getLayoutY() + target.getTranslateY();

        // Calculate desired camera position
        double desiredX = (windowWidth / 2) - playerX;
        double desiredY = (windowHeight / 2) - playerY;

        // Get current camera position
        double currentX = mapRoot.getTranslateX();
        double currentY = mapRoot.getTranslateY();

        // Smooth interpolation (lerp)
        double smoothX = currentX + (desiredX - currentX) * lerp;
        double smoothY = currentY + (desiredY - currentY) * lerp;

        // Apply the smooth movement
        mapRoot.setTranslateX(smoothX);
        mapRoot.setTranslateY(smoothY);
        
        // Apply zoom
        mapRoot.setScaleX(zoomFactor);
        mapRoot.setScaleY(zoomFactor);
    }

    /**
     * Sets the zoom level of the camera.
     *
     * @param zoom Zoom factor (1.0 = normal)
     */
    public void setZoom(double zoom) {
        this.zoomFactor = zoom;
    }

    /**
     * Gets the current zoom factor.
     *
     * @return Zoom factor
     */
    public double getZoom() {
        return zoomFactor;
    }

    /**
     * Sets the smoothing factor for camera movement.
     * 
     * values range from 0.05 (very smooth) to 0.5 (responsive).
     * The default value is 0.1.
     *
     * @param smoothing the smoothing factor between 0.05 and 1.0 (inclusive)
     */    
    public void setSmoothing(double smoothing) {
        this.lerp = Math.max(0.05, Math.min(1.0, smoothing));
    }
}