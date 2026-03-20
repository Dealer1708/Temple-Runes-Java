package project276.group4.domain.ai;

import project276.group4.domain.map.GameMap;
import project276.group4.domain.types.Direction;
import project276.group4.domain.types.Position;

import java.util.*;

/**
 * Utility class for pathfinding algorithms on a grid-based map.
 * Provides BFS-based pathfinding for enemy AI movement.
 */
public class Pathfinder {

    /**
     * Finds the next position to move towards a target using BFS pathfinding.
     * Returns the immediate next step from current position towards target.
     * 
     * @param from the starting position
     * @param to the target position
     * @param map the game map to navigate
     * @return the next position to move to, or null if no path exists
     */
    public static Position findNextStep(Position from, Position to, GameMap map) {
        if (map == null || from == null || to == null) {
            return null;
        }
        if (from.equals(to)) {
            return from;
        }

        List<Position> path = findPath(from, to, map);
        if (path != null && path.size() >= 2) {
            return path.get(1);
        }

        return chooseGreedyStep(from, to, map);
    }

    /**
     * Finds a complete path from start to goal using Breadth-First Search.
     * 
     * @param start the starting position
     * @param goal the goal position
     * @param map the game map to navigate
     * @return a list of positions representing the path, or null if no path exists
     */
    public static List<Position> findPath(Position start, Position goal, GameMap map) {
        if (map == null || start == null || goal == null) {
            return null;
        }

        if (!map.isInBounds(start) || !map.isInBounds(goal)) {
            return null;
        }

        if (start.equals(goal)) {
            return Collections.singletonList(start);
        }

        if (!map.isWalkable(goal)) {
            return null;
        }

        Queue<Position> queue = new LinkedList<>();
        Map<Position, Position> cameFrom = new HashMap<>();
        Set<Position> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, start, goal);
            }

            // Check all four directions
            for (Direction dir : Direction.values()) {
                Position neighbor = dir.apply(current);

                if (!visited.contains(neighbor) && map.isWalkable(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return null; // No path found
    }

    /**
     * Reconstructs the path from the BFS search results.
     * 
     * @param cameFrom map of position to previous position
     * @param start the start position
     * @param goal the goal position
     * @return the complete path as a list of positions
     */
    private static List<Position> reconstructPath(Map<Position, Position> cameFrom, 
                                                   Position start, Position goal) {
        List<Position> path = new ArrayList<>();
        Position current = goal;

        while (current != null) {
            path.add(0, current);
            current = cameFrom.get(current);
        }

        return path;
    }

    /**
     * Checks if there's a valid path between two positions.
     * 
     * @param from the starting position
     * @param to the target position
     * @param map the game map
     * @return true if a path exists, false otherwise
     */
    public static boolean hasPath(Position from, Position to, GameMap map) {
        return findPath(from, to, map) != null;
    }

    /**
     * Calculates the path distance (number of steps) between two positions.
     * 
     * @param from the starting position
     * @param to the target position
     * @param map the game map
     * @return the number of steps in the path, or -1 if no path exists
     */
    public static int pathDistance(Position from, Position to, GameMap map) {
        List<Position> path = findPath(from, to, map);
        return (path != null) ? path.size() - 1 : -1;
    }

    /**
     * Gets all walkable neighbors of a position.
     * 
     * @param position the center position
     * @param map the game map
     * @return a list of walkable neighbor positions
     */
    public static List<Position> getWalkableNeighbors(Position position, GameMap map) {
        List<Position> neighbors = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Position neighbor = dir.apply(position);
            if (map.isWalkable(neighbor)) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private static Position chooseGreedyStep(Position from, Position to, GameMap map) {
        Position best = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            Position candidate = dir.apply(from);
            if (!map.isWalkable(candidate)) {
                continue;
            }
            int distance = candidate.manhattanDistance(to);
            if (distance < bestDistance || (distance == bestDistance && best == null)) {
                bestDistance = distance;
                best = candidate;
            }
        }

        return best;
    }
}

