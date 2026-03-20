package project276.group4.domain.entities;

import project276.group4.domain.ai.Pathfinder;
import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.GameOverReason;
import project276.group4.domain.types.Position;

/**
 * Represents a moving enemy guardian that patrols the map and chases the player.
 * Guardians have vision range, speed, and can kill the player on contact.
 */
public class MovingGuardian extends Entity {
    private final int visionRange;
    private final int damage;
    private double baseSpeed;
    private double speed;
    private boolean isChasing;
    private Position previousPosition; // Track previous position to avoid backtracking

    /**
     * Creates a new moving guardian at the specified position.
     * 
     * @param position the initial position
     * @param visionRange the distance at which the guardian can see the player
     * @param speed the movement speed (moves every speed ticks)
     * @param damage the damage dealt on contact
     */
    public MovingGuardian(Position position, int visionRange, double speed, int damage) {
        super(position);
        this.visionRange = visionRange;
        this.baseSpeed = speed;
        this.speed = speed;
        this.damage = damage;
        this.isChasing = false;
        this.previousPosition = null; // Initially no previous position
    }
    
        /**
         * Creates a guardian with default values (vision=5, speed=1.0, damage=100).
         * 
         * @param position the initial position
         */
        public MovingGuardian(Position position) {
            this(position, 5, 1.0, 100);
        }
    
        /**
         * Gets the vision range of this guardian.
         * 
         * @return the vision range in tiles
         */
        public int getVisionRange() {
            return visionRange;
        }
    
        /**
         * Gets the speed of this guardian.
         * 
         * @return the speed multiplier
         */
        public double getSpeed() {
            return speed;
        }

        /**
         * Sets the base speed of this guardian (for dynamic difficulty).
         * 
         * @param speed the new speed value
         */
        public void setSpeed(double speed) {
            this.baseSpeed = speed;
            this.speed = speed;
        }
    
        /**
         * Gets the damage this guardian deals.
         * 
         * @return the damage value
         */
        public int getDamage() {
            return damage;
        }
    
        /**
         * Checks if this guardian is currently chasing the player.
         * 
         * @return true if chasing, false otherwise
         */
        public boolean isChasing() {
            return isChasing;
        }
    
        /**
         * Checks if the player is within vision range.
         * Uses both distance and line-of-sight checks.
         * 
         * @param player the player entity
         * @param map the game map
         * @return true if player is visible, false otherwise
         */
        public boolean isPlayerInVision(Player player, GameMap map) {
            if (!active || !player.isActive()) {
                return false;
            }
    
            // Check Manhattan distance first (quick check)
            int distance = position.manhattanDistance(player.getPosition());
            if (distance > visionRange) {
                return false;
            }
    
            // Check line of sight
            return map.hasLineOfSight(position, player.getPosition());
        }
    
        /**
         * Performs one turn of guardian AI behavior.
         * Moves towards the player if in vision, otherwise patrols randomly.
         * 
         * @param player the player entity
         * @param map the game map
         */
        public void performTurn(Player player, GameMap map) {
            if (!active || !player.isActive()) {
                return;
            }
            updateSpeed(player, map);
            // Check if player is in vision
            if (isPlayerInVision(player, map)) {
                isChasing = true;
                moveTowardsPlayer(player, map);
            } else {
                isChasing = false;
                // Move constantly even when player is not visible - patrol behavior
                performPatrolMovement(map);
            }
        }
    
        /**
         * Moves the guardian one step towards the player using pathfinding.
         * 
         * @param player the player to chase
         * @param map the game map
         */
        private void moveTowardsPlayer(Player player, GameMap map) {
            Position playerPosition = player.getPosition();
            if (playerPosition == null) {
                return;
            }
            Position nextStep = Pathfinder.findNextStep(position, playerPosition, map);

            if (nextStep != null && !nextStep.equals(position)) {
                this.previousPosition = this.position; // Track previous position
                this.position = nextStep;
            }
        }

