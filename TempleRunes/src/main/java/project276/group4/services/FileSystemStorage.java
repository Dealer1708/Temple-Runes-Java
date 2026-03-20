package project276.group4.services;

import java.io.IOException;
import java.nio.file.*;

/**
 * A utility class for simple file system read/write operations.
 * This class provides static helper methods for reading from and writing to
 * files. When writing, any missing parent directories are automatically created.
 */
public final class FileSystemStorage 
{
    
    /**
     * Reads the full contents of the file at the given path as a string.
     *
     * @param path the path of the file to read
     * @return the contents of the file as a string
     * @throws IOException if the file cannot be read or does not exist
     */
    public static String read(Path path) throws IOException { return Files.readString(path); }

    /**
     * Writes the given content to the file at the specified path.
     * 
     * If the parent directory does not already exist, it will be created.
     * The file will be created if missing, or overwritten if it already exists.
     * 
     * @param path the file path to write to
     * @param content the text content to write into the file
     * @throws IOException if an I/O error occurs during directory creation or writing
     */
    public static void write(Path path, String content) throws IOException
    {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}