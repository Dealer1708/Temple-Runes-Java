package project276.group4.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logs game statistics (score, time, etc.) to a file for tracking player performance.
 */
public final class GameStatsLogger {
    
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    /**
     * Saves game statistics to a JSON file in the user's home directory.
     * 
     * @param score the final score
     * @param timeSeconds the time played in seconds
     * @param difficulty the difficulty level
     * @param won whether the player won or lost
     * @throws IOException if the file cannot be written
     */
    public static void logGameStats(int score, int timeSeconds, String difficulty, boolean won) throws IOException {
        GameStats stats = new GameStats();
        stats.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        stats.score = score;
        stats.timeSeconds = timeSeconds;
        stats.timeFormatted = formatTime(timeSeconds);
        stats.difficulty = difficulty;
        stats.result = won ? "WIN" : "LOSS";
        
        // Save to project logs directory
        Path logPath = Paths.get("logs", "game_stats.json").toAbsolutePath();
        String jsonContent = mapper.writeValueAsString(stats);
        
        FileSystemStorage.write(logPath, jsonContent);
        
        System.out.println("Game stats saved to: " + logPath);
    }
    
    /**
     * Formats seconds into MM:SS format.
     */
    private static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    /**
     * Data structure for game statistics.
     */
    public static class GameStats {
        public String timestamp;
        public int score;
        public int timeSeconds;
        public String timeFormatted;
        public String difficulty;
        public String result; // WIN or LOSS
    }
}
