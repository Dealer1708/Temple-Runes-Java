# Temple Runes - Domain API Integration Guide

This guide explains how to integrate the Temple Runes domain layer (core game engine) with your UI/Controller layer.

## Overview

The domain layer provides a complete game engine with:
- **Entities**: Player, MovingGuardian, Trap, CursedIdol, Collectible
- **Map System**: Grid-based map with walkability checks
- **Collision Detection**: Priority-based collision resolution
- **AI Pathfinding**: BFS-based enemy movement
- **Interfaces**: Clean contracts for score management and game signals

## Core Integration Pattern

### 1. Game Loop Structure

Your `Game.updateTick()` method should follow this pattern:

```java
public void updateTick() {
    // 1. Update enemy AI
    for (MovingGuardian guardian : guardians) {
        guardian.performTurn(player, map);
    }
    
    // 2. Process player movement (from input)
    if (playerInput != null) {
        player.move(playerInput, map);
    }
    
    // 3. Resolve all collisions (CRITICAL - handles priority)
    CollisionOutcome outcome = CollisionSystem.resolveCollisions(
        player, 
        allEntities,  // List<Entity> of all game entities
        scoreManager, 
        gameSignals,
        map
    );
    
    // 4. Handle collision outcome
    if (outcome.isGameOver()) {
        // Game over triggered by collision
        return;
    }
    
    if (outcome.isRune()) {
        runesCollected++;
        // Check if all runes collected
        if (exitUnlockPolicy.shouldUnlockExit(runesCollected, totalRunes)) {
            gameSignals.unlockExit();
            unlockExitDoor(); // Your method to unlock the exit cell
        }
    }
}
```

### 2. Player Movement

Handle player input by calling `Player.move()`:

```java
// On arrow key press
public void handleInput(Direction direction) {
    boolean moved = player.move(direction, map);
    if (!moved) {
        // Movement was blocked (wall, locked door, etc.)
        playSoundEffect("blocked");
    }
}
```

### 3. Collision Priority

The `CollisionSystem` automatically handles priority:
1. **Trap** (highest) → Instant death
2. **MovingGuardian** → Instant death  
3. **CursedIdol** → Score deduction (possible game over if score < 0)
4. **Collectible** (lowest) → Score addition, rune collection

Only the **highest priority** collision is returned per tick.

### 4. Implementing Required Interfaces

#### ScoreManager

```java
public class GameScoreManager implements ScoreManager {
    private int score = 0;
    
    @Override
    public void addScore(int points) {
        score += points;
        updateScoreUI();
    }
    
    @Override
    public void deductScore(int points) {
        score -= points;
        updateScoreUI();
    }
    
    @Override
    public int getCurrentScore() {
        return score;
    }
    
    @Override
    public void resetScore() {
        score = 0;
    }
    
    @Override
    public boolean isScoreBelowThreshold() {
        return score < 0;
    }
}
```

#### GameSignals

```java
public class GameController implements GameSignals {
    @Override
    public void triggerGameOver(GameOverReason reason) {
        this.gameState = GameState.GAME_OVER;
        showGameOverScreen(reason.getMessage());
    }
    
    @Override
    public void triggerVictory() {
        this.gameState = GameState.VICTORY;
        showVictoryScreen();
    }
    
    @Override
    public void unlockExit() {
        // Find exit cell and unlock it
        Cell exitCell = map.getCell(exitPosition);
        exitCell.setLocked(false);
        playSound("exit_unlock");
    }
    
    @Override
    public void onItemCollected(String itemName, int scoreValue) {
        showNotification("Collected " + itemName + " (+" + scoreValue + ")");
    }
    
    @Override
    public void onPlayerDamaged(String damageSource) {
        showNotification("Hit by " + damageSource + "!");
    }
    
    @Override
    public void onCollectRune() {
        runesCollected++;
        setRuneTotals(totalRunes, runesCollected);
    }
    
    @Override
    public void setRuneTotals(int total, int collected) {
        updateRuneCounterUI(collected, total);
    }
}
```

#### ExitUnlockPolicy

```java
public class StandardExitPolicy implements ExitUnlockPolicy {
    @Override
    public boolean shouldUnlockExit(int runesCollected, int totalRunes) {
        return runesCollected >= totalRunes;
    }
}
```

### 5. Map Initialization

