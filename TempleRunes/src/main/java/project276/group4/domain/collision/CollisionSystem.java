package project276.group4.domain.collision;

import project276.group4.domain.entities.*;
import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.interfaces.ScoreManager;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.ItemType;

import java.util.List;

/**
 * Handles collision detection and resolution between the player and other entities.
 * Processes collisions in priority order: Trap > MovingGuardian > CursedIdol > Collectible.
 */
public class CollisionSystem {

    /**
     * Detects and resolves all collisions for the player.
     * Returns the highest priority collision that occurred.
     * 
     * @param player the player entity
     * @param entities all entities in the game
     * @param scoreManager the score manager
     * @param signals the game signals
     * @param map the game map
     * @return the collision outcome with highest priority
     */
    public static CollisionOutcome resolveCollisions(Player player, 
                                                     List<Entity> entities,
                                                     ScoreManager scoreManager,
                                                     GameSignals signals,
                                                     GameMap map) {
        if (!player.isActive() || !player.isAlive()) {
            return CollisionOutcome.none();
        }

        CollisionPriority bestPriority = CollisionPriority.NONE;
        Entity bestEntity = null;

        for (Entity entity : entities) {
            if (entity == player || !entity.isActive()) {
                continue;
            }

            if (!player.overlaps(entity)) {
                continue;
            }

            CollisionPriority priority = determinePriority(entity);
            if (priority == CollisionPriority.NONE) {
                continue;
            }
            if (priority == CollisionPriority.TRAP) {
                return handleCollision(player, entity, CollisionPriority.TRAP, scoreManager, signals, map);
            }
            if (priority.getPriority() > bestPriority.getPriority()) {
                bestPriority = priority;
                bestEntity = entity;
            }
        }

        if (bestEntity == null) {
            return CollisionOutcome.none();
        }

        return handleCollision(player, bestEntity, bestPriority, scoreManager, signals, map);
    }

    /**
     * Handles a specific collision between player and an entity.
     * 
     * @param player the player
     * @param entity the entity collided with
     * @param scoreManager the score manager
     * @param signals the game signals
     * @param map the game map
     * @return the collision outcome
     */
    private static CollisionOutcome handleCollision(Player player, Entity entity,
                                                    CollisionPriority priority,
                                                    ScoreManager scoreManager,
                                                    GameSignals signals,
                                                    GameMap map) {
        switch (priority) {
            case TRAP:
                Trap trap = (Trap) entity;
                trap.onTrigger(player, signals);
                return new CollisionOutcome(CollisionPriority.TRAP, trap, true, 0, false);
            case MOVING_GUARDIAN:
                MovingGuardian guardian = (MovingGuardian) entity;
                guardian.onPlayerContact(player, signals);
                return new CollisionOutcome(CollisionPriority.MOVING_GUARDIAN, guardian, true, 0, false);
            case CURSED_IDOL:
                CursedIdol idol = (CursedIdol) entity;
                idol.onTrigger(scoreManager, signals);
                boolean gameOver = scoreManager.isScoreBelowThreshold();
                return new CollisionOutcome(CollisionPriority.CURSED_IDOL, idol, gameOver, -idol.getScoreDeduction(), false);
            case COLLECTIBLE:
                Collectible collectible = (Collectible) entity;
                collectible.onPickup(scoreManager, signals);
                boolean isRune = collectible.getType() == ItemType.RUNE;
                if (isRune) {
                    signals.onCollectRune();
                }
                return new CollisionOutcome(CollisionPriority.COLLECTIBLE, collectible, false, collectible.getScoreValue(), isRune);
            default:
                return CollisionOutcome.none();
        }
    }

    /**
     * Checks if there's a collision between player and a specific entity.
     * 
     * @param player the player
     * @param entity the entity to check
     * @return true if they overlap, false otherwise
     */
    public static boolean checkCollision(Player player, Entity entity) {
        if (!player.isActive() || !entity.isActive()) {
            return false;
        }
        return player.overlaps(entity);
    }

    private static CollisionPriority determinePriority(Entity entity) {
        if (entity instanceof Trap) {
            return CollisionPriority.TRAP;
        }
        if (entity instanceof MovingGuardian) {
            return CollisionPriority.MOVING_GUARDIAN;
        }
        if (entity instanceof CursedIdol) {
            return CollisionPriority.CURSED_IDOL;
        }
        if (entity instanceof Collectible) {
            Collectible collectible = (Collectible) entity;
            if (!collectible.isCollected()) {
                return CollisionPriority.COLLECTIBLE;
            }
        }
        return CollisionPriority.NONE;
    }
}

