package project276.group4.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import project276.group4.domain.map.*;
import java.io.InputStream;
import java.util.List;

/**
 * Utility class for loading game maps from JSON files.
 */
public final class LevelLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Loads a game map from a JSON file.
     * 
     * @param mapPath the path to the map file in resources (e.g., "/maps/level1.json")
     * @return the loaded GameMap
     * @throws RuntimeException if the file cannot be loaded or is invalid
     */
    public static GameMap loadFromJson(String mapPath) {
        try {
            InputStream inputStream = LevelLoader.class.getResourceAsStream(mapPath);
            if (inputStream == null) {
                throw new RuntimeException("Map file not found: " + mapPath);
            }
            
            LevelData data = mapper.readValue(inputStream, LevelData.class);
            
            if (data.width <= 0 || data.height <= 0) {
                throw new RuntimeException("Invalid map dimensions: " + data.width + "x" + data.height);
            }
            if (data.layout == null || data.layout.isEmpty()) {
                throw new RuntimeException("Map layout is empty");
            }
            if (data.layout.size() != data.height) {
                throw new RuntimeException("Layout height mismatch: expected " + data.height + ", got " + data.layout.size());
            }
            
            // Convert string array to char array
            char[][] layout = new char[data.height][data.width];
            for (int y = 0; y < data.height; y++) {
                String row = data.layout.get(y);
                if (row.length() != data.width) {
                    throw new RuntimeException("Row " + y + " width mismatch: expected " + data.width + ", got " + row.length());
                }
                for (int x = 0; x < data.width; x++) {
                    layout[y][x] = row.charAt(x);
                }
            }
            
            System.out.printf("Level loaded: %dx%d (%s)%n", data.width, data.height, mapPath);
            return GameMap.fromMatrix(layout);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to load map: " + mapPath, e);
        }
    }
    
    /**
     * Data structure for JSON level format.
     */
    public static class LevelData {
        public int width;
        public int height;
        public List<String> layout;
    }
}