        /**
         * Performs random patrol movement when player is not in vision.
         * Guardian moves randomly to adjacent walkable cells to maintain constant movement.
         * Avoids immediate backtracking to prevent oscillating movement.
         * 
         * @param map the game map
         */
        private void performPatrolMovement(GameMap map) {
            Position[] directions = getAdjacentPositions();
            shuffleArray(directions);
            
            // Try to move without backtracking first
            if (tryMoveWithoutBacktracking(directions, map)) {
                return;
            }
            
            // Fallback: allow backtracking if stuck
            tryMoveWithBacktracking(directions, map);
        }

        /**
         * Gets all four adjacent positions (up, down, left, right).
         * 
         * @return array of adjacent positions
         */
        private Position[] getAdjacentPositions() {
            return new Position[] {
                new Position(position.getX() + 1, position.getY()),     // Right
                new Position(position.getX() - 1, position.getY()),     // Left
                new Position(position.getX(), position.getY() + 1),     // Down
                new Position(position.getX(), position.getY() - 1)      // Up
            };
        }

        /**
         * Shuffles an array in place using Fisher-Yates algorithm.
         * 
         * @param array the array to shuffle
         */
        private void shuffleArray(Position[] array) {
            for (int i = array.length - 1; i > 0; i--) {
                int j = (int) (Math.random() * (i + 1));
                Position temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        /**
         * Attempts to move to a valid position that is not the previous position.
         * 
         * @param directions array of candidate positions
         * @param map the game map
         * @return true if a move was made, false otherwise
         */
        private boolean tryMoveWithoutBacktracking(Position[] directions, GameMap map) {
            for (Position newPosition : directions) {
                if (isValidNonBacktrackingMove(newPosition, map)) {
                    moveTo(newPosition);
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks if a position is a valid move that doesn't backtrack.
         * 
         * @param newPosition the position to check
         * @param map the game map
         * @return true if valid and not backtracking
         */
        private boolean isValidNonBacktrackingMove(Position newPosition, GameMap map) {
            return map.isWalkable(newPosition) && 
                   (previousPosition == null || !newPosition.equals(previousPosition));
        }

        /**
         * Attempts to move to any walkable position, allowing backtracking.
         * 
         * @param directions array of candidate positions
         * @param map the game map
         */
        private void tryMoveWithBacktracking(Position[] directions, GameMap map) {
            for (Position newPosition : directions) {
                if (map.isWalkable(newPosition)) {
                    moveTo(newPosition);
                    break;
                }
            }
        }

        /**
         * Moves to a new position and updates previous position tracking.
         * 
         * @param newPosition the position to move to
         */
        private void moveTo(Position newPosition) {
            this.previousPosition = this.position;
            this.position = newPosition;
        }
    
        /**
         * Handles contact with the player, causing instant death.
         * 
         * @param player the player entity
         * @param signals the game signals to notify
         */
        public void onPlayerContact(Player player, GameSignals signals) {
            if (!active) {
                return;
            }
    
            // Kill the player instantly
            player.kill();
    
            // Signal game over
            signals.onPlayerDamaged("MovingGuardian");
            signals.triggerGameOver(GameOverReason.GUARDIAN_CONTACT);
        }
    
        @Override
        public String toString() {
            return "MovingGuardian{" +
                    "pos=" + position +
                    ", visionRange=" + visionRange +
                    ", speed=" + speed +
                    ", chasing=" + isChasing +
                    '}';
        }

        private void updateSpeed(Player player, GameMap map) {
            if (player == null || map == null) {
                speed = baseSpeed;
                return;
            }
            Position playerPosition = player.getPosition();
            if (playerPosition == null) {
                speed = baseSpeed;
                return;
            }
            Position exitPosition = map.findCellOfType(CellType.EXIT);
            if (exitPosition == null) {
                speed = baseSpeed;
                return;
            }

            int distanceToExit = playerPosition.manhattanDistance(exitPosition);
            int boostRange = Math.max(1, visionRange * 2);
            int clampedDistance = Math.min(distanceToExit, boostRange);
            double normalized = (boostRange - clampedDistance) / (double) boostRange;
            speed = baseSpeed * (1.0 + normalized);
        }
    }


    