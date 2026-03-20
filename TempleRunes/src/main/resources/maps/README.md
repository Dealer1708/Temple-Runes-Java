# Map File Format

## Overview
Maps are stored as text files in the `/maps/` directory. Each character in the file represents a different tile type.

## Character Legend

| Character | Meaning | Description |
|-----------|---------|-------------|
| `#` | Wall | Solid wall that blocks movement |
| `.` | Floor | Walkable floor space |
| `S` | Player Spawn | Where the player starts (required, only one) |
| `D` | Door/Exit | Exit door (required, only one) |

## Requirements

1. **Every map must have:**
   - Exactly one player spawn (`S`)
   - Exactly one door/exit (`D`)
   - Be surrounded by walls (`#`) for boundaries

2. **All rows must be the same length** (rectangular map)

3. **Recommended dimensions:** 20-30 rows × 20-30 columns

## Example Maps

### Level 1 (Default)
Located at: `/maps/level1.txt`
- Size: 20×25
- Difficulty: Medium
- Features: Multiple corridors and open areas

### Level 2 (Example)
Located at: `/maps/level2.txt`
- Size: 15×30
- Difficulty: Easy
- Features: Open layout with fewer walls

## Loading Maps

### In Code
```java
// Load a specific map file
char[][] layout = MapLoader.loadMap("/maps/level1.txt");

// Create game map from layout
GameMap map = GameMap.fromMatrix(layout);
```

### Changing the Active Map
Edit `GameView.java`, line ~140:
```java
char[][] layout = MapLoader.loadMap("/maps/level1.txt");  // Change filename here
```

## Creating Custom Maps

1. Create a new `.txt` file in `src/main/resources/maps/`
2. Use the character legend above to design your map
3. Ensure all requirements are met
4. Update `GameView.java` to load your new map

### Tips for Map Design

- **Start simple:** Begin with basic corridors and rooms
- **Test early:** Load and test your map frequently
- **Balance:** Mix open spaces with narrow corridors
- **Spawn placement:** Place player spawn (`S`) away from the exit door (`D`)
- **Strategic walls:** Use walls to create interesting pathways and hiding spots

## Example Custom Map (Small)

```
###############
#.............#
#.#.###.###.#.#
#.#.....#...#.#
#.#####.#.###.#
#.S.....#...D.#
###############
```

This 7×15 map creates a simple maze layout with the player starting on the left (`S`) and the exit on the right (`D`).