Create your map from level data:

```java
public GameMap loadLevel(int levelNumber) {
    // Create map
    GameMap map = new GameMap(width, height);
    
    // Set cell types from level data
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            CellType type = levelData[y][x];
            map.setCell(x, y, new Cell(new Position(x, y), type));
        }
    }
    
    // Find and mark exit as locked
    Position exitPos = map.findCellOfType(CellType.EXIT);
    if (exitPos != null) {
        map.getCell(exitPos).setLocked(true);
    }
    
    return map;
}
```

### 6. Entity Spawning

Spawn entities based on map spawn points:

```java
public void spawnEntities() {
    // Spawn player
    Position playerSpawn = map.findCellOfType(CellType.PLAYER_SPAWN);
    player = new Player(playerSpawn);
    
    // Spawn guardians
    List<Position> enemySpawns = findAllCellsOfType(CellType.ENEMY_SPAWN);
    for (Position pos : enemySpawns) {
        MovingGuardian guardian = new MovingGuardian(pos, 5, 1.0, 100);
        guardians.add(guardian);
    }
    
    // Spawn collectibles
    for (RuneData rune : levelData.getRunes()) {
        Collectible runeItem = new Collectible(
            new Position(rune.x, rune.y),
            ItemType.RUNE,
            rune.scoreValue,
            "Rune"
        );
        collectibles.add(runeItem);
    }
    
    // Add traps
    for (TrapData trap : levelData.getTraps()) {
        Trap trapEntity = new Trap(new Position(trap.x, trap.y));
        traps.add(trapEntity);
    }
    
    // Combine all entities for collision detection
    allEntities = new ArrayList<>();
    allEntities.add(player);
    allEntities.addAll(guardians);
    allEntities.addAll(collectibles);
    allEntities.addAll(traps);
    allEntities.addAll(cursedIdols);
}
```

## Midway Acceptance Criteria Checklist

✅ **Player Movement**: Call `player.move(direction, map)`  
✅ **Walls Block**: `map.isWalkable()` prevents invalid moves  
✅ **Rune Collection**: Collectibles with `ItemType.RUNE` increment counter  
✅ **Score Updates**: ScoreManager methods called automatically  
✅ **Trap Death**: Trap collision triggers instant `gameOver()`  
✅ **Guardian Death**: MovingGuardian collision triggers instant `gameOver()`  
✅ **Exit Unlocking**: Check `shouldUnlockExit()` after each rune  
✅ **Collision Priority**: System handles Trap > Guardian > Idol > Collectible  

## Key Domain Classes Reference

| Class | Package | Purpose |
|-------|---------|---------|
| `Player` | `domain.entities` | Player with movement and health |
| `MovingGuardian` | `domain.entities` | Enemy with AI chase behavior |
| `Trap` | `domain.entities` | Instant-death hazard |
| `CursedIdol` | `domain.entities` | Score-deduction hazard |
| `Collectible` | `domain.entities` | Items (runes, artifacts, coins) |
| `GameMap` | `domain.map` | Grid map with walkability |
| `Cell` | `domain.map` | Individual map tile |
| `CollisionSystem` | `domain.collision` | Collision detection and resolution |
| `Pathfinder` | `domain.ai` | BFS pathfinding for AI |
| `Position` | `domain.types` | Immutable 2D position |
| `Direction` | `domain.types` | Movement directions |

## Build and Compile

```bash
# Compile
mvn clean compile

# Package
mvn clean package

# Run (if main class configured)
mvn exec:java
```

## Common Patterns

### Victory Condition
```java
if (player.overlaps(exitCell.getPosition()) && !exitCell.isLocked()) {
    gameSignals.triggerVictory();
}
```

### Difficulty Scaling
```java
// Adjust guardian speed based on player distance to exit
int distanceToExit = Pathfinder.pathDistance(player.getPosition(), exitPos, map);
for (MovingGuardian guardian : guardians) {
    double speedMultiplier = 1.0 + (0.1 * (10 - distanceToExit));
    guardian.setSpeed(speedMultiplier);
}
```

### Trap Revelation
```java
// Reveal traps when player gets close
for (Trap trap : traps) {
    if (trap.getPosition().manhattanDistance(player.getPosition()) <= 2) {
        trap.reveal();
        // Update cell visual to show revealed trap
    }
}
```




