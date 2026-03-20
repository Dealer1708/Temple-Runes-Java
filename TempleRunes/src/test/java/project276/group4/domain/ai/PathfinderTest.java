package project276.group4.domain.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project276.group4.domain.map.Cell;
import project276.group4.domain.map.CellType;
import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Position;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Pathfinder}.
 */
public class PathfinderTest {

    private GameMap map;

    @BeforeEach
    void setUp() {
        map = new GameMap(5, 5);
        fillWithFloor(map);
    }

    private void fillWithFloor(GameMap gameMap) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                gameMap.setCell(x, y, new Cell(new Position(x, y), CellType.FLOOR));
            }
        }
    }

    @Test
    void findPath_ReturnsShortestRoute() {
        Position start = new Position(0, 0);
        Position goal = new Position(4, 0);
        List<Position> path = Pathfinder.findPath(start, goal, map);

        assertNotNull(path);
        assertEquals(goal, path.get(path.size() - 1));
        assertEquals(5, path.size());
    }

    @Test
    void findPath_BlockedGoal_ReturnsNull() {
        Position goal = new Position(2, 2);
        map.setCell(goal, new Cell(goal, CellType.WALL));

        assertNull(Pathfinder.findPath(new Position(0, 0), goal, map));
    }

    @Test
    void findNextStep_UsesGreedyFallbackWhenNoPath() {
        // Surround goal with walls so BFS fails
        Position start = new Position(0, 0);
        Position goal = new Position(2, 0);
        map.setCell(new Position(1, 0), new Cell(new Position(1, 0), CellType.WALL));
        map.setCell(new Position(2, 1), new Cell(new Position(2, 1), CellType.WALL));
        map.setCell(new Position(1, 1), new Cell(new Position(1, 1), CellType.WALL));

        Position next = Pathfinder.findNextStep(start, goal, map);

        assertNotNull(next);
        assertNotEquals(start, next);
        assertTrue(map.isWalkable(next));
    }

    @Test
    void pathDistance_ReturnsNegativeWhenUnreachable() {
        Position start = new Position(0, 0);
        Position goal = new Position(4, 4);
        // build a wall column blocking
        for (int y = 0; y < 5; y++) {
            Position wallPos = new Position(2, y);
            map.setCell(wallPos, new Cell(wallPos, CellType.WALL));
        }

        assertEquals(-1, Pathfinder.pathDistance(start, goal, map));
    }

    
    @Test
    public void findPathTest()
    {
        char[][] layout = { {'.','.','.'} };
        GameMap map = GameMap.fromMatrix(layout);
        Position start = new Position(0, 0);
        Position goal = new Position(2, 0);

        List<Position> path = Pathfinder.findPath(start, goal, map);
        assertNotNull(path);
        assertEquals(3, path.size());
        assertEquals(start, path.get(0));
        assertEquals(goal, path.get(path.size() - 1));
        assertTrue(Pathfinder.hasPath(start, goal, map));
        assertEquals(2, Pathfinder.pathDistance(start, goal, map));
    }

    @Test
    public void isPathBlockedTest()
    {
        char[][] layout = { {'#','#','#'} };
        GameMap map = GameMap.fromMatrix(layout);
        Position start = new Position(0, 0);
        Position goal = new Position(2, 0);

        assertNull(Pathfinder.findPath(start, goal, map));
        assertFalse(Pathfinder.hasPath(start, goal, map));
        assertEquals(-1, Pathfinder.pathDistance(start, goal, map));
    }

    @Test
    public void ChoseGreedyStepTest()
    {
        char[][] layout = {
            {'.','#','#'},
            {'.','#','#'},
            {'.','.','.'} 
        };
        GameMap map = GameMap.fromMatrix(layout);
        Position start = new Position(0, 0);
        Position goal = new Position(2, 2);

        Position next = Pathfinder.findNextStep(start, goal, map);
        assertNotNull(next);
        assertTrue(map.isWalkable(next));
        assertNotEquals(start, next);
    }
}

